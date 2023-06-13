package com.example.pokebag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokebag.api.PokemonService;
import com.example.pokebag.model.Pokemon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class ProcurarPokemon extends AppCompatActivity {

    private EditText editTextNomePoke;
    private ImageView fotoPokemon;
    private Button buttonProcurar, buttonAdd;
    private Retrofit retrofit;
    private TextView nomePokemon, tipoPokemon, spritePokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurar_pokemon);
        getSupportActionBar().hide();
        startComponents();
        buttonProcurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                RecuperarPokemonObject(view, context);
                buttonAdd.setVisibility(View.VISIBLE);

            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPokemon(v);
            }
        });
    }

    private void RecuperarPokemonObject(View view, Context context) {
        PokemonService pokemonService = retrofit.create(PokemonService.class);
        String pokemonConsulta = editTextNomePoke.getText().toString();
        Call<JsonObject> call = pokemonService.RecuperarPokeObject(pokemonConsulta);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    JsonArray typesArray = jsonObject.getAsJsonArray("types");
                    JsonObject typeObject = typesArray.get(0).getAsJsonObject(); // Acessando o primeiro elemento do array
                    JsonObject typeNameObject = typeObject.getAsJsonObject("type");
                    String typeName = typeNameObject.get("name").getAsString();

                    JsonObject speciesJson = jsonObject.getAsJsonObject("species");
                    String pokemonName = speciesJson.get("name").getAsString();

                    JsonObject spritesJson = jsonObject.getAsJsonObject("sprites");
                    String spriteLink = spritesJson.get("front_default").getAsString();

                    RequestOptions options = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true);

                    Glide.with(context)
                            .load(spriteLink)
                            .apply(options)
                            .into(fotoPokemon);

                    spritePokemon.setText(spriteLink);
                    tipoPokemon.setText(typeName);
                    nomePokemon.setText(pokemonName);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(view,"erro", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }

    private void addPokemon(View view) {
        String nome_pokemon = nomePokemon.getText().toString();
        String tipo_pokemon = tipoPokemon.getText().toString();
        String sprite_pokemon = spritePokemon.getText().toString();

        Pokemon pokemon = new Pokemon(nome_pokemon, tipo_pokemon, sprite_pokemon);
        if(nome_pokemon.isEmpty()) {
            Snackbar snackbar = Snackbar.make(view,"Erro ao adicionar pokemon", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DocumentReference docRef = db.collection("Treinadores").document(userId);
            docRef.update("pokemons", FieldValue.arrayUnion(pokemon)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("db","sucesso");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("db_error","erro: " + e.toString());
                }
            });
            Intent intent = new Intent(ProcurarPokemon.this, ListaPokemons.class);
            startActivity(intent);
            finish();

        }

    }

    private void startComponents() {
        editTextNomePoke = findViewById(R.id.editTextNomePoke);
        fotoPokemon = findViewById(R.id.fotoPokemon);
        buttonProcurar = findViewById(R.id.buttonProcurar);
        buttonAdd = findViewById(R.id.buttonAdd);
        nomePokemon = findViewById(R.id.nomePokemon);
        tipoPokemon = findViewById(R.id.tipoPokemon);
        spritePokemon = findViewById(R.id.spritePokemon);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
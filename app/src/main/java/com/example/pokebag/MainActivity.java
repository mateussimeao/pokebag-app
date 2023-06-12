package com.example.pokebag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pokebag.api.PokemonService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private EditText editTextLogin, editTextSenha;
    private Button button_login;
    private TextView text_tela_cadastro;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startComponents();

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextLogin.getText().toString();
                String senha = editTextSenha.getText().toString();
                //Intent intent = new Intent(MainActivity.this, TelaCadastro.class);
                //startActivity(intent);
            }
        });
        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TelaCadastro.class);
                startActivity(intent);
            }
        });
    }

    private void startComponents() {
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextSenha = findViewById(R.id.editTextSenha);
        button_login = findViewById(R.id.button_login);
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void RecuperarPokemonObject() {
        PokemonService pokemonService = retrofit.create(PokemonService.class);
        Call<JsonObject> call = pokemonService.RecuperarPokeObject("squirtle");
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


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
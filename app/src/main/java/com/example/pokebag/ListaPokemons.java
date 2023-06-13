package com.example.pokebag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.pokebag.model.Pokemon;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListaPokemons extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<Pokemon> listaPokemons;
    private Button button_edit_list;
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DocumentReference docRef = db.collection("Treinadores").document(userId);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pokemons);
        getSupportActionBar().hide();
        startComponents();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaPokemons = new ArrayList<>();
        myAdapter = new MyAdapter(this,listaPokemons);
        recyclerView.setAdapter(myAdapter);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    List<Map<String, Object>> pokemons = (List<Map<String, Object>>) documentSnapshot.get("pokemons");
                    if (pokemons != null) {
                        for (Map<String, Object> pokemon : pokemons) {
                            String name = (String) pokemon.get("name");
                            String type = (String) pokemon.get("type");
                            String sprite = (String) pokemon.get("sprite");
                            Pokemon p = new Pokemon(name, type, sprite);
                            listaPokemons.add(p);
                            Log.d("POKEMON", "teste"+listaPokemons);

                        }
                        myAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void startComponents() {
        button_edit_list = findViewById(R.id.button_edit_list);

    }
}
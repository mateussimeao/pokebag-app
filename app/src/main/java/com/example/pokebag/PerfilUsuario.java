package com.example.pokebag;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class PerfilUsuario extends AppCompatActivity {
    private TextView text_user, text_name;
    private Button button_explore, button_sair, button_list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        getSupportActionBar().hide();
        startComponents();

        button_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilUsuario.this, ProcurarPokemon.class);
                startActivity(intent);
            }
        });
        button_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilUsuario.this, ListaPokemons.class);
                startActivity(intent);
            }
        });



    }


    private void startComponents() {
        text_user = findViewById(R.id.text_user);
        text_name = findViewById(R.id.text_name);
        button_explore = findViewById(R.id.button_explore);
        button_list = findViewById(R.id.button_list);
        button_sair = findViewById(R.id.button_sair);
    }
}
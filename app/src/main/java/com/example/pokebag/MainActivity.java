package com.example.pokebag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pokebag.api.PokemonService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private TextView text_tela_cadastro, mainTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        startComponents();

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextLogin.getText().toString();
                if(username.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view,"Preencha todos os campos", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else{
                    autenticar(view);
                }


                //Intent intent = new Intent(MainActivity.this, ProcurarPokemon.class);
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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            Intent intent = new Intent(MainActivity.this, PerfilUsuario.class);
            startActivity(intent);
            finish();
        }
    }

    private void startComponents() {
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextSenha = findViewById(R.id.editTextSenha);
        button_login = findViewById(R.id.button_login);
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        mainTitle = findViewById(R.id.mainTitle);

    }

    private void autenticar(View view) {
        String username = editTextLogin.getText().toString();
        String senha = editTextSenha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(username+"@mail.com",senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this, PerfilUsuario.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    try {
                        throw task.getException();
                    } catch(Exception e){
                        Snackbar snackbar = Snackbar.make(view,"erro: "+ e.toString(), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }
            }
        });
    }

}
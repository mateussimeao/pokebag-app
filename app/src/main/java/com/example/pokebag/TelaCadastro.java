package com.example.pokebag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pokebag.model.Pokemon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaCadastro extends AppCompatActivity {

    private EditText editTextNomeCadastro, editTextUserCadastro, editTextSenhaCadastro;
    private Button button_cadastro;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);
        getSupportActionBar().hide();
        startComponents();

        button_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = editTextNomeCadastro.getText().toString();
                String id = editTextUserCadastro.getText().toString();

                if(nome.isEmpty() || id.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view,"Preencha todos os campos", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else {
                    cadastrar(view);
                }
            }
        });
    }
    private void cadastrar(View view) {

        String username = editTextUserCadastro.getText().toString();
        String senha = editTextSenhaCadastro.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(username+"@mail.com",senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    salvarUsuario(view);
                    Snackbar snackbar = Snackbar.make(view,"Cadastro realizado com sucesso!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else {
                    String error;
                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthUserCollisionException e) {
                        error = "Usuario ja cadastrado";
                    }
                    catch (Exception e){
                        error = "Erro no cadastro";
                    }
                    Snackbar snackbar = Snackbar.make(view,error, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });

    }

    private void salvarUsuario(View view) {
        String nome = editTextNomeCadastro.getText().toString();
        List<Pokemon> listaPokemons = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> treinadores = new HashMap<>();
        String username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        int index = username.indexOf('@');
        String showUsername = username.substring(0, index);
        treinadores.put("nome", nome);
        treinadores.put("username", showUsername);
        treinadores.put("pokemons", listaPokemons);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docRef = db.collection("Treinadores").document(userId);
        docRef.set(treinadores).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db","sucesso");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db_error","erro: " + e.toString());
                Snackbar snackbar = Snackbar.make(view,e.toString(), Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

    }

    private void startComponents() {
        editTextNomeCadastro = findViewById(R.id.editTextNomeCadastro);
        editTextUserCadastro = findViewById(R.id.editTextUserCadastro);
        editTextSenhaCadastro = findViewById(R.id.editTextSenhaCadastro);
        button_cadastro = findViewById(R.id.button_cadastro);
    }
}
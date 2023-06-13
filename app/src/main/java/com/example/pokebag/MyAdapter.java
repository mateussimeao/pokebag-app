package com.example.pokebag;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokebag.model.Pokemon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Pokemon> listaPokemons;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DocumentReference docRef = db.collection("Treinadores").document(userId);


    public MyAdapter(Context context, ArrayList<Pokemon> listaPokemons) {
        this.context = context;
        this.listaPokemons = listaPokemons;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_pokemon, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Pokemon pokemon = listaPokemons.get(position);
        int pos = position;
        holder.name.setText(pokemon.getName());
        holder.type.setText(pokemon.getType());
        Glide.with(context)
                .load(pokemon.getSprite())
                .into(holder.sprite);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> updates = new HashMap<>();

                String currentPokemon = pokemon.getName();
                updates.put("pokemons", FieldValue.arrayRemove(pokemon));
                docRef.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firestore333", "Elemento removido do array com sucesso");
                        Snackbar snackbar = Snackbar.make(v,currentPokemon+" removido", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore333", "Erro ao remover elemento do array" + e.toString(), e);
                        Snackbar snackbar = Snackbar.make(v,"Erro ao remover pokemon", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return listaPokemons.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, type;
        ImageView sprite;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            sprite = itemView.findViewById(R.id.sprite);
        }
    }
}
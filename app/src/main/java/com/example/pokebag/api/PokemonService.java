package com.example.pokebag.api;

import com.example.pokebag.model.Pokemon;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokemonService {

    @GET("pokemon/{pokemon}/")
    Call<Pokemon> RecuperarPokemon(@Path("pokemon") String pokemon);

    @GET("pokemon/{pokemon}/")
    Call<JsonObject> RecuperarPokeObject(@Path("pokemon") String pokemon);

}

package com.example.android.mypokemon.json;

import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PokemonJsonParser {

    private static final String LOG_TAG = PokemonJsonParser.class.getSimpleName();
    private static final String POKEMONS = "pokemons";
    private static final String POKEMON_ID = "pokemon_id";
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lng";

    public PokemonJsonParser() {}

    public List<Pokemon> parsePokemonFromJson(String json) {
        List<Pokemon> result = new ArrayList<>();
        try {
            JSONArray pokemons = new JSONObject(json).getJSONArray(POKEMONS);
            for (int i = 0; i < pokemons.length(); i++) {
                JSONObject pokemon = pokemons.getJSONObject(i);
                int id = pokemon.getInt(POKEMON_ID);
                double lat = pokemon.getDouble(LATITUDE);
                double lng = pokemon.getDouble(LONGITUDE);
                Log.d(LOG_TAG, "(" + id + ", " + lat + ", " + lng + ")");

                Location location = new Location("mypokemon");
                location.setLatitude(lat);
                location.setLongitude(lng);
                result.add(new Pokemon(id, location));
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
        return result;
    }
}

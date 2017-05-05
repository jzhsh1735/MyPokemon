package com.example.android.mypokemon.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.android.mypokemon.json.Pokemon;
import com.example.android.mypokemon.json.PokemonJsonParser;
import com.example.android.mypokemon.notification.PokemonNotification;

import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PokemonSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String LOG_TAG = PokemonSyncAdapter.class.getSimpleName();
    // TODO: Move to SharedPreference.
    private static final boolean ENABLE_DISTANCE = true;
    private static final double RADIUS_METERS = 1000;

    private final Context context;
    private Location location = null;
    private List<Pokemon> pokemons = null;

    public PokemonSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.context = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Start sync");
        SmartLocation.with(context).location().oneFix().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location loc) {
                Log.d(LOG_TAG, "location: (" + loc.getLatitude() + ", " + loc.getLongitude() + ")");
                location = loc;
                SearchPokemonTask task = new SearchPokemonTask();
                task.execute();
            }
        });
    }

    private class SearchPokemonTask extends AsyncTask<Void, String, String> {

        private static final String BASE_URL = "https://sgpokemap.com/query2.php?";
        private static final String TIME_PARAM = "since";
        private static final String POKEMON_PARAM = "mons";
        private static final String REFERER_HEADER = "https://sgpokemap.com/";
        private static final String USER_AGENT_HEADER = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";
        private static final String X_REQUESTED_WITH_HEADER = "XMLHttpRequest";
        // TODO: Move to SharedPreference.
        private static final String POKEMONS = "113,115,122,173,179,180,181,201,203,214,225,235,236,237,241,242,243,244,245,246,247,248";

        @Override
        protected String doInBackground(Void... params) {
            String url = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(TIME_PARAM, "0")
                    .appendQueryParameter(POKEMON_PARAM, POKEMONS)
                    .build()
                    .toString();
            Log.d(LOG_TAG, "url: " + url);
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("referer", REFERER_HEADER)
                        .addHeader("user-agent", USER_AGENT_HEADER)
                        .addHeader("x-requested-with", X_REQUESTED_WITH_HEADER)
                        .build();
                Response response = client.newCall(request).execute();
                String json = response.body().string();
                Log.d(LOG_TAG, "json: " + json);
                return json;
            } catch (Exception e) {
                Log.e(LOG_TAG, "error: " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            PokemonJsonParser pokemonJsonParser = new PokemonJsonParser();
            pokemons = pokemonJsonParser.parsePokemonFromJson(result);

            for (Pokemon pokemon : pokemons) {
                if (!ENABLE_DISTANCE || pokemon.isWithinArea(location, RADIUS_METERS)) {
                    Log.d("Notification", pokemon.toString());
                    PokemonNotification notification = new PokemonNotification(getContext(), pokemon);
                    notification.notifyPokemon();
                }
            }
        }
    }
}

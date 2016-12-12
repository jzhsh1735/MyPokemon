package com.example.android.mypokemon;

import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textView = null;
    private Button button = null;
    private Location location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.search_result);
        button = (Button) findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartLocation.with(MainActivity.this).location().oneFix().start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location loc) {
                        location = loc;
                        new SearchPokemonTask().execute();
                    }
                });
            }
        });
    }

    private class SearchPokemonTask extends AsyncTask<Void, String, String> {

        private final String TAG = SearchPokemonTask.class.getSimpleName();
        private final String BASE_URL = "https://sgpokemap.com/query2.php?";
        private final String TIME_PARAM = "since";
        private final String POKEMON_PARAM = "mons";
        private final String REFERER_HEADER = "https://sgpokemap.com/";
        private final String USER_AGENT_HEADER = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";
        private final String X_REQUESTED_WITH_HEADER = "XMLHttpRequest";

        @Override
        protected String doInBackground(Void... params) {
            Log.d(TAG, "lat: " + location.getLatitude());
            Log.d(TAG, "long: " + location.getLongitude());
            String pokemon = "4,5,6,83,88,89,106,115,122,131,132,138,139,140,141,143,144,145,146,147,148,149";
            String url = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(TIME_PARAM, "0")
                    .appendQueryParameter(POKEMON_PARAM, pokemon)
                    .build()
                    .toString();
            Log.d(TAG, "url: " + url);
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
                //Log.d(TAG, "json: " + json);
                return json;
            } catch (Exception e) {
                Log.e(TAG, "error: " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }
    }
}

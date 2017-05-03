package com.example.android.mypokemon.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PokemonAuthenticatorService extends Service {

    private PokemonAuthenticator authenticator = null;

    @Override
    public void onCreate() {
        authenticator = new PokemonAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}

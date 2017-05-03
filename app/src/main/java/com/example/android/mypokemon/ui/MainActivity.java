package com.example.android.mypokemon.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.mypokemon.R;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String AUTHORITY = "com.example.android.mypokemon";
    private static final String ACCOUNT_TYPE = "mypokemon.example.com";
    private static final String ACCOUNT = "mypokemon";
    // TODO: Move to SharedPreference.
    private static final long SYNC_INTERVAL_SECS = 300L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Account account = createAccount();
        ContentResolver.setSyncAutomatically(account, AUTHORITY, true);
        ContentResolver.addPeriodicSync(account, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL_SECS);
    }

    private Account createAccount() {
        Account account = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(account, null, null)) {
            Log.d(LOG_TAG, "Account added");
        } else {
            Log.d(LOG_TAG, "Account exist");
        }
        return account;
    }
}

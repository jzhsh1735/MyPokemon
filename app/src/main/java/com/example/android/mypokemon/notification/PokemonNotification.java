package com.example.android.mypokemon.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.android.mypokemon.R;
import com.example.android.mypokemon.json.Pokemon;

import java.util.Random;

public class PokemonNotification {

    private static final String TITLE = "!!MyPokemon is comming!!";
    private static final long[] VIBRATE = new long[]{1000, 1000, 1000};
    private static final Random RANDOM = new Random();
    private static final int RANDOM_MAX = 9999;
    private static final int RANDOM_MIN = 1000;

    private final Context context;
    private NotificationCompat.Builder builder = null;

    public PokemonNotification(Context context, Pokemon pokemon) {
        this.context = context;

        Intent resultIntent = new Intent(Intent.ACTION_VIEW);
        resultIntent.setData(Uri.parse("geo:0,0?q=" + pokemon.getLatitude() + "," + pokemon.getLongitude() + "(MyPokemon)"));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(TITLE)
                .setContentText(pokemon.toString())
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setVibrate(VIBRATE);
    }

    public void notifyPokemon() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(RANDOM.nextInt(RANDOM_MAX - RANDOM_MIN) + RANDOM_MIN, builder.build());
    }
}

package com.burakgomec.wordreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.burakgomec.wordreminder.Model.Database;

public class ReminderBroadcast extends BroadcastReceiver {
    Database database;
    String word1,word2;
    @Override
    public void onReceive(Context context, Intent intent) {
        database = new Database(context);
        database.getWords();
        word1 = database.translatedWordArrayList.get(database.translatedWordArrayList.size()-1).getFirstWord();
        word2 = database.translatedWordArrayList.get(database.translatedWordArrayList.size()-1).getSecondWord();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"notifyChannel")
                .setSmallIcon(R.drawable.ic_baseline_star_rate_24)
                .setSubText("Hatırlatıcı")
                .setContentTitle("İşte son kayıt ettiğiniz kelime")
                .setContentText(word1 + "  =  " + word2)
                .setColor(Color.RED)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat  = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(200,builder.build());

    }
}

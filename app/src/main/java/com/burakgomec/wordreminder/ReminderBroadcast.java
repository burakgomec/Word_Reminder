package com.burakgomec.wordreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.burakgomec.wordreminder.Model.DatabaseController;

import java.util.Random;

public class ReminderBroadcast extends BroadcastReceiver {
    String word1,word2;
    Random random;
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            if(DatabaseController.getInstance().getTranslatedWordArrayList().size() >0){
                random = new Random();
                int randomIndex = random.nextInt(DatabaseController.getInstance().getTranslatedWordArrayList().size());
                word1 = DatabaseController.getInstance().getTranslatedWordArrayList()
                        .get(randomIndex).getFirstWord();
                word2 = DatabaseController.getInstance().getTranslatedWordArrayList()
                        .get(randomIndex).getSecondWord();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"notifyChannel")
                        .setSmallIcon(R.drawable.ic_baseline_star_rate_24)
                        .setContentTitle("İşte son kayıt ettiğiniz kelimelerden birisi ")
                        .setContentText(word1 + "  =  " + word2)
                        .setColor(Color.RED)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManagerCompat  = NotificationManagerCompat.from(context);

                notificationManagerCompat.notify(200,builder.build());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }
}

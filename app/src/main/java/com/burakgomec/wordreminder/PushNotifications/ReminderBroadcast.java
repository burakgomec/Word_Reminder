package com.burakgomec.wordreminder.PushNotifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.burakgomec.wordreminder.Model.Database;
import com.burakgomec.wordreminder.Model.DatabaseController;
import com.burakgomec.wordreminder.R;

import java.util.Random;

public class ReminderBroadcast extends BroadcastReceiver {
    String word1,word2;
    Random random;
    SharedPreferences sharedPreferences;
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            sharedPreferences = context.getSharedPreferences("notification",Context.MODE_PRIVATE);
                Database database = new Database(context);
                database.getWords();
                if(DatabaseController.getInstance().getTranslatedWordArrayList().size() >0){
                    random = new Random();
                    int randomIndex = random.nextInt(DatabaseController.getInstance().getTranslatedWordArrayList().size());
                    word1 = DatabaseController.getInstance().getTranslatedWordArrayList()
                            .get(randomIndex).getFirstWord();
                    word2 = DatabaseController.getInstance().getTranslatedWordArrayList()
                            .get(randomIndex).getSecondWord();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"notifyChannel")
                            .setSmallIcon(R.drawable.ic_baseline_star_rate_24)
                            .setContentTitle(context.getResources().getString(R.string.notificationText))
                            .setContentText(word1 + "  =  " + word2)
                            .setColor(Color.YELLOW)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManagerCompat  = NotificationManagerCompat.from(context);
                    notificationManagerCompat.notify(200,builder.build());
                }
                database.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ReminderBroadcast extends BroadcastReceiver {

    String word1,word2;
    Random random;
    SharedPreferences sharedPreferences;
    NotificationCompat.Builder builder;
    ArrayList<String> pushMessages;

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            random = new Random();
            sharedPreferences = context.getSharedPreferences("notification",Context.MODE_PRIVATE);
                Database database = new Database(context);
                database.getWords();
                if(DatabaseController.getInstance().getTranslatedWordArrayList().size() >0){
                    int randomIndex = random.nextInt(DatabaseController.getInstance().getTranslatedWordArrayList().size());
                    word1 = DatabaseController.getInstance().getTranslatedWordArrayList()
                            .get(randomIndex).getFirstWord();
                    word2 = DatabaseController.getInstance().getTranslatedWordArrayList()
                            .get(randomIndex).getSecondWord();

                    builder = new NotificationCompat.Builder(context,"notifyChannel")
                            .setSmallIcon(R.drawable.ic_baseline_star_rate_24)
                            .setContentTitle(context.getResources().getString(R.string.notificationText))
                            .setContentText(word1 + "  =  " + word2)
                            .setColor(Color.YELLOW)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                }
                else{
                    int counter = sharedPreferences.getInt("counter",0);
                    if(!(counter >= 3)){
                        pushMessages = new ArrayList<>();
                        pushMessages.addAll(Arrays.asList(context.getResources().getStringArray(R.array.pushMessages)));
                        int randomIndex = random.nextInt(pushMessages.size());
                        builder = new NotificationCompat.Builder(context,"notifyChannel")
                                .setSmallIcon(R.drawable.ic_baseline_star_rate_24)
                                .setContentTitle(pushMessages.get(randomIndex))
                                .setColor(Color.YELLOW)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        counter++;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("counter",counter);
                        editor.apply();
                    }

                }
            NotificationManagerCompat notificationManagerCompat  = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(200,builder.build());
            database.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

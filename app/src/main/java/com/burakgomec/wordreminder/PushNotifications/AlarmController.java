package com.burakgomec.wordreminder.PushNotifications;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class AlarmController {

    private static AlarmController alarmController;
    SharedPreferences sharedPreferences;
    String hour;


    public void setAlarm(Context context){
        //Intent intent = new Intent(context, ReminderBroadcast.class);
        //boolean isWorking =(PendingIntent.getBroadcast(context, 0, intent, 0) != null);
         sharedPreferences = context.getSharedPreferences("notification",MODE_PRIVATE);
         hour = sharedPreferences.getString("hour","19");
         createNotificationChannel(context);

         Intent intentNotify = new Intent(context,ReminderBroadcast.class);
         PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intentNotify,0);
         AlarmManager alarmManager  = (AlarmManager)context.getSystemService(ALARM_SERVICE);

         Calendar calendar = Calendar.getInstance();
         calendar.setTimeInMillis(System.currentTimeMillis());
         calendar.set(Calendar.HOUR_OF_DAY, 7);
         calendar.set(Calendar.MINUTE,23);
         alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 20, pendingIntent);

         //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(),2000,pendingIntent);
         //alarmManager.set(AlarmManager.RTC_WAKEUP,15000,pendingIntent);
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);

    }

    public void closeAlarm(Context context){
        Intent myIntent = new Intent(context, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    public void createNotificationChannel(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ // >= API 26
            CharSequence name = "WordReminder";
            NotificationChannel channel = new NotificationChannel("notifyChannel",name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    private AlarmController(){ }

    public static AlarmController getInstance(){
        if(alarmController == null){
            alarmController = new AlarmController();
        }
        return  alarmController;
    }
}

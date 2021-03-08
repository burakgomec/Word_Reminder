package com.burakgomec.wordreminder.PushNotifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class RebootReceiver extends BroadcastReceiver {
    SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            sharedPreferences = context.getSharedPreferences("notification",Context.MODE_PRIVATE);
            if(sharedPreferences.getBoolean("choice",true)){
                AlarmController.getInstance().setAlarm(context);
            }
        }
    }
}



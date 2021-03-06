package com.burakgomec.wordreminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.burakgomec.wordreminder.Model.Database;
import com.burakgomec.wordreminder.Model.DatabaseController;
import com.burakgomec.wordreminder.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.customview.widget.Openable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    AppBarConfiguration appBarConfiguration;
    NavController navController;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setTheme(R.style.Theme_WordReminder);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Connection.getInstance().fillLangModals(this);

         appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_saved, R.id.navigation_settings)
                .build();
         navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        database = new Database(this);
        database.getWords();



        if(DatabaseController.getInstance().getTranslatedWordArrayList().size() != 0){
            createNotificationChannel();
            Intent intent = new Intent(this,ReminderBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 10);
            calendar.set(Calendar.MINUTE, 22);


            alarmManager.set(AlarmManager.RTC_WAKEUP,3000,pendingIntent);
            //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
              //      AlarmManager.INTERVAL_DAY,pendingIntent);
        }
     }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, (Openable) null);
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ // >= API 26
            CharSequence name = "WordReminder";
            NotificationChannel channel = new NotificationChannel("notifyChannel",name,NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }


}

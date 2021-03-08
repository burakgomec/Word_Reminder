package com.burakgomec.wordreminder;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.burakgomec.wordreminder.Model.Database;
import com.burakgomec.wordreminder.PushNotifications.AlarmController;
import com.burakgomec.wordreminder.PushNotifications.RebootReceiver;
import com.burakgomec.wordreminder.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.customview.widget.Openable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    AppBarConfiguration appBarConfiguration;
    NavController navController;
    Database database;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_WordReminder);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Connection.getInstance().fillLangModals(this);

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_search, R.id.navigation_saved, R.id.navigation_settings).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        database = new Database(this);
        database.getWords();

        sharedPreferences = getSharedPreferences("notification",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("onCreateKey",true)){
            AlarmController.getInstance().setAlarm(getApplicationContext());
            ComponentName receiver = new ComponentName(this, RebootReceiver.class);
            PackageManager pm = this.getPackageManager();
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("onCreateKey",false);
            editor.apply();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, (Openable) null);
    }
}

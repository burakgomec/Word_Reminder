package com.burakgomec.wordreminder;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

import kotlin.jvm.internal.Ref;

public class MainActivity extends AppCompatActivity {

    TextView textViewTop;
    BottomNavigationView navView;
    Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTop = findViewById(R.id.textViewTop);
        navView = findViewById(R.id.nav_view);
        bottomNavigationClickListener();
        Connection.getInstance().fillLangModals(this);

     }

    private void bottomNavigationClickListener(){
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_search:
                        textViewTop.setText(R.string.word_reminder);
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.navigation_saved:
                        textViewTop.setText(R.string.saved);
                        selectedFragment = new SavedFragment();
                        break;
                    case R.id.navigation_settings:
                        textViewTop.setText(R.string.settings);
                        selectedFragment = new SettingsFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,selectedFragment).commit();
                return true;
            }
        });
    }
    }

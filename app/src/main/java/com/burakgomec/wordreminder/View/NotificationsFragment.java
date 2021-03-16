package com.burakgomec.wordreminder.View;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.burakgomec.wordreminder.Model.DatabaseController;
import com.burakgomec.wordreminder.PushNotifications.AlarmController;
import com.burakgomec.wordreminder.PushNotifications.RebootReceiver;
import com.burakgomec.wordreminder.R;
import com.burakgomec.wordreminder.databinding.FragmentNotificationsBinding;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;


public class NotificationsFragment extends Fragment {

    FragmentNotificationsBinding binding;
    SharedPreferences sharedPreferences;
    Boolean choice;
    String hour;
    SharedPreferences.Editor editor;
    ComponentName receiver;
    PackageManager pm;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = view.getContext().getSharedPreferences("notification",Context.MODE_PRIVATE);
        permissionControl();
        hour = sharedPreferences.getString("hour","19");
        binding.switchButton.setChecked(choice);
        binding.buttonTimeZone.setText(getString(R.string.changeTimeZone,hour));
        binding.switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchButtonChangeListener(isChecked,getView());
        });
        binding.buttonTimeZone.setOnClickListener(this::timeZoneButtonClickListener);
    }


    private void permissionControl(){
        editor = sharedPreferences.edit();
        if(sharedPreferences.getBoolean("firstCreate",true)){
            choice = DatabaseController.getInstance().userPermissionCheck;
            editor.putBoolean("choice",choice);
            editor.putBoolean("firstCreate",false);
            editor.apply();
        }
        else{
            choice = sharedPreferences.getBoolean("choice",false);
        }
    }

    private void timeZoneButtonClickListener(View v){
        if(!choice){
            Snackbar.make(v, R.string.pleaseChangeYourNotificationSetting, Snackbar.LENGTH_SHORT).show();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(getString(R.string.buttonTimeZoneTitle,hour));
            builder.setItems(R.array.hours, (dialog, which) -> {
                editor.putString("hour",String.valueOf(which));
                hour = String.valueOf(which);
                editor.apply();
                binding.buttonTimeZone.setText(getString(R.string.changeTimeZone,hour));
                Toast.makeText(v.getContext(), R.string.savedTimeZoneSuccessful, Toast.LENGTH_SHORT).show();
                AlarmController.getInstance().closeAlarm(v.getContext());
                AlarmController.getInstance().setAlarm(v.getContext());

            });
            builder.setNegativeButton(R.string.exit, (dialog, which) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void switchButtonChangeListener(Boolean isChecked,View view){
        editor.putBoolean("choice", isChecked);
        choice = isChecked;
        editor.apply();
        if(isChecked){
            AlarmController.getInstance().setAlarm(view.getContext());
            receiver = new ComponentName(view.getContext(), RebootReceiver.class);
            pm = view.getContext().getPackageManager();
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
        else{
            AlarmController.getInstance().closeAlarm(view.getContext());
            receiver = new ComponentName(view.getContext(), RebootReceiver.class);
            pm = view.getContext().getPackageManager();
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }


}
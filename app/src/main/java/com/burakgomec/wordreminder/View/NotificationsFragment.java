package com.burakgomec.wordreminder.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.burakgomec.wordreminder.PushNotifications.AlarmController;
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
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = view.getContext().getSharedPreferences("notification", Context.MODE_PRIVATE);
        choice = sharedPreferences.getBoolean("choice",true);
        hour = sharedPreferences.getString("hour","19");
        binding.switchButton.setChecked(choice);


        binding.switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor = sharedPreferences.edit();
            editor.putBoolean("choice", isChecked);
            choice = isChecked;
            editor.apply();
            if(isChecked){
                AlarmController.getInstance().setAlarm(getContext());
            }
            else{
                AlarmController.getInstance().closeAlarm(getContext());
            }
        });


        binding.buttonTimeZone.setText(getString(R.string.changeTimeZone,hour));

        binding.buttonTimeZone.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(getString(R.string.buttonTimeZoneTitle,hour));
            builder.setItems(R.array.hours, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(!choice){
                        Snackbar.make(v, R.string.pleaseChangeYourNotificationSetting, Snackbar.LENGTH_SHORT).show();
                    }
                    else{
                        editor = sharedPreferences.edit();
                        editor.putString("hour",String.valueOf(which));
                        hour = String.valueOf(which);
                        editor.apply();
                        Toast.makeText(v.getContext(), R.string.savedTimeZoneSuccessful, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
            });
            AlertDialog alert = builder.create();
            alert.show();
        });


    }
}
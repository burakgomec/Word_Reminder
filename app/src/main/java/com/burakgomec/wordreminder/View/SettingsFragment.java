package com.burakgomec.wordreminder.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.burakgomec.wordreminder.Model.Database;
import com.burakgomec.wordreminder.R;
import com.burakgomec.wordreminder.Adapters.SettingsRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsFragment extends Fragment {

    RecyclerView recyclerViewSettings;
    ArrayList<String> settings;
    SettingsRecyclerAdapter settingsRecyclerAdapter;
    Button clearSavedWords;
    Database database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewSettings = view.findViewById(R.id.recyclerViewSettings);
        clearSavedWords = view.findViewById(R.id.buttonClearSavedWords);
        settings = new ArrayList<>(Arrays.asList(view.getResources().getStringArray(R.array.recyclerItems)));
        settingsRecyclerAdapter = new SettingsRecyclerAdapter(view.getContext(),settings);
        recyclerViewSettings.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        recyclerViewSettings.setAdapter(settingsRecyclerAdapter);
        database = new Database(view.getContext());


        clearSavedWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Kayıt edilen çevirileri temizle");
                builder.setMessage("Bu uygulamadaki tüm çeviri kayıtları temizlenecek");
                builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.deleteAllWords();
                        database.close();
                    }
                });
                builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
}
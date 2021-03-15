package com.burakgomec.wordreminder.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.burakgomec.wordreminder.Model.Database;
import com.burakgomec.wordreminder.R;
import com.burakgomec.wordreminder.Adapters.SettingsRecyclerAdapter;
import com.burakgomec.wordreminder.databinding.FragmentSettingsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;
    ArrayList<String> settings;
    SettingsRecyclerAdapter settingsRecyclerAdapter;
    Database database;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settings = new ArrayList<>(Arrays.asList(view.getResources().getStringArray(R.array.recyclerItems)));
        settingsRecyclerAdapter = new SettingsRecyclerAdapter(view.getContext(),settings);
        binding.recyclerViewSettings.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        binding.recyclerViewSettings.setAdapter(settingsRecyclerAdapter);
        database = new Database(view.getContext());

        binding.buttonClearSavedWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSavedWords(v);
            }
        });
    }



    private void clearSavedWords(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.clearSavedTranslations);
        builder.setMessage(R.string.clearSavedTranslationsMessage);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.deleteAllWords();
                database.close();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
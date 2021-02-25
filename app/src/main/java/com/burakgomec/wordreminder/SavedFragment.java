package com.burakgomec.wordreminder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SavedFragment extends Fragment {

    Database database;
    ArrayList<TranslatedWord> translatedWords;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = new Database(view.getContext());
        recyclerView = view.findViewById(R.id.recyclerViewSaved);
        translatedWords = new ArrayList<>();
        translatedWords = database.getWords();
        WordsRecyclerAdapter wordsRecyclerAdapter = new WordsRecyclerAdapter(view.getContext(),translatedWords,database);
        recyclerView.setAdapter(wordsRecyclerAdapter);

    }

    @Override
    public void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
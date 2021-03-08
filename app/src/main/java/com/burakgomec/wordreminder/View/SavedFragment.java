package com.burakgomec.wordreminder.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.burakgomec.wordreminder.Model.Database;
import com.burakgomec.wordreminder.Adapters.WordsRecyclerAdapter;
import com.burakgomec.wordreminder.Model.DatabaseController;
import com.burakgomec.wordreminder.databinding.FragmentSavedBinding;

import org.jetbrains.annotations.NotNull;

public class SavedFragment extends Fragment {

    FragmentSavedBinding binding;
    Database database;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSavedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = new Database(view.getContext());
        if(DatabaseController.getInstance().getTranslatedWordArrayList().size() > 0){
            binding.textViewNullList.setVisibility(View.INVISIBLE);
            WordsRecyclerAdapter wordsRecyclerAdapter = new WordsRecyclerAdapter(view.getContext(),database,binding);
            binding.recyclerViewSaved.setVisibility(View.VISIBLE);
            binding.recyclerViewSaved.setAdapter(wordsRecyclerAdapter);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
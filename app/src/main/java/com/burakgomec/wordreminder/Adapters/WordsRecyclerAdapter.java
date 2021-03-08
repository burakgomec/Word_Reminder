package com.burakgomec.wordreminder.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.burakgomec.wordreminder.Model.DatabaseController;
import com.burakgomec.wordreminder.Model.Database;
import com.burakgomec.wordreminder.R;
import com.burakgomec.wordreminder.Model.TranslatedWord;
import com.burakgomec.wordreminder.databinding.FragmentSavedBinding;

import java.util.ArrayList;

public class WordsRecyclerAdapter extends RecyclerView.Adapter<WordsRecyclerAdapter.ViewHolder> {


    Context context;
    ArrayList<TranslatedWord> translatedWordArrayList;
    Database database;
    FragmentSavedBinding binding;

    public WordsRecyclerAdapter(Context context, Database database, FragmentSavedBinding binding){
        this.context = context;
        this.database = database;
        this.translatedWordArrayList = DatabaseController.getInstance().getTranslatedWordArrayList();
        this.binding = binding;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_row_words,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.word1.setText(translatedWordArrayList.get(position).getFirstWord());
        holder.word2.setText(translatedWordArrayList.get(position).getSecondWord());

        holder.imageViewUnStar.setOnClickListener(v -> {
            database.deleteWordWithId(translatedWordArrayList.get(position));
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount() - position);
            if(translatedWordArrayList.size() == 0){
                binding.recyclerViewSaved.setVisibility(View.INVISIBLE);
                binding.textViewNullList.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public int getItemCount() {
        return translatedWordArrayList.size();
    }


    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView word1,word2;
        ImageView imageViewUnStar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            word1 = itemView.findViewById(R.id.textViewSavedWord1);
            word2 = itemView.findViewById(R.id.textViewSavedWord2);
            imageViewUnStar= itemView.findViewById(R.id.imageViewUnStar);
        }
    }
}

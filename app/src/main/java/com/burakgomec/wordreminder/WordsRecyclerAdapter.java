package com.burakgomec.wordreminder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class WordsRecyclerAdapter extends RecyclerView.Adapter<WordsRecyclerAdapter.ViewHolder> {


    Context context;
    ArrayList<TranslatedWord> translatedWordArrayList;
    Database database;

    public WordsRecyclerAdapter(Context context,ArrayList<TranslatedWord> translatedWords,Database database){
        this.context = context;
        this.translatedWordArrayList = translatedWords;
        this.database = database;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_row_words,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.word1.setText(translatedWordArrayList.get(position).getFirstWord()+"ID"+translatedWordArrayList.get(position).getId());
        holder.word2.setText(translatedWordArrayList.get(position).getSecondWord());
        holder.imageViewUnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return translatedWordArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
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

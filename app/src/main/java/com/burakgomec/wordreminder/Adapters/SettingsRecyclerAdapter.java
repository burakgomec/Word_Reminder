package com.burakgomec.wordreminder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.burakgomec.wordreminder.R;
import com.burakgomec.wordreminder.View.SettingsFragmentDirections;


import java.util.ArrayList;

public class SettingsRecyclerAdapter extends RecyclerView.Adapter<SettingsRecyclerAdapter.ViewHolder> {

    ArrayList<String> settings;
    Context context;
    NavDirections navDirections;
    String appPackageName;


    public SettingsRecyclerAdapter(Context context,ArrayList<String> settings){
        this.context = context;
        this.settings = settings;
        appPackageName =  context.getPackageName();
    }

    @NonNull
    @Override
    public SettingsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reycler_row_settings,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsRecyclerAdapter.ViewHolder holder, int position) {
        holder.textViewSetting.setText(settings.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        navDirections = SettingsFragmentDirections.actionNavigationSettingsToAboutFragment2();
                        Navigation.findNavController(holder.itemView).navigate(navDirections);
                        break;

                    case 1:
                    //
                        break;
                    case 2:
                        try {
                           context.startActivity((new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName))));
                        } catch (android.content.ActivityNotFoundException error) {
                            context.startActivity((new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName))));
                        }
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return settings.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSetting;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSetting = itemView.findViewById(R.id.textViewSetting);
        }
    }
}

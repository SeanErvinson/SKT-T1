package com.sktt1.butters.data.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sktt1.butters.R;
import com.sktt1.butters.data.models.User;

import java.util.ArrayList;

public class SettingsTagRecylcerAdapter extends RecyclerView.Adapter<SettingsTagRecylcerAdapter.SettingsTagViewHolder> {

    private final ArrayList<User> users;
    private final OnSettingsTagListener onSettingsTagListener;
    public SettingsTagRecylcerAdapter(ArrayList<User> users, OnSettingsTagListener onSettingsTagListener){
        this.users = users;
        this.onSettingsTagListener = onSettingsTagListener;
    }

    @NonNull
    @Override
    public SettingsTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_settings_tag, parent, false);
        return new SettingsTagRecylcerAdapter.SettingsTagViewHolder(view, onSettingsTagListener);
    }

    static class SettingsTagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnSettingsTagListener onSettingsTagListener;

        public SettingsTagViewHolder(@NonNull View itemView, OnSettingsTagListener onSettingsTagListener){
            super(itemView);

            this.onSettingsTagListener = onSettingsTagListener;

        }

        @Override
        public void onClick(View view) {
            onSettingsTagListener.onClick(getAdapterPosition());
        }
    }

    public interface OnSettingsTagListener {
        void onClick(int index);
    }
}

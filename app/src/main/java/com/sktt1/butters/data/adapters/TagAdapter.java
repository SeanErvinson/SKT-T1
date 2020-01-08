package com.sktt1.butters.data.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sktt1.butters.R;
import com.sktt1.butters.data.models.Location;
import com.sktt1.butters.data.models.Tag;
import com.sktt1.butters.data.utilities.DateTimePattern;
import com.sktt1.butters.data.utilities.DateUtility;

import java.util.ArrayList;
import java.util.Date;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {

    private ArrayList<Tag> tags;

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_cell, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {

        holder.bindTagName(tags.get(position).getName());

        Location lastSeenLocation = tags.get(position).getLastSeenLocation();
        String tagLocation = holder.itemView.getResources().getString(R.string.last_seen_location, lastSeenLocation.getName());
        holder.bindTagLocation(tagLocation);

        Date lastSeenTime = tags.get(position).getLastSeenTime();
        String formattedDate = DateUtility.getFormattedDate(lastSeenTime, DateTimePattern.TIME);
        String tagTime = holder.itemView.getResources().getString(R.string.last_seen_time, formattedDate);
        holder.bindTagTime(tagTime);
    }

    @Override
    public int getItemCount() {
        return tags != null ? tags.size() : 0;
    }

    static class TagViewHolder extends RecyclerView.ViewHolder {

        private TextView tagName;
        private TextView tagLocation;
        private TextView tagTime;
        private TextView tagLocate;

        TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tv_key_cell_name);
            tagLocation = itemView.findViewById(R.id.tv_key_cell_last_location);
            tagTime = itemView.findViewById(R.id.tv_key_cell_time);
            tagLocate = itemView.findViewById(R.id.tv_key_cell_locate);
        }

        void bindTagName(String content) {
            tagName.setText(content);
        }

        void bindTagLocation(String content) {
            tagLocation.setText(content);
        }

        void bindTagTime(String content) {
            tagTime.setText(content);
        }
    }

    public void setTagsData(ArrayList<Tag> tags) {
        this.tags = tags;
        notifyDataSetChanged();
    }
}

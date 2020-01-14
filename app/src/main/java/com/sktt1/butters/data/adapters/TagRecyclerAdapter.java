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

public class TagRecyclerAdapter extends RecyclerView.Adapter<TagRecyclerAdapter.TagViewHolder> {

    private ArrayList<Tag> tags;
    private OnTagListener onTagListener;

    public TagRecyclerAdapter(ArrayList<Tag> tags, OnTagListener onTagListener) {
        this.tags = tags;
        this.onTagListener = onTagListener;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_tag, parent, false);
        return new TagViewHolder(view, onTagListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {

        holder.bindTagName(tags.get(position).getName());

        Location lastSeenLocation = tags.get(position).getLastSeenLocation();
        if(lastSeenLocation != null){
            String tagLocation = holder.itemView.getResources().getString(R.string.last_seen_location, lastSeenLocation.getName());
            holder.bindTagLocation(tagLocation);
        }
        if(lastSeenLocation != null){
            Date lastSeenTime = tags.get(position).getLastSeenTime();
            String formattedDate = DateUtility.getFormattedDate(lastSeenTime, DateTimePattern.TIME);
            String tagTime = holder.itemView.getResources().getString(R.string.last_seen_time, formattedDate);
            holder.bindTagTime(tagTime);
        }
    }

    @Override
    public int getItemCount() {
        return tags != null ? tags.size() : 0;
    }

    static class TagViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        private TextView tagName;
        private TextView tagLocation;
        private TextView tagTime;
        private TextView tagLocate;
        private OnTagListener onTagListener;

        TagViewHolder(@NonNull View itemView, OnTagListener onTagListener) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tv_key_cell_name);
            tagLocation = itemView.findViewById(R.id.tv_key_cell_last_location);
            tagTime = itemView.findViewById(R.id.tv_key_cell_time);
            tagLocate = itemView.findViewById(R.id.tv_key_cell_locate);

            this.onTagListener = onTagListener;
            tagLocate.setOnClickListener(this);
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

        @Override
        public void onClick(View view) {
            onTagListener.onClick(getAdapterPosition());
        }
    }

    public interface OnTagListener{
        void onClick(int index);
    }
}

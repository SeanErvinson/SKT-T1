package com.sktt1.butters.data.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sktt1.butters.R;
import com.sktt1.butters.data.database.DatabaseHelper;
import com.sktt1.butters.data.models.Location;
import com.sktt1.butters.data.models.Tag;
import com.sktt1.butters.data.utilities.DateTimePattern;
import com.sktt1.butters.data.utilities.DateUtility;

import java.util.ArrayList;
import java.util.Date;

public class TagRecyclerAdapter extends RecyclerView.Adapter<TagRecyclerAdapter.TagViewHolder> {

    private ArrayList<Tag> mTags;
    private OnTagListener onTagListener;

    public TagRecyclerAdapter(OnTagListener onTagListener) {
        this.mTags = new ArrayList<>();
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

        holder.bindTagName(mTags.get(position).getName());

        DatabaseHelper databaseHelper = new DatabaseHelper(holder.tagName.getContext());
        Location lastSeenLocation = databaseHelper.getLocationById(mTags.get(position).getLastSeenLocationId());
        if(lastSeenLocation != null){
            String tagLocation = holder.itemView.getResources().getString(R.string.last_seen_location, lastSeenLocation.getName());
            holder.bindTagLocation(tagLocation);
        }
        if(lastSeenLocation != null){
            Date lastSeenTime = mTags.get(position).getLastSeenTime();
            if(lastSeenTime != null){
                String formattedDate = DateUtility.getFormattedDate(lastSeenTime, DateTimePattern.TIME);
                String tagTime = holder.itemView.getResources().getString(R.string.last_seen_time, formattedDate);
                holder.bindTagTime(tagTime);
            }
        }
        if(lastSeenLocation != null){
            holder.bindTagLocate(mTags.get(position).isConnected());
        }
    }

    public void loadTags(ArrayList<Tag> tags){
        mTags = tags;
    }

    @Override
    public int getItemCount() {
        return mTags != null ? mTags.size() : 0;
    }

    public Tag getTag(int position) {
        return mTags.get(position);
    }

    public void clear() {
        mTags.clear();
    }

    public void addTag(Tag tag) {
        if(!mTags.contains(tag)) {
            if(tag.getName() == null) return;
            mTags.add(tag);
            notifyDataSetChanged();
        }
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

        void bindTagLocate(boolean isConnected){
            if (!isConnected){
                tagLocate.setEnabled(false);
            } else {
                tagLocate.setEnabled(true);
            }
        }

        void bindTagLocation(String content) {
            if(content.length() <= 0){
                tagLocation.setVisibility(View.GONE);
            } else {
                tagLocation.setText(content);
            }
        }

        void bindTagTime(String content) {
            if(content.length() <= 0) {
                tagTime.setVisibility(View.GONE);
            } else {
                tagTime.setText(content);
            }
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

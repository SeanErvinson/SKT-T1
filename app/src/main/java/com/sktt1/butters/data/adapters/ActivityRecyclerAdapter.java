package com.sktt1.butters.data.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sktt1.butters.R;
import com.sktt1.butters.data.models.Activity;
import com.sktt1.butters.data.utilities.DateTimePattern;
import com.sktt1.butters.data.utilities.DateUtility;

import java.util.ArrayList;

public class ActivityRecyclerAdapter extends RecyclerView.Adapter<ActivityRecyclerAdapter.ActivityViewHolder>{

    private final ArrayList<Activity> activities;
    private final OnActivityListener onActivityListener;

    public ActivityRecyclerAdapter(ArrayList<Activity> activities, OnActivityListener onActivityListener) {
        this.activities = activities;
        this.onActivityListener = onActivityListener;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_activity, parent, false);
        return new ActivityRecyclerAdapter.ActivityViewHolder(view, onActivityListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        holder.bindMessage(activities.get(position).getMessage());

        String formattedDate = DateUtility.getFormattedDate(activities.get(position).getNotifiedOn(), DateTimePattern.DATE);
        String formattedTime= DateUtility.getFormattedDate(activities.get(position).getNotifiedOn(), DateTimePattern.TIME);
        holder.bindDateTimeInfo(String.format("%s at %s", formattedDate, formattedTime));
    }

    @Override
    public int getItemCount() {
        return activities != null ? activities.size() : 0;
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        private TextView message;
        private TextView dateTimeInfo;
        private OnActivityListener onActivityListener;

        public ActivityViewHolder(@NonNull View itemView, OnActivityListener onActivityListener) {
            super(itemView);
            message = itemView.findViewById(R.id.tv_activity_cell_message);
            dateTimeInfo = itemView.findViewById(R.id.tv_activity_cell_date_time_info);
            this.onActivityListener = onActivityListener;
        }

        public void bindMessage(String content){message.setText(content);}
        public void bindDateTimeInfo(String content){dateTimeInfo.setText(content);}

        @Override
        public void onClick(View view) {
            onActivityListener.onClick(getAdapterPosition());
        }
    }

    public interface OnActivityListener {
        void onClick(int index);
    }
}

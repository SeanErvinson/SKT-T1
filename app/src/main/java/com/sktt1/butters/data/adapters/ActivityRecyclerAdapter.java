package com.sktt1.butters.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sktt1.butters.R;
import com.sktt1.butters.data.database.DatabaseHelper;
import com.sktt1.butters.data.models.Activity;
import com.sktt1.butters.data.utilities.DateTimePattern;
import com.sktt1.butters.data.utilities.DateUtility;

import java.util.ArrayList;
import java.util.Date;

public class ActivityRecyclerAdapter extends RecyclerView.Adapter<ActivityRecyclerAdapter.ActivityViewHolder> {

    private ArrayList<Activity> mActivities;
    private final OnActivityListener onActivityListener;
    private Context context;

    public ActivityRecyclerAdapter(Context context, OnActivityListener onActivityListener) {
        this.mActivities = new ArrayList<>();
        this.onActivityListener = onActivityListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_activity, parent, false);
        return new ActivityRecyclerAdapter.ActivityViewHolder(view, onActivityListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        if (mActivities.get(position).getNotifiedOn() == null) return;
        Date notifiedOn = mActivities.get(position).getNotifiedOn();
        String formattedDate = DateUtility.getFormattedDate(notifiedOn, DateTimePattern.DATE);
        String formattedTime = DateUtility.getFormattedDate(notifiedOn, DateTimePattern.TIME);
        holder.bindMessage(mActivities.get(position).getMessage());
        holder.bindDateTimeInfo(context.getString(R.string.activity_date_time_info, formattedDate, formattedTime));
    }

    public void loadActivities(ArrayList<Activity> activities) {
        mActivities = activities;
    }

    @Override
    public int getItemCount() {
        return mActivities != null ? mActivities.size() : 0;
    }

    public Activity getActivity(int position) {
        return mActivities.get(position);
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView message;
        private TextView dateTimeInfo;
        private OnActivityListener onActivityListener;
        private ImageButton buttonDelete;

        ActivityViewHolder(@NonNull View itemView, final OnActivityListener onActivityListener) {
            super(itemView);
            message = itemView.findViewById(R.id.tv_activity_cell_message);
            dateTimeInfo = itemView.findViewById(R.id.tv_activity_cell_date_time_info);
            buttonDelete = itemView.findViewById(R.id.btn_activity_cell_delete);
            this.onActivityListener = onActivityListener;

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onActivityListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Context context = buttonDelete.getContext();
                            onActivityListener.onDelete(position);
                        }
                    }
                }
            });
        }

        void bindMessage(String content) {
            message.setText(content);
        }

        void bindDateTimeInfo(String content) {
            dateTimeInfo.setText(content);
        }

        @Override
        public void onClick(View view) {
            onActivityListener.onClick(getAdapterPosition());
        }

    }

    public interface OnActivityListener {
        void onClick(int index);
        void onDelete(int index);
    }
}

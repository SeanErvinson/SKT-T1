package com.sktt1.butters.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sktt1.butters.R;
import com.sktt1.butters.data.models.Tag;

import java.util.ArrayList;

public class SettingsTagAdapter extends ArrayAdapter<Tag> implements View.OnClickListener {

    private ArrayList<Tag> mTags;
    Context mContext;
    private int lastPostion = -1;

    private static class ViewHolder {
        TextView tvTagName;
        TextView tvTagConnectivityStatus;
        TextView tvTagSoundAlarm;
    }

    public SettingsTagAdapter(Context context, ArrayList<Tag> tags){
        super(context, R.layout.list_cell_settings_tag, tags);

        this.mTags = tags;
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Tag tag = getItem(position);

        ViewHolder viewHolder;

        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_cell_settings_tag, parent, false);
            viewHolder.tvTagName = convertView.findViewById(R.id.tv_settingsTag_cell_name);
            viewHolder.tvTagConnectivityStatus = convertView.findViewById(R.id.tv_settingsTag_cell_isConnected);
            viewHolder.tvTagSoundAlarm = convertView.findViewById(R.id.tv_settingsTag_cell_soundAlarm);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPostion = position;

        viewHolder.tvTagName.setText(tag.getName());
        viewHolder.tvTagConnectivityStatus.setText(Boolean.toString(tag.isConnected()));
        viewHolder.tvTagSoundAlarm.setText("hit the quan by wayne madla");
        return convertView;
    }
    @Override
    public void onClick(View view){
    }


}

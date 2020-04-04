package com.sktt1.butters.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sktt1.butters.R;
import com.sktt1.butters.data.models.Ringtone;

import java.util.ArrayList;

public class FindMyPhoneAlarmAdapter extends ArrayAdapter<Ringtone> {

    private ArrayList<Ringtone> ringtones;
    private Context mContext;
    private int lastPosition = -1;


    private static class ViewHolder {
        TextView txtTitle;
        ImageView imgCheck;
        LinearLayout llRingtoneCell;
    }

    public FindMyPhoneAlarmAdapter(@NonNull Context context, ArrayList<Ringtone> ringtones) {
        super(context, R.layout.list_cell_ringtone, ringtones);
        this.mContext = context;
        this.ringtones = ringtones;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ringtone ringtone = getItem(position);
        final ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_cell_ringtone, parent, false);
            viewHolder.txtTitle = convertView.findViewById(R.id.tv_ringtone_cell_name);
            viewHolder.imgCheck = convertView.findViewById(R.id.iv_ringtone_cell_check);
            viewHolder.llRingtoneCell = convertView.findViewById(R.id.ll_ringtone_cell);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.txtTitle.setText(ringtone.getRingtoneTitle());
        viewHolder.llRingtoneCell.setTag(position);
        return convertView;
    }
}

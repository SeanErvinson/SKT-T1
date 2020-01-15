package com.sktt1.butters.data.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sktt1.butters.R;

import java.util.ArrayList;

public class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDevice> {

    public BluetoothDeviceAdapter(@NonNull Context context, @NonNull ArrayList<BluetoothDevice> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BluetoothDevice device = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_bluetooth_device, parent, false);
        }
        TextView bluetoothName = convertView.findViewById(R.id.tv_bluetooth_device_cell_name);
        bluetoothName.setText(device.getName());
        return convertView;
    }
}

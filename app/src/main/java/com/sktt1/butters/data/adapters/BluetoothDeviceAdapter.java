package com.sktt1.butters.data.adapters;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sktt1.butters.R;

import java.util.ArrayList;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.BluetoothDeviceViewHolder>{

    private ArrayList<BluetoothDevice> mLeDevices;
    private final OnDeviceListener onDeviceListener;

    public BluetoothDeviceAdapter(OnDeviceListener onDeviceListener) {
        this.mLeDevices = new ArrayList<>();
        this.onDeviceListener = onDeviceListener;
    }

    @NonNull
    @Override
    public BluetoothDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_bluetooth_device, parent, false);
        return new BluetoothDeviceViewHolder(view, onDeviceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothDeviceViewHolder holder, int position) {
        holder.bindName(mLeDevices.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mLeDevices != null ? mLeDevices.size() : 0;
    }

    public void addDevice(BluetoothDevice device) {
        if(!mLeDevices.contains(device)) {
            if(device.getName() == null) return;
            mLeDevices.add(device);
        }
    }

    public BluetoothDevice getDevice(int position) {
        return mLeDevices.get(position);
    }

    public void clear() {
        mLeDevices.clear();
    }

    static class BluetoothDeviceViewHolder  extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private TextView name;
        private OnDeviceListener onDeviceListener;
        private LinearLayout bluetoothDeviceCell;


        public BluetoothDeviceViewHolder(@NonNull View itemView, OnDeviceListener onDeviceListener) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_bluetooth_device_cell_name);
            bluetoothDeviceCell = itemView.findViewById(R.id.ll_bluetooth_device);
            this.onDeviceListener = onDeviceListener;
            bluetoothDeviceCell.setOnClickListener(this);
        }

        public void bindName(String content){name.setText(content);}

        @Override
        public void onClick(View view) {
            onDeviceListener.onClick(getAdapterPosition());
        }
    }

    public interface OnDeviceListener{
        void onClick(int index);
    }
}

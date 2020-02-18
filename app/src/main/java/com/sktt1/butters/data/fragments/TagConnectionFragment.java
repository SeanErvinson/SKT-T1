package com.sktt1.butters.data.fragments;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sktt1.butters.AddTagActivity;
import com.sktt1.butters.R;
import com.sktt1.butters.data.adapters.BluetoothDeviceAdapter;

public class TagConnectionFragment extends Fragment implements BluetoothDeviceAdapter.OnDeviceListener {
    private static final String TAG = TagConnectionFragment.class.getSimpleName();

    private final static int BLUETOOTH_ENABLE_REQUEST = 1001;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 24000;

    private RecyclerView mScannedDevicesList;
    private FragmentListener mListener;
    private BluetoothDeviceAdapter mBluetoothDeviceAdapter;

    public TagConnectionFragment() {
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHandler = new Handler();
        initializeWidget(view);
        initializeRecyclerView();
        initializeBluetooth();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tag_connection, container, false);
    }

    private void initializeWidget(View view) {
        mScannedDevicesList = view.findViewById(R.id.rv_pair_tag_scanned_devices);
    }

    private void initializeRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBluetoothDeviceAdapter = new BluetoothDeviceAdapter(this);
        mScannedDevicesList.setLayoutManager(linearLayoutManager);
        mScannedDevicesList.setAdapter(mBluetoothDeviceAdapter);
        mScannedDevicesList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void initializeBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getContext(), getString(R.string.bluetooth_not_supported_message), Toast.LENGTH_LONG).show();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, BLUETOOTH_ENABLE_REQUEST);
        }
        scanLeDevice(true);
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
        new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                mBluetoothDeviceAdapter.addDevice(device);
                mBluetoothDeviceAdapter.notifyDataSetChanged();
            }
        };

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case BLUETOOTH_ENABLE_REQUEST:
                if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getContext(), "Enable bluetooth", Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        scanLeDevice(false);
        mBluetoothDeviceAdapter.clear();
    }

    @Override
    public void onClick(int index) {
        BluetoothDevice device = mBluetoothDeviceAdapter.getDevice(index);
        mBluetoothDeviceAdapter.selectedPosition = index;
        AddTagActivity tagActivity = (AddTagActivity) getActivity();
        if(device == null) return;
        if(device.getName().equals("SKT-T1")){
            mListener.onSelectedDevice(device);
            tagActivity.hideNavButton(false);
        }else{
            Toast.makeText(getContext(), "Not a compatible device.", Toast.LENGTH_SHORT).show();
            tagActivity.hideNavButton(true);
        }
    }

    public interface FragmentListener {
        void onSelectedDevice(BluetoothDevice selectedDevice);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            mListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

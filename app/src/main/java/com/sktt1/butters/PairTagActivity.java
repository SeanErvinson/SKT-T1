package com.sktt1.butters;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sktt1.butters.data.adapters.BluetoothDeviceAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

// TODO: Create a helper class to get the currently connected devices. Filter the recognized names and return.
public class PairTagActivity extends AppCompatActivity {
    private final static String TAG = "PairTagActivity";
    private final static int BLUETOOTH_ENABLE_REQUEST = 1001;

    private TextView mBluetoothStatus;
    private ListView mScannedDevicesList;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDeviceAdapter scannedDeviceAdapter;

    private ArrayList<BluetoothDevice> scannedBluetoothDevices = new ArrayList<>();

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG, "Device: " + device.getName() + " Bond " + device.getBondState());
                if (!scannedBluetoothDevices.contains(device)) {
                    scannedBluetoothDevices.add(device);
                    scannedDeviceAdapter.notifyDataSetChanged();
                }
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (scannedBluetoothDevices.size() == 0) {
                    mBluetoothStatus.setText(getString(R.string.bluetooth_finish_empty_message));
                }
                mBluetoothStatus.setText(getString(R.string.bluetooth_finish_message));
                bluetoothAdapter.cancelDiscovery();
                unregisterReceiver(receiver);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_tag);
        initializeWidget();
        initializeReceivers();
        initializeBluetooth();
    }

    private void initializeWidget() {
        mBluetoothStatus = findViewById(R.id.tv_pair_tag_bluetooth_status);
        mScannedDevicesList = findViewById(R.id.lv_pair_tag_scanned_devices);
        scannedDeviceAdapter = new BluetoothDeviceAdapter(this, scannedBluetoothDevices);
        mScannedDevicesList.setAdapter(scannedDeviceAdapter);
    }

    private void initializeBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, getString(R.string.bluetooth_not_supported_message), Toast.LENGTH_LONG).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, BLUETOOTH_ENABLE_REQUEST);
        }
        scanBluetooth();
    }

    private void scanBluetooth() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }

    private void initializeReceivers() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case BLUETOOTH_ENABLE_REQUEST:
                if (resultCode == RESULT_OK) {
                    mBluetoothStatus.setText(getString(R.string.bluetooth_scan_message));
                } else {
                    mBluetoothStatus.setText(getString(R.string.bluetooth_off_message));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}

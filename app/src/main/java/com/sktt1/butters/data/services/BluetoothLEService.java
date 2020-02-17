package com.sktt1.butters.data.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.sktt1.butters.data.receivers.TagBroadcastReceiver;

import java.util.UUID;

public class BluetoothLEService extends Service {
    private final static String TAG = BluetoothLEService.class.getSimpleName();

    public final static UUID UUID_LONG_GPS = UUID.fromString("0000DD00-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_LAT_GPS = UUID.fromString("0000DD02-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_FMP = UUID.fromString("0000FF01-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_ALERT = UUID.fromString("0000AA00-0000-1000-8000-00805f9b34fb");

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;

    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            return false;
        }

        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public BluetoothLEService getService() {
            return BluetoothLEService.this;
        }
    }


    private int connectionState = BluetoothProfile.STATE_DISCONNECTED;
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (BluetoothProfile.STATE_CONNECTED == newState) {
                intentAction = TagBroadcastReceiver.ACTION_GATT_CONNECTED;
                connectionState = BluetoothProfile.STATE_CONNECTED;
                BluetoothDevice device = gatt.getDevice();
                broadcastUpdate(intentAction, device);
            }
            if (BluetoothProfile.STATE_DISCONNECTED == newState) {
                intentAction = TagBroadcastReceiver.ACTION_GATT_DISCONNECTED;
                connectionState = BluetoothProfile.STATE_DISCONNECTED;
                BluetoothDevice device = gatt.getDevice();
                broadcastUpdate(intentAction, device);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(TagBroadcastReceiver.ACTION_GATT_SERVICES_DISCOVERED);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(TagBroadcastReceiver.ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(TagBroadcastReceiver.ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    public BluetoothGatt connect(String macAddress) {
        if (mBluetoothAdapter == null || macAddress == null) {
            return null;
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(macAddress);
        if (device == null) {
            return null;
        }
        return device.connectGatt(this, false, mGattCallback);
    }

    private void broadcastUpdate(String intentAction) {
        Intent intent = new Intent(intentAction);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(String intentAction, BluetoothDevice selectedDevice) {
        Intent intent = new Intent(intentAction);
        intent.putExtra(TagBroadcastReceiver.EXTRA_DATA, selectedDevice);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(String intentAction, BluetoothGattCharacteristic characteristic) {
        Intent intent = new Intent(intentAction);
        if (UUID_LAT_GPS.equals(characteristic.getUuid())) {
            final float latitude = parseFloatCharacteristic(characteristic);
            intent.putExtra(TagBroadcastReceiver.GPS_LAT_DATA, latitude);
        } else if (UUID_LONG_GPS.equals(characteristic.getUuid())) {
            final float latitude = parseFloatCharacteristic(characteristic);
            intent.putExtra(TagBroadcastReceiver.GPS_LNG_DATA, latitude);
        } else if (UUID_FMP.equals(characteristic.getUuid())) {
            final int fmp = parseIntCharacteristic(characteristic);
            intent.putExtra(TagBroadcastReceiver.ACTION_PHONE_ALERTED, fmp);
        }
        sendBroadcast(intent);
    }

    private float parseFloatCharacteristic(BluetoothGattCharacteristic characteristic) {
        int flag = characteristic.getProperties();
        int format = -1;
        if ((flag & 0x01) != 0) {
            format = BluetoothGattCharacteristic.FORMAT_FLOAT;
        }
        return characteristic.getFloatValue(format, 1);
    }

    private int parseIntCharacteristic(BluetoothGattCharacteristic characteristic) {
        int flag = characteristic.getProperties();
        int format;
        if ((flag & 0x01) != 0) {
            format = BluetoothGattCharacteristic.FORMAT_UINT16;
        } else {
            format = BluetoothGattCharacteristic.FORMAT_UINT8;
        }
        return characteristic.getIntValue(format, 1);
    }
}

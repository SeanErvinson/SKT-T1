package com.sktt1.butters.data.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.sktt1.butters.data.receivers.TagBroadcastReceiver;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.UUID;

public class BluetoothLEService extends Service {
    private final static String TAG = BluetoothLEService.class.getSimpleName();

    public final static UUID UUID_GPS_SERVICE = UUID.fromString("0000DD00-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_LAT_CHAR = UUID.fromString("0000DD01-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_LNG_CHAR = UUID.fromString("0000DD02-0000-1000-8000-00805f9b34fb");

    public final static UUID UUID_FMP_SERVICE = UUID.fromString("0000FF00-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_FMP_CHAR = UUID.fromString("0000FF01-0000-1000-8000-00805f9b34fb");

    public final static UUID UUID_ALERT_SERVICE = UUID.fromString("0000AA00-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_ALERT_CHAR = UUID.fromString("0000AA01-0000-1000-8000-00805f9b34fb");

    public final static UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private ArrayList<BluetoothGatt> mBluetoothGatts = new ArrayList<>();
    private HashMap<String, Queue<BluetoothGattCharacteristic>> gattChars = new HashMap<>();

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
                BluetoothGatt bluetoothGatt = getBluetoothGatt(device.getAddress());
                bluetoothGatt.discoverServices();
            }
            if (BluetoothProfile.STATE_DISCONNECTED == newState) {
                intentAction = TagBroadcastReceiver.ACTION_GATT_DISCONNECTED;
                connectionState = BluetoothProfile.STATE_DISCONNECTED;
                BluetoothDevice device = gatt.getDevice();
                broadcastUpdate(intentAction, device);
            }
        }

        public void requestCharacteristics(BluetoothGatt gatt) {
            gatt.readCharacteristic(gattChars.get(gatt.getDevice().getAddress()).poll());
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
            if(gattChars.get(gatt.getDevice().getAddress()).size() > 0){
                requestCharacteristics(gatt);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(TagBroadcastReceiver.ACTION_DATA_AVAILABLE, characteristic);
        }
    };
    public BluetoothGatt getBluetoothGatt(String macAddress) {
        for (BluetoothGatt bluetoothGatt : mBluetoothGatts) {
            if (bluetoothGatt.getDevice().getAddress().equals(macAddress))
                return bluetoothGatt;
        }
        return null;
    }

    public void removeBluetoothGatt(String macAddress) {
        for (int i = 0; i < mBluetoothGatts.size(); i++) {
            if (mBluetoothGatts.get(i).getDevice().getAddress().equals(macAddress)) {
                mBluetoothGatts.remove(i);
            }
        }
    }

    public void disconnect(String macAddress) {
        if (mBluetoothAdapter == null || mBluetoothGatts == null) return;
        BluetoothGatt bluetoothGatt = getBluetoothGatt(macAddress);
        if (bluetoothGatt != null) bluetoothGatt.disconnect();
    }

    public boolean connect(String macAddress) {
        if (mBluetoothAdapter == null || macAddress == null) {
            return false;
        }

        BluetoothGatt bluetoothGatt = getBluetoothGatt(macAddress);
        if (bluetoothGatt != null) {
            if (bluetoothGatt.connect()) {
                return true;
            }
            return false;
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(macAddress);
        if (device == null) {
            return false;
        }

        bluetoothGatt = device.connectGatt(this, false, mGattCallback);
        if (bluetoothGatt == null) {
            return false;
        }
        mBluetoothGatts.add(bluetoothGatt);
        return true;
    }

    public void executeReadCharacteristics(String macAddress, Queue<BluetoothGattCharacteristic> characteristics){
        gattChars.put(macAddress, characteristics);
        getBluetoothGatt(macAddress).readCharacteristic(characteristics.poll());
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
        if (UUID_LAT_CHAR.equals(characteristic.getUuid())) {
            final float latitude = parseFloatCharacteristic(characteristic);
            intent.putExtra(TagBroadcastReceiver.GPS_LAT_DATA, latitude);
        } else if (UUID_LNG_CHAR.equals(characteristic.getUuid())) {
            final float longitude = parseFloatCharacteristic(characteristic);
            intent.putExtra(TagBroadcastReceiver.GPS_LNG_DATA, longitude );
        } else if (UUID_FMP_SERVICE.equals(characteristic.getUuid())) {
            final int fmp = parseIntCharacteristic(characteristic);
            intent.putExtra(TagBroadcastReceiver.ACTION_PHONE_ALERTED, fmp);
        }
        sendBroadcast(intent);
    }

    private float parseFloatCharacteristic(BluetoothGattCharacteristic characteristic) {
        byte[] data = characteristic.getValue();
        return ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getFloat();
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

    public void readCharacteristic(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.readCharacteristic(characteristic);
    }

    private void writeCharacteristic(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.writeCharacteristic(characteristic);
    }

    public boolean writeLocateCharacteristic(BluetoothGatt bluetoothGatt, int soundAlarm){
        if(bluetoothGatt == null) return false;
        BluetoothGattService bluetoothGattService = bluetoothGatt.getService(BluetoothLEService.UUID_ALERT_SERVICE);
        if(bluetoothGattService != null) {
            BluetoothGattCharacteristic characteristic = bluetoothGattService.getCharacteristic(BluetoothLEService.UUID_ALERT_CHAR);
            characteristic.setValue(new byte[]{(byte) soundAlarm});
            writeCharacteristic(bluetoothGatt, characteristic);
            return true;
        }else{
            return false;
        }
    }

    public void setCharacteristicNotification(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        if (UUID_GPS_SERVICE.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            bluetoothGatt.writeDescriptor(descriptor);
        }
    }
}

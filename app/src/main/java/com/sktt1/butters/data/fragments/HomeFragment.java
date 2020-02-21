package com.sktt1.butters.data.fragments;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sktt1.butters.AddTagActivity;
import com.sktt1.butters.MainActivity;
import com.sktt1.butters.R;
import com.sktt1.butters.data.adapters.TagRecyclerAdapter;
import com.sktt1.butters.data.database.DatabaseHelper;
import com.sktt1.butters.data.models.Tag;
import com.sktt1.butters.data.receivers.TagBroadcastReceiver;

import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements TagRecyclerAdapter.OnTagListener {
    public static final String TAG = HomeFragment.class.getSimpleName();

    private static final int ADD_TAG_REQUEST_CODE = 4144;

    private RecyclerView mTagsView;
    private FloatingActionButton mFab;
    private TagRecyclerAdapter mTagRecyclerAdapter;

    private DatabaseHelper databaseHelper;
    private BroadcastReceiver mTagBluetoothBroadcastReceiver;

    private String mBluetoothName;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeBroadcastReceiver();
        initializeWidget(view);
        initializeRecyclerView();
    }


    private void initializeBroadcastReceiver() {
        mTagBluetoothBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (TagBroadcastReceiver.ACTION_GATT_CONNECTED.equals(action)) {
                    final BluetoothDevice bluetoothDevice = intent.getParcelableExtra(TagBroadcastReceiver.EXTRA_DATA);
//                    databaseHelper.get
                    if(bluetoothDevice != null){}
//                    tag.setConnected(true);
//                    mTagRecyclerAdapter.notifyDataSetChanged();
                    else{
                        if(mBluetoothName == null) return;
                        Tag newTag = new Tag(){{
                            setName(mBluetoothName);
                            setMacAddress(bluetoothDevice.getAddress());
                            setLastSeenTime(new Date());
                            setConnected(true);
                        }};
                        mBluetoothName = null;
                        mTagRecyclerAdapter.addTag(newTag);
                        databaseHelper.tagCreateDevice(newTag.getName(), newTag.getMacAddress(), newTag.getLastSeenLocationId(), newTag.getLastSeenTime().toString());
                    }
                } else if (TagBroadcastReceiver.ACTION_GATT_DISCONNECTED.equals(action)) {
                    BluetoothDevice disconnectedDevice = intent.getParcelableExtra(TagBroadcastReceiver.EXTRA_DATA);
                    Tag tag = mTagRecyclerAdapter.getTagByAddress(disconnectedDevice.getAddress());
                    tag.setConnected(false);
                    mTagRecyclerAdapter.notifyDataSetChanged();
                    ((MainActivity)getActivity()).mBluetoothLeService.removeBluetoothGatt(disconnectedDevice.getAddress());
                }
            }
        };
    }

    private void initializeWidget(View view) {
        mTagsView = view.findViewById(R.id.rv_home_tag_list);
        mFab = view.findViewById(R.id.fab_add_tag);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTagActivity.class);
                startActivityForResult(intent, ADD_TAG_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TAG_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String tagAddress = data.getStringExtra("tagAddress");
                    String tagName = data.getStringExtra("tagName");
                    if (((MainActivity) getActivity()).mBluetoothLeService.connect(tagAddress)) {
                        mBluetoothName = tagName;
                    }else{
                        Toast.makeText(getContext(), "Unable to connect to device", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getContext(), "Operation was canceled", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initializeRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mTagsView.setLayoutManager(linearLayoutManager);
        mTagRecyclerAdapter = new TagRecyclerAdapter(this);
        mTagsView.setAdapter(mTagRecyclerAdapter);
        mTagRecyclerAdapter.loadTags(databaseHelper.fetchTagData());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getContext() != null)
            getContext().registerReceiver(mTagBluetoothBroadcastReceiver, createTagIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getContext() != null)
            getContext().unregisterReceiver(mTagBluetoothBroadcastReceiver);
    }

    private static IntentFilter createTagIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TagBroadcastReceiver.ACTION_GATT_CONNECTED);
        intentFilter.addAction(TagBroadcastReceiver.ACTION_GATT_DISCONNECTED);
        return intentFilter;
    }

    @Override
    public void onClick(int index) {
        Tag tag = mTagRecyclerAdapter.getTag(index);
        if (tag != null) {
            BluetoothGatt gatt = ((MainActivity) getActivity()).mBluetoothLeService.getBluetoothGatt(tag.getMacAddress());
            if(gatt == null) return;
//            boolean status = ((MainActivity)getActivity()).mBluetoothLeService.writeLocateCharacteristic(gatt, tag.getSoundAlarm());
            boolean status = ((MainActivity)getActivity()).mBluetoothLeService.writeLocateCharacteristic(gatt, 2);
            if(!status){
                Toast.makeText(getContext(), "Unable to communicate with tag", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

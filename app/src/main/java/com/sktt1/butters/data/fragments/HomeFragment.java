package com.sktt1.butters.data.fragments;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.database.Cursor;
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

import com.sktt1.butters.R;
import com.sktt1.butters.data.adapters.TagRecyclerAdapter;
import com.sktt1.butters.data.database.DatabaseHelper;
import com.sktt1.butters.data.database.tables.TagTable;
import com.sktt1.butters.data.models.Tag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class HomeFragment extends Fragment implements TagRecyclerAdapter.OnTagListener {
    public  static final String TAG = "HomeFragment";

    private OnFragmentInteractionListener mListener;
    private RecyclerView mTagsView;
    private ArrayList<Tag> tags;
    private DatabaseHelper databaseHelper;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
//        getDeviceStatus();
        initializeWidget(view);
        initializeRecyclerView();
    }

    private void initializeWidget(View view){
        mTagsView = view.findViewById(R.id.rv_home_tag_list);
    }

//    private void getDeviceStatus(){
//    BluetoothAdapter.getDefaultAdapter()
//        if(bluetoothAdapter.isEnabled()){
//            Set<BluetoothDevice> connectedDevices = bluetoothAdapter.getBondedDevices();
//            for(Tag tag: tags){
//                for (BluetoothDevice device: connectedDevices) {
//                    if(tag.getMacAddress().equals(device.getAddress())){
//                        tag.setConnected(true);
//                    }
//                }
//            }
//        }
//    }

    private void initializeRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mTagsView.setLayoutManager(linearLayoutManager);
        TagRecyclerAdapter tagRecyclerAdapter = new TagRecyclerAdapter(tags, this);
        mTagsView.setAdapter(tagRecyclerAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
    }

    public void fetchData(){
        final Cursor data = databaseHelper.tagFeedList();

        tags = new ArrayList<>();
        data.moveToFirst();
        while(data.moveToNext()) {
            tags.add(
                    new Tag(){{
                        Date date = new Date();
                        try {
                            date = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString(data.getColumnIndex(TagTable.COL_LAST_SEEN_TIME)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        setId(data.getInt(data.getColumnIndex(TagTable.COL_ID)));
                        setName(data.getString(data.getColumnIndex(TagTable.COL_NAME)));
                        setMacAddress(data.getString(data.getColumnIndex(TagTable.COL_MAC_ADDRESS)));
                        setLastSeenLocationId(Integer.parseInt(data.getString(data.getColumnIndex(TagTable.COL_LAST_SEEN_LOCATION_ID))));
                        setLastSeenTime(date);
                        setSoundAlarm(Integer.parseInt((data.getString(data.getColumnIndex(TagTable.COL_SOUND_ALARM)))));

                    }}
            );
        }
    }

    @Override
    public void onClick(int index) {
        Log.d("TAG", tags.get(index).toString());
    }
}

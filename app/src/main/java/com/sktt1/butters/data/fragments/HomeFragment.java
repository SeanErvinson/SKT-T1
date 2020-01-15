package com.sktt1.butters.data.fragments;

import android.content.Context;
import android.net.Uri;
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
import com.sktt1.butters.data.models.Tag;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements TagRecyclerAdapter.OnTagListener {
    public  static final String TAG = "HomeFragment";

    private OnFragmentInteractionListener mListener;
    private RecyclerView mTagsView;
    private ArrayList<Tag> tags;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        TODO: Check for all connected devices
        //        if(bluetoothAdapter.isEnabled()){
//            Set<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getBondedDevices();
//            for (BluetoothDevice device: bluetoothDevices) {
//                Log.d(TAG, "initializeBluetooth: "+ device.getName());
//            }
//        }
        mTagsView = view.findViewById(R.id.rv_home_tag_list);
        mTagsView.setLayoutManager(linearLayoutManager);
        tags = new ArrayList<Tag>();
        tags.add(
                new Tag(){{
                    setId("1");
                    setName("First Tag");
                }}
        );
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
    public void onClick(int index) {
        Log.d("TAG", tags.get(index).toString());
    }
}

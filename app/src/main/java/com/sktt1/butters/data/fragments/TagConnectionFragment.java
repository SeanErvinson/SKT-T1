package com.sktt1.butters.data.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sktt1.butters.R;

public class TagScanFragment extends Fragment {
    private RecyclerView mScannedDevicesList;

    public TagScanFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tag_scan, container, false);
    }

}

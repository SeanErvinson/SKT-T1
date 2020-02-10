package com.sktt1.butters.data.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sktt1.butters.R;

public class TagConnectionFragment extends Fragment {
    private RecyclerView mScannedDevicesList;
    private FragmentListener mListener;

    public TagConnectionFragment() {}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeWidget(view);
        initializeRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tag_connection, container, false);
    }

    private void initializeWidget(View view){
        mScannedDevicesList = view.findViewById(R.id.rv_pair_tag_scanned_devices);
    }

    private void initializeRecyclerView(){
//        mScannedDevicesList.
    }

    public interface FragmentListener{
        void onSelectedDevice();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentListener){
            mListener = (FragmentListener) context;
        }else{
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

package com.sktt1.butters.data.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sktt1.butters.R;
import com.sktt1.butters.data.adapters.ActivityRecyclerAdapter;
import com.sktt1.butters.data.database.DatabaseHelper;
import com.sktt1.butters.data.database.tables.ActivityTable;
import com.sktt1.butters.data.models.Activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ActivityFragment extends Fragment implements ActivityRecyclerAdapter.OnActivityListener {
    public static final String TAG = "ActivityFragment";

    private OnFragmentInteractionListener mListener;
    private RecyclerView mActivityView;
    private ArrayList<Activity> activities;
    private ActivityRecyclerAdapter mActivityRecyclerAdapter;
    private DatabaseHelper databaseHelper;

    public ActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        activities = databaseHelper.fetchActivityData();
        initializeWidget(view);
        initializeRecyclerView();

    }

    private void initializeWidget(View view) {
        mActivityView = view.findViewById(R.id.rv_activity_activity_list);
    }

    private void initializeRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mActivityView.setLayoutManager(linearLayoutManager);
        mActivityRecyclerAdapter = new ActivityRecyclerAdapter(activities, this);
        mActivityView.setAdapter(mActivityRecyclerAdapter);
    }


    @Override
    public void onClick(int index) {
        Log.d("TAG", activities.get(index).toString());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}

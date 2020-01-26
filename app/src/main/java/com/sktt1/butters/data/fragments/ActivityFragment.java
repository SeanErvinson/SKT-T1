package com.sktt1.butters.data.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sktt1.butters.R;
import com.sktt1.butters.data.adapters.ActivityRecyclerAdapter;
import com.sktt1.butters.data.database.DatabaseHelper;
import com.sktt1.butters.data.models.Activity;

import java.util.ArrayList;


public class ActivityFragment extends Fragment implements ActivityRecyclerAdapter.OnActivityListener {
    public  static final String TAG = "ActivityFragment";

    private OnFragmentInteractionListener mListener;
    private RecyclerView mActivityView;
    private ArrayList<Activity> activities;
    private ActivityRecyclerAdapter activityRecyclerAdapter;
    private DatabaseHelper databaseHelper;

    public ActivityFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mActivityView = view.findViewById(R.id.rv_activity_activity_list);
        mActivityView.setLayoutManager(linearLayoutManager);
        activities = new ArrayList<>();
        activities.add(
                new Activity(){{
                    setId(1);
                    setMessage("Keys has been disconnected");
                }}
        );
//        activities.addAll(databaseHelper.activityFeedList());
        activityRecyclerAdapter = new ActivityRecyclerAdapter(activities, this);
        mActivityView.setAdapter(activityRecyclerAdapter);
    }


    @Override
    public void onClick(int index) {
        Log.d("TAG", activities.get(index).toString());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new DatabaseHelper(getActivity());
        databaseHelper.activityCreateNotification("OOPS NATANGAL","9/9/2019");
    }

    @Override
    public void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}

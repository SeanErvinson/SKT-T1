package com.sktt1.butters.data.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sktt1.butters.R;
import com.sktt1.butters.data.adapters.ActivityRecyclerAdapter;
import com.sktt1.butters.data.database.DatabaseHelper;


public class ActivityFragment extends Fragment implements ActivityRecyclerAdapter.OnActivityListener {
    public static final String TAG = ActivityFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;
    private RecyclerView mActivityView;
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
        initializeWidget(view);
        initializeRecyclerView();
    }

    private void initializeWidget(View view) {
        mActivityView = view.findViewById(R.id.rv_activity_activity_list);
    }

    private void initializeRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mActivityView.setLayoutManager(linearLayoutManager);
        mActivityView.setAdapter(mActivityRecyclerAdapter);
        mActivityView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }


    @Override
    public void onClick(int index) {
        Log.d("TAG", mActivityRecyclerAdapter.getActivity(index).getMessage());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
        mActivityRecyclerAdapter = new ActivityRecyclerAdapter(getContext(),this);
        mActivityRecyclerAdapter.loadActivities(databaseHelper.fetchActivityData());
    }

    @Override
    public void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}

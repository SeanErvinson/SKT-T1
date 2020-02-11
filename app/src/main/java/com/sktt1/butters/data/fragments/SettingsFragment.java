package com.sktt1.butters.data.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sktt1.butters.R;
import com.sktt1.butters.data.database.DatabaseHelper;
import com.sktt1.butters.data.database.tables.TagTable;
import com.sktt1.butters.data.models.Tag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SettingsFragment extends Fragment {
    public static final String TAG = "SettingsFragment";


    private ListView mSettingsTagsView;
    private OnFragmentInteractionListener mListener;
    private ArrayList<Tag> tags;
    private DatabaseHelper databaseHelper;

    public SettingsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
        initializeWidget(view);
    }

    private void initializeWidget(View view){
        mSettingsTagsView = view.findViewById(R.id.lv_settings_tags_list);
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

    private void fetchData(){
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



}

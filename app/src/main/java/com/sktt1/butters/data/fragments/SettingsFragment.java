package com.sktt1.butters.data.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sktt1.butters.R;
import com.sktt1.butters.data.database.DatabaseHelper;
import com.sktt1.butters.data.database.tables.TagTable;
import com.sktt1.butters.data.models.Tag;

import java.lang.reflect.Array;
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        fetchData();

        SettingsTagAdapter adapter = new SettingsTagAdapter(getContext(), tags);
        mSettingsTagsView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());


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
                        setConnected(Boolean.parseBoolean(data.getString(data.getColumnIndex(TagTable.COL_IS_CONNECTED))));

                    }}
            );
        }
    }


    class SettingsTagAdapter extends ArrayAdapter<Tag>{
        Context context;
        ArrayList<Tag> settingsTags;

        SettingsTagAdapter (Context c, ArrayList<Tag> sTags){
            super(c,R.layout.list_cell_settings_tag);
            this.context = c;
            this.settingsTags = sTags;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)  {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_cell_settings_tag, parent, false);
            TextView tvTagName = row.findViewById(R.id.tv_settingsTag_cell_name);
            TextView tvTagConnectivityStatus = row.findViewById(R.id.tv_settingsTag_cell_isConnected);
            TextView tvTagSoundAlarm = row.findViewById(R.id.tv_settingsTag_cell_soundAlarm);

            tvTagName.setText(settingsTags.get(position).getName());
            tvTagConnectivityStatus.setText(Boolean.toString(settingsTags.get(position).isConnected()));
            tvTagSoundAlarm.setText("yawits");
            return row;
        }
    }
}

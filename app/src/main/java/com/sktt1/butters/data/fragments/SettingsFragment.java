package com.sktt1.butters.data.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Ringtone;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sktt1.butters.EditFindMyPhoneAlarmActivity;
import com.sktt1.butters.EditUserDetailsActivity;
import com.sktt1.butters.R;
import com.sktt1.butters.data.database.DatabaseHelper;
import com.sktt1.butters.data.database.tables.TagTable;
import com.sktt1.butters.data.models.Tag;
import com.sktt1.butters.data.preference.SharedPreferenceHelper;
import com.sktt1.butters.data.utilities.DateTimePattern;
import com.sktt1.butters.data.utilities.DateUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "SettingsFragment";

    private OnFragmentInteractionListener mListener;
    private SharedPreferenceHelper sharedPreferencesHelper;

    private LinearLayout llSettingsUserData, llSettingsFindMyPhoneAlarm;
    private TextView tvSettingsUsername, tvSettingsUserNickname, tvSettingsFindMyPhoneAlarm, tvSettingsNumberDevices,
            tvSettingsNumberInactiveDevices;

    public SettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeWidget(view);
        updateUserDetails();


    }

    private void initializeWidget(View view) {
        llSettingsUserData = view.findViewById(R.id.ll_settings_userData);
        llSettingsFindMyPhoneAlarm = view.findViewById(R.id.ll_settings_find_my_phone_alarm);
        tvSettingsFindMyPhoneAlarm = view.findViewById(R.id.tv_settings_find_my_phone_alarm);
        tvSettingsUsername = view.findViewById(R.id.tv_settings_username);
        tvSettingsUserNickname = view.findViewById(R.id.tv_settings_nickname);
        tvSettingsNumberDevices = view.findViewById(R.id.tv_settings_number_devices);
        tvSettingsNumberInactiveDevices = view.findViewById(R.id.tv_settings_number_inactive_devices);

        llSettingsUserData.setOnClickListener(this);
        llSettingsFindMyPhoneAlarm.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        sharedPreferencesHelper = new SharedPreferenceHelper(context);

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
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserDetails();
    }

    @Override
    public void onClick(View view) {

        Intent intent;

        switch (view.getId()) {
            case R.id.ll_settings_userData:
                intent = new Intent(getActivity(), EditUserDetailsActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_settings_find_my_phone_alarm:
                intent = new Intent(getActivity(), EditFindMyPhoneAlarmActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void updateUserDetails() {
        tvSettingsUsername.setText(sharedPreferencesHelper.getUserName());
        tvSettingsUserNickname.setText(sharedPreferencesHelper.getUserNickname());
        tvSettingsFindMyPhoneAlarm.setText(sharedPreferencesHelper.getUserFindMyPhoneAlarm());

    }
}

package com.sktt1.butters;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sktt1.butters.data.adapters.FindMyPhoneAlarmAdapter;
import com.sktt1.butters.data.models.Ringtone;
import com.sktt1.butters.data.preference.SharedPreferenceHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditFindMyPhoneAlarmActivity extends AppCompatActivity {

    private ArrayList<Ringtone> ringtones;
    private ListView mRingtoneListView;
    private FindMyPhoneAlarmAdapter findMyPhoneAlarmAdapter;
    private MediaPlayer mp;
    private SharedPreferenceHelper sharedPreferenceHelper;
    private RelativeLayout rlAppBar;
    private ImageView mAccountProfile, mActivity, mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_find_my_phone_alarm);
        ringtones = getNotifications();
        initializeWidget();
        initializeAdapter();
        initializeActionBar();

    }

    private void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar == null)
            throw new NullPointerException("Action bar is not set.");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.main_action_bar);
        View view = getSupportActionBar().getCustomView();
        Toolbar parent = (Toolbar) view.getParent();
        parent.setContentInsetsAbsolute(0, 0);

//        rlAppBar = view.findViewById(R.id.rl_app_bar);
        mActivity = view.findViewById(R.id.iv_action_bar_notification);
        mAccountProfile = view.findViewById(R.id.iv_action_bar_account);

//        mBack = new ImageView(this);
//        mBack.setImageResource(R.drawable.ic_chevron_left_black_24dp);
//        mBack.setId(0);
//
//        rlAppBar.addView(mBack);
//        rlAppBar.bringChildToFront(mBack);
        mActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initializeAdapter() {

        ringtones = getNotifications();
        findMyPhoneAlarmAdapter = new FindMyPhoneAlarmAdapter(this, ringtones);
        mRingtoneListView.setAdapter(findMyPhoneAlarmAdapter);
        mRingtoneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ringtone ringtone = ringtones.get(position);

                Uri ringtoneSound = Uri.parse(ringtone.getRingtoneUri());
                mp = MediaPlayer.create(getApplicationContext(), ringtoneSound);
                mp.start();
                Toast.makeText(getApplicationContext(), ringtone.getRingtoneTitle(), Toast.LENGTH_SHORT).show();
                sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
                sharedPreferenceHelper.setUserFindMyPhoneAlarm(ringtone.getRingtoneTitle());
            }
        });
    }

    private void initializeWidget() {
        mRingtoneListView = findViewById(R.id.lv_ringtone);
    }

    public ArrayList<Ringtone> getNotifications() {
        RingtoneManager manager = new RingtoneManager(this);
        manager.setType(RingtoneManager.TYPE_NOTIFICATION);
        final Cursor cursor = manager.getCursor();

        ArrayList<Ringtone> list = new ArrayList<>();
        while (cursor.moveToNext()) {

            list.add(
                    new Ringtone() {{
                        setRingtoneTitle(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));
                        setRingtoneUri(cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(RingtoneManager.ID_COLUMN_INDEX));
                    }}
            );
        }
        return list;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mp != null) {
            mp.release();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

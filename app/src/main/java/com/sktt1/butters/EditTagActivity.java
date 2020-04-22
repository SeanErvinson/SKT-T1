package com.sktt1.butters;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sktt1.butters.data.database.DatabaseHelper;
import com.sktt1.butters.data.models.Tag;

public class EditTagActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvHighVolume;
    private TextView tvMediumVolume;
    private TextView tvLowVolume;
    private TextView tvTagName;
    private LinearLayout llEditTag;
    private int alarmVolume = 1;
    private Tag tag;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tag);

        Intent intent = getIntent();
        tag = intent.getParcelableExtra("tag");
        initializeWidget();
        initializeActionBar();
    }

    private void initializeWidget() {
        llEditTag = findViewById(R.id.ll_tag_name);
        tvTagName = findViewById(R.id.tv_tag_name);
        tvTagName.setText(tag.getName());
        tvHighVolume = findViewById(R.id.tv_tag_alarm_high);
        tvMediumVolume = findViewById(R.id.tv_tag_alarm_medium);
        tvLowVolume = findViewById(R.id.tv_tag_alarm_low);

        llEditTag.setOnClickListener(this);
        tvLowVolume.setOnClickListener(this);
        tvMediumVolume.setOnClickListener(this);
        tvHighVolume.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Context context = tvHighVolume.getContext();
        alarmVolume = tag.getAlarm();
        switch (view.getId()) {
            case R.id.tv_tag_alarm_high:
                alarmVolume = 0;
                Toast.makeText(context, "Alarm volume set to high", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_tag_alarm_medium:
                alarmVolume = 1;
                Toast.makeText(context, "Alarm volume set to medium", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_tag_alarm_low:
                alarmVolume = 2;
                Toast.makeText(context, "Alarm volume set to low", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_tag_name:
                Intent intent = new Intent(this, EditTagNameActivity.class);
                intent.putExtra("tag", tag);
                startActivity(intent);
                finish();
                break;
        }
        tag.setAlarm(alarmVolume);
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.tagUpdateSoundAlarm(tag.getId(), alarmVolume);
    }

    private void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar == null)
            throw new NullPointerException("Action bar is not set.");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.main_action_bar_back);
        View view = getSupportActionBar().getCustomView();
        Toolbar parent = (Toolbar) view.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ImageView mBack = view.findViewById(R.id.iv_action_bar_back_button);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }
}

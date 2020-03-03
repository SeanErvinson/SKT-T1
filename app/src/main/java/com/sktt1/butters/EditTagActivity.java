package com.sktt1.butters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sktt1.butters.data.database.DatabaseHelper;
import com.sktt1.butters.data.models.Tag;

public class EditTagActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvHighVolume;
    private TextView tvMediumVolume;
    private TextView tvLowVolume;
    private LinearLayout llEditTag;
    private int alarmVolume = 1;
    private Tag tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tag);
        initializeWidget();
    }

    private void initializeWidget(){
        tvHighVolume = findViewById(R.id.tv_tag_alarm_high);
        tvMediumVolume = findViewById(R.id.tv_tag_alarm_medium);
        tvLowVolume = findViewById(R.id.tv_tag_alarm_low);
        llEditTag = findViewById(R.id.ll_tag_details);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tag_alarm_high:
                alarmVolume = 2;
                break;
            case R.id.tv_tag_alarm_medium:
                alarmVolume = 1;
                break;
            case R.id.tv_tag_alarm_low:
                alarmVolume = 0;
                break;
            case R.id.ll_tag_details:
                Intent intent = new Intent(this, EditTagActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.tagUpdateSoundAlarm(id, alarmVolume);
    }
}

package com.sktt1.butters;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sktt1.butters.data.fragments.ActivityFragment;
import com.sktt1.butters.data.preference.SharedPreferenceHelper;

public class EditUserDetailsActivity extends AppCompatActivity {

    private EditText etEditName, etEditNickname;
    private Button btSaveUserDetails;
    private SharedPreferenceHelper sharedPreferenceHelper;
    private RelativeLayout rlAppBar;
    private ImageView mAccountProfile, mActivity, mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_details);
        initializeWidget();
        initializeActionBar();
        sharedPreferenceHelper = new SharedPreferenceHelper(this);
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

    private void initializeWidget() {
        etEditName = findViewById(R.id.et_edit_name);
        etEditNickname = findViewById(R.id.et_edit_nickname);
        btSaveUserDetails = findViewById(R.id.bt_save_user_details);

        btSaveUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = sharedPreferenceHelper.getUserName();
                String nickname = sharedPreferenceHelper.getUserNickname();

                if (etEditName.getText().length() != 0) {
                    name = etEditName.getText().toString();
                }
                if (etEditNickname.getText().length() != 0){
                    nickname = etEditNickname.getText().toString();
                }

                sharedPreferenceHelper.setUser(name,nickname);
                finish();

            }
        });
    }

}

package com.sktt1.butters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sktt1.butters.data.preference.SharedPreferenceHelper;

public class EditUserDetailsActivity extends AppCompatActivity {

    private EditText etEditName, etEditNickname;
    private Button btSaveUserDetails;
    private SharedPreferenceHelper sharedPreferenceHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_details);
        initializeWidget();
        sharedPreferenceHelper = new SharedPreferenceHelper(this);
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

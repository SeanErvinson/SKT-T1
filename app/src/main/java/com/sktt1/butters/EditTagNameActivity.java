package com.sktt1.butters;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sktt1.butters.data.database.DatabaseHelper;
import com.sktt1.butters.data.models.Tag;

public class EditTagNameActivity extends AppCompatActivity {

    private EditText etTagName;
    private Button btUpdate;
    private Tag tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tag_name);

        Intent intent = getIntent();
        tag = intent.getParcelableExtra("tag");
        initializeView();
        initializeActionBar();
    }

    public void initializeView() {
        etTagName = findViewById(R.id.et_edit_tag_label);
        btUpdate = findViewById(R.id.btn_tag_label_update);

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = null;
                if (etTagName.getText().length() != 0) {
                    name = etTagName.getText().toString();
                    DatabaseHelper databaseHelper = new DatabaseHelper(etTagName.getContext());
                    databaseHelper.tagUpdateName(name, tag.getId());
                    databaseHelper.close();
                    Toast.makeText(etTagName.getContext(), "Tag label updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(etTagName.getContext(), "Tag label unchanged", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
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
    protected void onResume() {
        super.onResume();
        initializeView();
    }
}

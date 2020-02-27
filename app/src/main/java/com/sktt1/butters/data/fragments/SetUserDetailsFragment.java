package com.sktt1.butters.data.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sktt1.butters.MainActivity;
import com.sktt1.butters.R;
import com.sktt1.butters.data.preference.SharedPreferenceHelper;

public class SetUserDetailsFragment extends Fragment {

    private EditText etEditName, etEditNickname;
    private SharedPreferenceHelper sharedPreferenceHelper;

    public SetUserDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_user_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeWidget(view);
    }

    public void initializeWidget(View view) {
        etEditName = view.findViewById(R.id.et_edit_name);
        etEditNickname = view.findViewById(R.id.et_edit_nickname);
        Button btSaveUserDetails = view.findViewById(R.id.bt_save_user_details);

        btSaveUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = sharedPreferenceHelper.getUserName();
                String nickname = sharedPreferenceHelper.getUserNickname();

                if (etEditName.getText().length() != 0) {
                    name = etEditName.getText().toString();
                }
                if (etEditNickname.getText().length() != 0) {
                    nickname = etEditNickname.getText().toString();
                }

                sharedPreferenceHelper.setUser(name, nickname);

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceHelper = new SharedPreferenceHelper(getActivity());
    }
}

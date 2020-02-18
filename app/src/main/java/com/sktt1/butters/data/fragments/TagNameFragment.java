package com.sktt1.butters.data.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sktt1.butters.AddTagActivity;
import com.sktt1.butters.R;

public class TagNameFragment extends Fragment {
    private static final String TAG = TagNameFragment.class.getSimpleName();

    public interface FragmentListener {
        void inputChange(CharSequence label);
    }

    private EditText mTagLabel;
    private FragmentListener mListener;

    public TagNameFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeWidget(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tag_name, container, false);
    }

    private void initializeWidget(View view) {
        mTagLabel = view.findViewById(R.id.et_add_tag_label);

        mTagLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                AddTagActivity tagActivity = (AddTagActivity) getActivity();
                if (tagActivity == null) return;
                if (charSequence.length() > 0)
                    tagActivity.hideNavButton(false);
                else
                    tagActivity.hideNavButton(true);
                mListener.inputChange(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            mListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

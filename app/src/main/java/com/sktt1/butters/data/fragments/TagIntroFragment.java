package com.sktt1.butters.data.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sktt1.butters.R;

public class TagIntroFragment extends Fragment {

    private int mLayout;

    public TagIntroFragment() {}

    public static TagIntroFragment newInstance(int layout){
        TagIntroFragment tagIntroFragment = new TagIntroFragment();
        Bundle args = new Bundle();
        args.putInt("layout", layout);
        tagIntroFragment.setArguments(args);
        return tagIntroFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            mLayout = getArguments().getInt("layout", R.layout.add_tag_instruction_1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(mLayout, container, false);
    }

}

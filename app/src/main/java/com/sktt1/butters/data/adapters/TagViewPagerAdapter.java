package com.sktt1.butters.data.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class TagViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public TagViewPagerAdapter(@NonNull FragmentManager fm, List<Fragment> fragments) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments != null ? fragments.size() : 0;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}

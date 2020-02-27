package com.sktt1.butters;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.sktt1.butters.data.extensions.ZoomOutPageTransformer;
import com.sktt1.butters.data.fragments.SetUserDetailsFragment;
import com.sktt1.butters.data.fragments.SliderFragment;

public class SliderActivity extends FragmentActivity {

    private static final int NUM_PAGES = 2;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        viewPager = findViewById(R.id.vp_slider);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        private ScreenSlidePagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 1) {
                return new SetUserDetailsFragment();
            }
            return new SliderFragment();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}
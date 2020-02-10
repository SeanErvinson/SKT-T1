package com.sktt1.butters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sktt1.butters.data.adapters.TagViewPagerAdapter;
import com.sktt1.butters.data.extensions.LockViewPager;
import com.sktt1.butters.data.fragments.TagIntroFragment;
import com.sktt1.butters.data.fragments.TagNameFragment;
import com.sktt1.butters.data.models.Tag;

import java.util.ArrayList;

public class AddTagActivity extends AppCompatActivity implements TagNameFragment.FragmentListener {

    private LockViewPager mViewPager;
    private TagViewPagerAdapter mTagViewPagerAdapter;
    private Button mNext;
    private String mTagLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);
        getSupportActionBar().hide();
        initializeWidget();
        initializeViewPager();
    }

    private void initializeWidget() {
        mViewPager = findViewById(R.id.lvp_add_tag);
        mViewPager.disableScroll(true);
        mNext = findViewById(R.id.btn_add_tag_next);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(+1);
                if (current < mTagViewPagerAdapter.getCount()) {
                    mViewPager.setCurrentItem(current);
                } else {
                    Intent resultIntent = new Intent();

                    Tag newTag = new Tag() {{
                        setName(mTagLabel);
                        setMacAddress(""); //Whatever was passed
                    }};
                    resultIntent.putExtra("newTag", newTag);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }

    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }


    private void initializeViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(TagIntroFragment.newInstance(R.layout.add_tag_instruction_1));
        fragments.add(new TagNameFragment());
        fragments.add(TagIntroFragment.newInstance(R.layout.add_tag_instruction_1));
        mTagViewPagerAdapter = new TagViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mTagViewPagerAdapter);
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == mTagViewPagerAdapter.getCount() - 1) {
                mNext.setText(getResources().getString(R.string.done));
            }
            if (position == mTagViewPagerAdapter.getCount() - 2) {
                mNext.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void hideNavButton(boolean state) {
        mNext.setVisibility(state ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void inputChange(CharSequence label) {
        mTagLabel = label.toString();
    }
}
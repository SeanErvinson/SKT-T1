package com.sktt1.butters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sktt1.butters.data.fragments.ActivityFragment;
import com.sktt1.butters.data.fragments.HomeFragment;
import com.sktt1.butters.data.fragments.MapFragment;
import com.sktt1.butters.data.fragments.OnFragmentInteractionListener;
import com.sktt1.butters.data.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    private BottomNavigationView mBottomNavigationView;
    private Fragment mHomeFragment, mMapFragment, mSettingsFragment, mActivityFragment;
    private ImageView mAccountProfile, mActivity;
    private Fragment currentFragment;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeActionBar();
        initializeWidget();
        initializeFragments();
//        Intent intent = new Intent(this, PairTagActivity.class);
//        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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

        mActivity = view.findViewById(R.id.iv_action_bar_notification);
        mAccountProfile = view.findViewById(R.id.iv_action_bar_account);

        mActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().hide(currentFragment).show(mActivityFragment).commit();
                currentFragment = mActivityFragment;
            }
        });
    }

    private void initializeWidget() {
        mBottomNavigationView = findViewById(R.id.bnv_main_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void initializeFragments() {
        mHomeFragment = new HomeFragment();
        mMapFragment = new MapFragment();
        mSettingsFragment = new SettingsFragment();
        mActivityFragment = new ActivityFragment();
        fragmentManager = getSupportFragmentManager();
        currentFragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.fl_main_container, currentFragment).commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                currentFragment = new HomeFragment();
                break;
            case R.id.navigation_map:
                currentFragment = new MapFragment();
                break;
            case R.id.navigation_settings:
                currentFragment = new SettingsFragment();
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.fl_main_container, currentFragment).commit();
        return true;
    }
}

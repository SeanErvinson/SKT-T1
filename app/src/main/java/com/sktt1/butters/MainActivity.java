package com.sktt1.butters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sktt1.butters.data.fragments.HomeFragment;
import com.sktt1.butters.data.fragments.MapFragment;
import com.sktt1.butters.data.fragments.OnFragmentInteractionListener;
import com.sktt1.butters.data.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    private static final String TAG = "MainActivity";

    private BottomNavigationView mBottomNavigationView;
    private FrameLayout mMainContainer;
    private Fragment mHomeFragment, mMapFragment, mSettingsFragment;
    private Fragment currentFragment;
    private FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeActionBar();
        initializeWidget();
        initializeFragments();

        mFragmentManager.beginTransaction().add(R.id.fl_main_container, mHomeFragment, HomeFragment.TAG).hide(mSettingsFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.fl_main_container, mMapFragment, MapFragment.TAG).hide(mMapFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.fl_main_container,mSettingsFragment, SettingsFragment.TAG).commit();

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        mFragmentManager.beginTransaction().hide(currentFragment).show(mHomeFragment).commit();
                        currentFragment = mHomeFragment;
                        return true;

                    case R.id.navigation_map:
                        mFragmentManager.beginTransaction().hide(currentFragment).show(mMapFragment).commit();
                        currentFragment = mMapFragment;
                        return true;

                    case R.id.navigation_settings:
                        mFragmentManager.beginTransaction().hide(currentFragment).show(mSettingsFragment).commit();
                        currentFragment = mSettingsFragment;
                        return true;
                }
                return false;
            }
        });
    }

    private void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        View view = getLayoutInflater().inflate(R.layout.main_action_bar,
                null);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(view, layoutParams);
        Toolbar parent = (Toolbar) view.getParent();
        parent.setContentInsetsAbsolute(0, 0);
    }

    private void initializeWidget(){
        mMainContainer = findViewById(R.id.fl_main_container);
        mBottomNavigationView = findViewById(R.id.bnv_main_navigation);
    }

    private void initializeFragments(){
        mHomeFragment = new HomeFragment();
        mMapFragment = new MapFragment();
        mSettingsFragment = new SettingsFragment();
        currentFragment = mHomeFragment;
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

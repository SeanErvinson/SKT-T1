package com.sktt1.butters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sktt1.butters.data.database.DatabaseHelper;
import com.sktt1.butters.data.fragments.ActivityFragment;
import com.sktt1.butters.data.fragments.HomeFragment;
import com.sktt1.butters.data.fragments.MapFragment;
import com.sktt1.butters.data.fragments.OnFragmentInteractionListener;
import com.sktt1.butters.data.fragments.SettingsFragment;
import com.sktt1.butters.data.models.Tag;
import com.sktt1.butters.data.receivers.TagBroadcastReceiver;
import com.sktt1.butters.data.services.BluetoothLEService;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private BottomNavigationView mBottomNavigationView;
    private Fragment mHomeFragment, mMapFragment, mSettingsFragment, mActivityFragment;
    private ImageView mActivity;
    private FragmentManager mFragmentManager;
    private TagBroadcastReceiver mTagBroadcastReceiver;
    public BluetoothLEService mBluetoothLeService;
    private DatabaseHelper databaseHelper;
    private ArrayList<Tag> tags;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLEService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
            for (Tag tag : tags) {
                mBluetoothLeService.connect(tag.getMacAddress(), false);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        initializeTags();
        initializeActionBar();
        initializeBroadcastReceivers();
        initializeWidget();
        Intent gattServiceIntent = new Intent(this, BluetoothLEService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        initializeFragments();
        registerReceiver(mTagBroadcastReceiver, createTagIntentFilter());
    }

    private void initializeTags() {
        tags = databaseHelper.fetchTagData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mTagBroadcastReceiver);
        unbindService(mServiceConnection);
    }

    private static IntentFilter createTagIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TagBroadcastReceiver.ACTION_GATT_CONNECTED);
        intentFilter.addAction(TagBroadcastReceiver.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(TagBroadcastReceiver.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(TagBroadcastReceiver.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(TagBroadcastReceiver.ACTION_PHONE_ALERTED);
        intentFilter.addAction(TagBroadcastReceiver.ACTION_SIT_ALERTED);
        intentFilter.addAction(TagBroadcastReceiver.ACTION_STOP_SOUND);
        return intentFilter;
    }

    private void initializeBroadcastReceivers() {
        mTagBroadcastReceiver = new TagBroadcastReceiver(new DatabaseHelper(this));
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
        mActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new ActivityFragment(), ActivityFragment.class.getSimpleName());
            }
        });
    }

    private void initializeWidget() {
        mBottomNavigationView = findViewById(R.id.bnv_main_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void initializeFragments() {
        mFragmentManager = getSupportFragmentManager();
        changeFragment(new HomeFragment(), HomeFragment.class.getSimpleName());
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                changeFragment(new HomeFragment(), HomeFragment.class.getSimpleName());
                break;
            case R.id.navigation_map:
                changeFragment(new MapFragment(), MapFragment.class.getSimpleName());
                break;
            case R.id.navigation_settings:
                changeFragment(new SettingsFragment(), SettingsFragment.class.getSimpleName());
                break;
        }
        return true;
    }

    public void changeFragment(Fragment fragment, String tagFragmentName) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            if (currentFragment.getTag().equals(ActivityFragment.class.getSimpleName())) {
                fragmentTransaction.remove(currentFragment);
            } else {
                fragmentTransaction.hide(currentFragment);
            }
        }
        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(R.id.fl_main_container, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }
        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }
}

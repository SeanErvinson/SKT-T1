package com.sktt1.butters.data.fragments;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sktt1.butters.MainActivity;
import com.sktt1.butters.R;
import com.sktt1.butters.data.database.DatabaseHelper;
import com.sktt1.butters.data.models.Tag;
import com.sktt1.butters.data.receivers.TagBroadcastReceiver;
import com.sktt1.butters.data.utilities.DateTimePattern;
import com.sktt1.butters.data.utilities.DateUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.sktt1.butters.data.receivers.TagBroadcastReceiver.GPS_LAT_DATA;
import static com.sktt1.butters.data.receivers.TagBroadcastReceiver.GPS_LNG_DATA;
import static com.sktt1.butters.data.receivers.TagBroadcastReceiver.TAG_DATA;
import static com.sktt1.butters.data.services.SKTGattAttributes.UUID_GPS_SERVICE;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final String TAG = MapFragment.class.getSimpleName();
    private final static int LOCATION_ENABLE_REQUEST = 1002;

    private Button mBuzz, mSit;
    private BottomSheetBehavior mBottomSheetBehavior;
    private LinearLayout mBottonSheet;
    private TextView mMapTagName, mMapTagLastLocation, mMapTagTime;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private CameraPosition mainCamera;
    private Location currentLocation;
    private boolean isLocationUpdate;
    private LocationCallback locationCallback;
    private BroadcastReceiver mBroadcastReceiver;
    private ArrayList<Marker> mMarkers = new ArrayList<>();
    private ArrayList<Tag> tags;
    private HashMap<Integer, com.sktt1.butters.data.models.Location> locations;
    private Circle mTagRange;
    private Geocoder mGeocoder;

    private DatabaseHelper databaseHelper;

    private LocationRequest locationRequest;

    public MapFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
        tags =  ((MainActivity)getActivity()).getTags();
        locations = new HashMap<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeBroadcastReceiver();
        initializeWidget(view);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        mGeocoder = new Geocoder(getContext());
        isLocationUpdate = false;
        initializeLocationCallback();
        initializeLocationRequest();
    }

    private void initializeMarkers() {
        for (Tag tag : tags) {
            com.sktt1.butters.data.models.Location location = databaseHelper.getLocationById(tag.getLastSeenLocationId());
            locations.put(tag.getLastSeenLocationId(), location);
            if (location == null) continue;
            mMarkers.add(addMapMarker(tag, new LatLng(location.getLatitude(), location.getLongitude())));
            BluetoothGatt gatt = ((MainActivity) getActivity()).mBluetoothLeService.getBluetoothGatt(tag.getMacAddress());
            ((MainActivity) getActivity()).mBluetoothLeService.readCharacteristic(gatt, gatt.getService(UUID.fromString(UUID_GPS_SERVICE)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    private void initializeBroadcastReceiver() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (TagBroadcastReceiver.ACTION_DATA_AVAILABLE.equals(action)) {
                    String address = intent.getStringExtra(TAG_DATA);
                    Tag tag = getTagFromAddress(address);
                    if (tag == null) return;
                    if (tag.isSit()) {
                        Intent sitIntent = new Intent(TagBroadcastReceiver.ACTION_SIT_ALERTED);
                        sitIntent.putExtra(TagBroadcastReceiver.TAG_DATA, address);
                        getActivity().sendBroadcast(sitIntent);
                        tag.setSit(false);
                    }
                    com.sktt1.butters.data.models.Location location = locations.get(tag.getLastSeenLocationId());
                    if (location == null) return;
                    double lng = intent.getFloatExtra(GPS_LNG_DATA, (float) location.getLongitude());
                    double lat = intent.getFloatExtra(GPS_LAT_DATA, (float) location.getLatitude());
                    lat = lat == 0 ? location.getLatitude() : lat;
                    lng = lng == 0 ? location.getLongitude() : lng;
                    if(lng == 0 && lat == 0) return;
                    mGeocoder = new Geocoder(getContext());
                    List<Address> addresses;
                    String featuredName = location.getName();
                    try {
                        addresses = mGeocoder.getFromLocation(lat, lng, 1);
                        featuredName = addresses.get(0).getFeatureName();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    location.setLatitude(lat);
                    location.setLongitude(lng);
                    location.setName(featuredName);
                    databaseHelper.tagUpdateLocation(tag.getId(), featuredName, lng, lat);
                    updateMarker(address, new LatLng(lat, lng));
                } else if (TagBroadcastReceiver.ACTION_GATT_CONNECTED.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(TagBroadcastReceiver.EXTRA_DATA);
                    BluetoothGatt gatt = ((MainActivity) getActivity()).mBluetoothLeService.getBluetoothGatt(device.getAddress());
                    ((MainActivity) getActivity()).mBluetoothLeService.readCharacteristic(gatt, gatt.getService(UUID.fromString(UUID_GPS_SERVICE)));
                }
            }
        };
    }

    private Tag getTagFromAddress(String address) {
        for (Tag tag : tags) {
            if (address.equals(tag.getMacAddress())) {
                return tag;
            }
        }
        return null;
    }

    private void initializeWidget(View view) {
        mBuzz = view.findViewById(R.id.btn_map_buzz);
        mSit = view.findViewById(R.id.btn_map_sit);
        mMapTagName = view.findViewById(R.id.tv_map_tag_name);
        mMapTagLastLocation = view.findViewById(R.id.tv_map_tag_last_location);
        mMapTagTime = view.findViewById(R.id.tv_map_tag_time);
        mBottonSheet = view.findViewById(R.id.bs_map_action);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottonSheet);
        mBottomSheetBehavior.setPeekHeight(0);
    }

    private void initializeLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);
    }

    private void initializeLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        currentLocation = location;
                        if (mTagRange == null) {
                            initializeTagRange();
                        }
                        if (mMap != null) {
                            updateTagRange();
                        }
                    }
                }
            }
        };
    }

    private void initializeTagRange() {
        mTagRange = mMap.addCircle(new CircleOptions()
                .center(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .radius(40)
                .fillColor(Color.argb(25, 240, 0, 0))
                .strokeColor(Color.argb(30, 240, 0, 0))
                .strokeWidth(1));
    }

    private void updateMarker(String address, LatLng location) {
        if (mMap == null) return;
        for (Marker marker : mMarkers) {
            Tag currentTag = (Tag) marker.getTag();
            if (currentTag == null) return;
            if (currentTag.getMacAddress().equals(address)) {
                double lat = marker.getPosition().latitude;
                double lng = marker.getPosition().longitude;
                if (location.latitude != 0) lat = location.latitude;
                if (location.longitude != 0) lng = location.longitude;
                marker.setPosition(new LatLng(lat, lng));
                return;
            }
        }
        mMap.addMarker(createTagMarker(location));
    }

    private void updateTagRange() {
        mTagRange.setCenter(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
    }


    private void startLocationUpdate() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                isLocationUpdate = true;
                currentLocation = location;
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                if (mapFragment != null)
                    mapFragment.getMapAsync(MapFragment.this);
            }
        });
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void stopLocationUpdate() {
        isLocationUpdate = false;
        mTagRange = null;
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }


    private void checkPermissions() {
        if (getContext() == null) return;
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_ENABLE_REQUEST);
        } else {
            startLocationUpdate();
        }
    }

    private MarkerOptions createTagMarker(LatLng latLng) {
        return new MarkerOptions()
                .position(latLng)
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_dot));
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        if (context == null) return null;
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.clear();

        mainCamera = CameraPosition.builder()
                .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .zoom(18)
                .bearing(0)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mainCamera), 1000, null);
        initializeMarkers();
    }

    private void setBottomSheetInformation(Marker selectedMarker) {
        if (selectedMarker == null) return;
        final Tag selectedTag = (Tag) selectedMarker.getTag();
        if (selectedTag == null) return;
        mMapTagName.setText(selectedTag.getName());
        String time = DateUtility.getFormattedDate(selectedTag.getLastSeenTime(), DateTimePattern.TIME);
        mMapTagTime.setText(getString(R.string.time_recorded, time));
        com.sktt1.butters.data.models.Location location = locations.get(selectedTag.getLastSeenLocationId());
        mMapTagLastLocation.setText(getString(R.string.last_seen_location, location.getName()));
        mBuzz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothGatt bluetoothGatt = ((MainActivity) getActivity()).mBluetoothLeService.getBluetoothGatt(selectedTag.getMacAddress());
                boolean status = ((MainActivity) getActivity()).mBluetoothLeService.writeLocateCharacteristic(bluetoothGatt, selectedTag.getAlarm());
                if (!status) {
                    Toast.makeText(getContext(), "Unable to communicate with tag", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTag.setSit(true);
            }
        });
    }

    private void resetBottomSheetInformation() {
        mMapTagName.setText(null);
        mMapTagTime.setText(null);
        mMapTagLastLocation.setText(null);
        mSit.setOnClickListener(null);
        mBuzz.setOnClickListener(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdate();
        if (getContext() != null)
            getContext().unregisterReceiver(mBroadcastReceiver);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!isLocationUpdate) {
            checkPermissions();
        }
        if (getContext() != null)
            getContext().registerReceiver(mBroadcastReceiver, createTagIntentFilter());
    }

    private static IntentFilter createTagIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TagBroadcastReceiver.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(TagBroadcastReceiver.ACTION_GATT_CONNECTED);
        return intentFilter;
    }

    private Marker addMapMarker(Tag tag, LatLng latLng) {
        Marker marker = mMap.addMarker(createTagMarker(latLng));
        marker.setTag(tag);
        return marker;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        stopLocationUpdate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_ENABLE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdate();
            } else {
                Toast.makeText(getContext(), getString(R.string.location_error_message), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == LOCATION_ENABLE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                isLocationUpdate = true;
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            marker.setIcon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_dot_selected));
            setBottomSheetInformation(marker);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            marker.setIcon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_dot));
            resetBottomSheetInformation();
        }
        return true;
    }
}


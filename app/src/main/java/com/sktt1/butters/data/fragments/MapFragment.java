package com.sktt1.butters.data.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sktt1.butters.R;
import com.sktt1.butters.data.models.Tag;
import com.sktt1.butters.data.utilities.DateTimePattern;
import com.sktt1.butters.data.utilities.DateUtility;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    public static final String TAG = "MapFragment";
    private final static int LOCATION_ENABLE_REQUEST = 1002;

    private OnFragmentInteractionListener mListener;
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
    private Map<Marker, Tag> mTagMarkers = new HashMap<>();

    private LocationRequest locationRequest;

    public MapFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeWidget(view);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        isLocationUpdate = false;
        initializeLocationCallback();
        initializeLocationRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
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
                        if (mMap != null) {
                            mMap.clear();
                            Tag newTag = new Tag() {{
                                setName("Hello World");
                                setLastSeenTime(new Date());
                            }};
                            Marker newMarker = mMap.addMarker(addTagMarker(newTag));
                            mTagMarkers.put(newMarker, newTag);
                            mMap.addCircle(new CircleOptions()
                                    .center(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                                    .radius(40)
                                    .fillColor(Color.argb(25, 240, 0, 0))
                                    .strokeColor(Color.argb(30, 240, 0, 0))
                                    .strokeWidth(1));
                        }
                    }
                }
            }
        };
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
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }


    private void checkPermissions() {
        if(getContext() == null) return;
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_ENABLE_REQUEST);
        } else {
            startLocationUpdate();
        }
    }

    public MarkerOptions addTagMarker(Tag tag) {
        MarkerOptions m = new MarkerOptions()
                .position(new LatLng(14.6229344, 120.9913965))
                .title(tag.getName())
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_dot));
        if (tag.getLastSeenLocationId() != 0) {
            m.snippet(String.valueOf(tag.getId()));
        }
        return m;
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
        mMap.setMyLocationEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMinZoomPreference(18);

        mMap.clear();

        mainCamera = CameraPosition.builder()
                .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .zoom(18)
                .bearing(0)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mainCamera), 1000, null);
    }

    private void setBottomSheetInformation(Marker selectedMarker){
        final Tag selectedTag = mTagMarkers.get(selectedMarker);
        if(selectedTag == null || selectedMarker == null){
            return;
        }
        mMapTagName.setText(selectedTag.getName());
        String time = DateUtility.getFormattedDate(selectedTag.getLastSeenTime(), DateTimePattern.TIME);
        mMapTagTime.setText(getString(R.string.time_recorded, time));
//        String location = selectedTag.getLastSeenLocationId();
        mMapTagLastLocation.setText(getString(R.string.last_seen_location, "UST"));
        mBuzz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Buzz "+selectedTag.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        mSit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Sit "+selectedTag.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetBottomSheetInformation(){
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
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!isLocationUpdate) {
            checkPermissions();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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


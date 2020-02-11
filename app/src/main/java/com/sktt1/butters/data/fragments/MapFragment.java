package com.sktt1.butters.data.fragments;

import android.content.Context;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.sktt1.butters.R;
import com.sktt1.butters.data.models.Tag;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    public static final String TAG = "MapFragment";

    private OnFragmentInteractionListener mListener;
    private Button mBuzz, mSit;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private CameraPosition mainCamera;
    private Location currentLocation;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    public MapFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeWidget(view);

        mSit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTag(new Tag() {{
                    setName("Hello");
                }});
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        currentLocation = location;
                        Log.d(TAG, "Location " + location.getLongitude() + "----" + location.getLatitude());
                        if(mMap != null)
                            mMap.clear();
                            mMap.addCircle(new CircleOptions()
                                    .center(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                                    .radius(70)
                                    .fillColor(Color.argb(25, 240, 0, 0))
                                    .strokeColor(Color.argb(30, 240, 0, 0))
                                    .strokeWidth(1));
                    }
                }
            }
        };

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.d(TAG, "Success");
                currentLocation = location;
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                if(mapFragment != null)
                    mapFragment.getMapAsync(MapFragment.this);
            }
        });

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        return rootView;
    }

    private void initializeWidget(View view) {
        mBuzz = view.findViewById(R.id.btn_map_buzz);
        mSit = view.findViewById(R.id.btn_map_sit);
    }


    public void addTag(Tag tag) {
        MarkerOptions m = new MarkerOptions()
                .position(new LatLng(37.4219999, -122.0862462))
                .title(tag.getName())
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_person_24px));
        if (tag.getLastSeenLocationId() != 0) {
            m.snippet(String.valueOf(tag.getId()));
        }
        mMap.addMarker(m);
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMinZoomPreference(15);
        googleMap.setMyLocationEnabled(true);

        googleMap.clear();

        mainCamera = CameraPosition.builder()
                .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .zoom(18)
                .bearing(0)
                .build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(mainCamera), 1000, null);
    }
}

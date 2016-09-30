package com.fimo_pitch.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fimo_pitch.R;
import com.fimo_pitch.support.ShowToast;
import com.fimo_pitch.support.TrackGPS;
import com.fimo_pitch.support.Utils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.fimo_pitch.R.id.edt_location;

/**
 */
public class SearchFragment extends Fragment implements OnMapReadyCallback {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String TAG="SearchFragment";
    private MapFragment fragment;
    private Location myLocation;
    private int LOCATION=1;
    private GoogleApiClient client;
    private TrackGPS gps;
    private GoogleMap map;
    // 144 xuan thuy : lat : 21.036654, lng 105.781218
    private SupportMapFragment mapFragment;
    private LocationManager locationManager;
    private boolean checkNetwork=false;
    private boolean checkGPS=false;
    private LatLng xuanthuy = new LatLng(21.036654,105.78218);
    private int permissionCode=9999;
    private ArrayList<LatLng> latLngs;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        Log.d(TAG,"mapFragment null");

        initMap();
        return view;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==permissionCode)
        {
            Log.d(TAG,"OK");

            if(grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            }
        }
        else
            Log.d(TAG,"Not ok");

    }
    public void initMap()
    {
        mapFragment = new SupportMapFragment() {
            @Override
            public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                if(mapFragment!=null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            map = googleMap;
                            Utils.moveCamera(xuanthuy,12,map);
                            Utils.showCircle(xuanthuy,4,map);
                            map.addMarker(new MarkerOptions().position(xuanthuy));
                            initMapLicense();

                        }
                    });
                }

            }
        };
        getChildFragmentManager().beginTransaction().add(R.id.fragment_map, mapFragment).commit();
    }
    public void initMapLicense()
    {
        if(map!=null)
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Utils.showCircle(latLng, 2000,map);
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude, 1);
                    String cityName = addresses.get(0).getAddressLine(0);
                    String stateName = addresses.get(0).getAddressLine(1);
                    Log.d(TAG,addresses.toString()+"");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                map.clear();
                Utils.showCircle(cameraPosition.target,12,map);
                Random random =new Random();
                int x = random.nextInt(6 - 1 + 1) + 1;

            }
        });
    }
    public SearchFragment()
    {}
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }
}

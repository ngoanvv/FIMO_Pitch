package com.fimo_pitch.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.fimo_pitch.R;
import com.fimo_pitch.support.TrackGPS;
import com.fimo_pitch.support.Utils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Diep_Chelsea on 16/08/2016.
 */
public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener,
        LocationSource.OnLocationChangedListener, GoogleMap.OnCameraChangeListener {
    private static String TAG="SearchActivity";
    private GoogleMap map;
    private MapFragment fragment;
    private Location myLocation;
    private int LOCATION=1;
    GoogleApiClient client;
    private TrackGPS gps;
    protected LocationManager locationManager;
    private boolean checkNetwork=false;
    private boolean checkGPS=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);


    }


    public void addMarker() {
        map.addMarker(new MarkerOptions().position(new LatLng(21.025764, 105.81134160)));
        map.addMarker(new MarkerOptions().position(new LatLng(21.0237642, 105.8334160)));
        map.addMarker(new MarkerOptions().position(new LatLng(21.0277643, 105.8534160)));
        map.addMarker(new MarkerOptions().position(new LatLng(21.029977644, 105.8634160)));
        moveCamera(new LatLng(21.029977644, 105.8634160),12);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    public void moveCamera(LatLng latLng,int zoom) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom  );
        map.animateCamera(cameraUpdate);
        map.addMarker(new MarkerOptions().position(latLng));

    }



    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!checkNetwork) Utils.showInternetSettingsAlert(SearchActivity.this);
        else {
            if(!checkGPS)
            Utils.showGpsSettingsAlert(SearchActivity.this);
            else
            {
                gps = new TrackGPS(SearchActivity.this);
                if(gps.canGetLocation()){
                    double longitude = gps.getLongitude();
                    double latitude = gps .getLatitude();
                    LatLng latLng = new LatLng(latitude,longitude);
                    Log.d(TAG,"lat : " + latitude +" lng :"+longitude);
                    moveCamera(new LatLng(gps.getLatitude(),gps.getLongitude()),12);
                    showCircle(latLng,5000);
                }
                else
                {
                    Utils.openDialog(SearchActivity.this,"Không định vị được vị trí của bạn");
                }
            }
        }

    }
    public void showCircle(LatLng latLng, double radius)
    {
        map.clear();
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng)
                .center(latLng)
                .clickable(true)
                .strokeColor(Color.parseColor("#F6CECE"))
                .fillColor(Color.parseColor("#F6CECE"))
                .radius(radius);
        map.addCircle(circleOptions);
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_flag)).position(latLng);
        map.addMarker(markerOptions);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gps = new TrackGPS(SearchActivity.this);
            if(gps.canGetLocation()){
                double longitude = gps.getLongitude();
                double latitude = gps .getLatitude();
                Log.d(TAG,"lat : " + latitude +" lng :"+longitude);
                LatLng latLng = new LatLng(gps.getLatitude(),gps.getLongitude());
                moveCamera(latLng,12);
                showCircle(latLng,5000);

            }
            else
            {
                Utils.openDialog(SearchActivity.this,"Không định vị được vị trí của bạn");
            }
        }
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
        map.setOnCameraChangeListener(this);




    }

    @Override
    public void onMapClick(LatLng latLng) {
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
//        Log.d(TAG,"lat :"+cameraPosition.target.latitude+" long : "+cameraPosition.target.longitude);
//        showCircle(cameraPosition.target,10000);
    }

}

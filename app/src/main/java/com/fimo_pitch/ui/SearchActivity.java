package com.fimo_pitch.ui;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.fimo_pitch.R;
import com.fimo_pitch.support.GPSTracker;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);


    }

    public void showGpsSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    public void showInternetSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Network settings");
        alertDialog.setMessage("Network is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
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
        if(map==null)
            Log.d(TAG,"map null");
        else
        {
            if(isGPSEnabled(this)) {
                Log.d(TAG,"map not null");
                getGPS();

            }
        }

    }

    public void moveCamera(LatLng latLng,int zoom) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom  );
        map.animateCamera(cameraUpdate);
    }

    public void getGPS() {

        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation())
        {
            Log.d(TAG,"in GPS(): GPS: "+ gpsTracker.getLatitude()+" -- "+gpsTracker.getLongitude());
            myLocation=gpsTracker.getLocation();
            LatLng currentLatLng = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
            showCircle(currentLatLng,7000);
            moveCamera(currentLatLng,12);
        }
        else
        {
            Toast.makeText(SearchActivity.this, "Cannot get Location", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isGPSEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try
        {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(network_enabled == false) showInternetSettingsAlert();
            else
            {
                if(gps_enabled==false) showGpsSettingsAlert();
                else
                {
                    return true;
                }
            }



        }
        catch (Exception ex)
        {
            return false;

        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    public void showCircle(LatLng latLng, double radius)
    {
        map.clear();
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng)
                .center(latLng)
                .clickable(true)
                .strokeColor(Color.parseColor("#AEEDE4"))
                .fillColor(Color.parseColor("#AEEDE4"))
                .radius(radius);
        map.addCircle(circleOptions);
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_flag)).position(latLng);
        map.addMarker(markerOptions);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if(isGPSEnabled(this))
        {
            getGPS();
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
//        showCircle(new LatLng(location.getLatitude(),location.getLongitude()),10000);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.d(TAG,"lat :"+cameraPosition.target.latitude+" long : "+cameraPosition.target.longitude);
        showCircle(cameraPosition.target,10000);
//        moveCamera(cameraPosition.target,12);

    }
}

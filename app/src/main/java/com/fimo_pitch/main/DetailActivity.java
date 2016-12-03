package com.fimo_pitch.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.fimo_pitch.R;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.support.TrackGPS;
import com.fimo_pitch.support.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, DirectionCallback {
    private RoundedImageView bt_call,bt_order;
    private int callRequest = 1;
    private int gpsRequest = 2;

    private String TAG = "DetailActivity";
    private SupportMapFragment mapFragment;
    private TextView bt_now;
    private GoogleMap map;
    private TrackGPS gps;
    private LatLng currentLatLng;
    private LatLng start,end;
    private List<Route> listRoute;
    private LatLng xuanthuy = new LatLng(21.036654,105.78218);
    private JSONObject rawDirections;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        bt_order.setOnClickListener(this);
        bt_call.setOnClickListener(this);
        bt_now.setOnClickListener(this);
        ActivityCompat.requestPermissions(DetailActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},gpsRequest);
    }

    public void initView() {
        bt_call = (RoundedImageView) findViewById(R.id.bt_call);
        bt_order = (RoundedImageView) findViewById(R.id.bt_order);
        bt_now = (TextView) findViewById(R.id.bt_now);

        mapFragment = new SupportMapFragment();
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.detail_map, mapFragment).commit();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_call: {
                ActivityCompat.requestPermissions(DetailActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, callRequest);

                break;
            }
            case R.id.bt_order: {
                startActivity(new Intent(DetailActivity.this,PricingActivity.class));
                break;
            }
            case R.id.bt_now: {
                startActivity(new Intent(DetailActivity.this,ListPitchActivity.class));
                break;
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == callRequest)
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "01266343244"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        if (requestCode == gpsRequest)
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                gps = new TrackGPS(DetailActivity.this,DetailActivity.this);
                if(gps.canGetLocation()){
                    double longitude = gps.getLongitude();
                    double latitude = gps .getLatitude();
                    Log.d(TAG,"lat : " + latitude +" lng :"+longitude);
                    currentLatLng = new LatLng(gps.getLatitude(),gps.getLongitude());
                    map.addMarker(new MarkerOptions().position(currentLatLng));
                    Utils.moveCamera(currentLatLng,12,map);
                    if(Utils.isConnected(DetailActivity.this))
                    {
                        drawLine(map,currentLatLng,xuanthuy);
//                        map.addPolyline(new PolylineOptions().add(currentLatLng,xuanthuy).color(Color.BLUE).width(5));

                    }
                }
            }
    }
    public void drawLine(GoogleMap map,LatLng start,LatLng finish)
    {
        GoogleDirection.withServerKey("AIzaSyAbx2lqsUq1OfOCHUbk7N_DPlyNzeP1n6s")
                .from(start)
                .to(finish)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(mapFragment !=null) {
            map = googleMap;

        }
    }
    public void drawPolyline(GoogleMap map,LatLng start,LatLng end)
    {
        map.addPolyline(new PolylineOptions().add(start,end).color(Color.BLUE).width(7));
    }
    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        JSONObject routes,legs;
        JSONArray steps;
        Route route = direction.getRouteList().get(0);
        Leg leg = route.getLegList().get(0);
        List<Step> stepList = leg.getStepList();
        Log.d("Size ",stepList.size()+"");
        for (int i=0;i<stepList.size()-1;i++)
        {
        }
        map.addMarker(new MarkerOptions().position(leg.getEndLocation().getCoordination()));
        map.addMarker(new MarkerOptions().position(leg.getStartLocation().getCoordination()));

        ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
        PolylineOptions polylineOptions = DirectionConverter.createPolyline(this, directionPositionList, 5,
                                                    getResources().getColor(R.color.bluehistory));

        map.addPolyline(polylineOptions);
    }

    @Override
    public void onDirectionFailure(Throwable t) {
            Log.d(TAG,"Direction Failed");
    }
}

package com.fimo_pitch.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.HttpRequest;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.SystemPitchAdapter;
import com.fimo_pitch.main.DetailActivity;
import com.fimo_pitch.model.SystemPitch;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 */
public class SearchFragment extends Fragment implements OnMapReadyCallback, Response.ErrorListener, Response.Listener {
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
    private  SupportMapFragment mapFragment;
    private LocationManager locationManager;
    private boolean checkNetwork=false;
    private boolean checkGPS=false;
    private LatLng xuanthuy = new LatLng(21.036654,105.78218);
    private int permissionCode=9999;
    private ArrayList<LatLng> latLngs;
    private LatLng currentLatLng;
    private LatLng start,end,waypoint;
    private ArrayList<SystemPitch> listSystemPitch;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_search, container, false);
            mapFragment = new SupportMapFragment();
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
            getChildFragmentManager().beginTransaction().add(R.id.fragment_map, mapFragment).commit();
            return view;
        }
        catch (Exception e)
        {
            return inflater.inflate(R.layout.empty, container, false);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==permissionCode)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                gps = new TrackGPS(getContext(),getActivity());
                if(gps.canGetLocation()){
                    double longitude = gps.getLongitude();
                    double latitude = gps .getLatitude();
                    Log.d("SearchFragment","lat : " + latitude +" lng :"+longitude);
                    currentLatLng = new LatLng(gps.getLatitude(),gps.getLongitude());
                    map.addMarker(new MarkerOptions().position(currentLatLng));
                    Utils.showCircle(currentLatLng,5000,map);
                    Utils.moveCamera(currentLatLng,13,map);
                }
            }
        }
        else
            Utils.openDialog(getContext(),"Không định vị được vị trí của bạn");
    }
    public void initMapLicense() {
        if (map != null)
        {
            getData();
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                }
            });
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    SystemPitch systemPitch = new SystemPitch();
                    systemPitch.setComment("3.3");
                    systemPitch.setOwnerName("Tiến TM");
                    systemPitch.setPhone("0923829832");
                    systemPitch.setOwnerID("12123123");
                    systemPitch.setName("Sân bóng Cổ Loa");
                    systemPitch.setLat(currentLatLng.latitude+"");
                    systemPitch.setLng(currentLatLng.longitude+"");
                    systemPitch.setId("1");
                    systemPitch.setDescription("Sân đẹp sân ngon ahihi");
                    systemPitch.setAddress("Số 43, Cổ Loa, Đông Anh, Hà Nội");
                    systemPitch.setRating("3.4");
                    Intent intent =new Intent(getActivity(),DetailActivity.class);
                    intent.putExtra(CONSTANT.SystemPitch_MODEL,systemPitch);
                    startActivity(intent);
                    return true;
                }
            });
            map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    Random random = new Random();
                    int x = random.nextInt(6 - 1 + 1) + 1;
                }
            });
        }
    }
    public SearchFragment() {}
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



        if(googleMap !=null) {
            map = googleMap;
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},permissionCode);
            initMapLicense();
            gps = new TrackGPS(getContext(),getActivity());
            if(gps.canGetLocation())
            {
                double longitude = gps.getLongitude();
                double latitude = gps .getLatitude();
                currentLatLng = new LatLng(gps.getLatitude(),gps.getLongitude());
                map.addMarker(new MarkerOptions().position(currentLatLng).title("Sân bóng Cổ Loa")).showInfoWindow();
//                    Utils.showCircle(currentLatLng,5000,map);
                Utils.moveCamera(currentLatLng,12,map);
            }

        }
    }
    private  void getData() {
        listSystemPitch = new ArrayList<>();
        HttpRequest.HttpGETrequest(getContext(),API.getSystemPitch,this,this);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(Object response) {
        Log.d(TAG,response.toString());
        try {
            JSONObject result = new JSONObject((String) response);
            if(result.getString("status").contains("success"))
            {
                JSONArray data = result.getJSONArray("data");
                if(data !=null)
                {
                    for(int i =0;i<data.length()-1;i++)
                    {
                        JSONObject object =data.getJSONObject(i);
                        SystemPitch systemPitch = new SystemPitch();
                        systemPitch.setId(object.getString("id"));
                        systemPitch.setName(object.getString("name"));
                        systemPitch.setAddress(object.getString("address"));
                        systemPitch.setDescription(object.getString("description"));
                        systemPitch.setPhone(object.getString("phone"));
                        systemPitch.setLat(object.getString("lat"));
                        systemPitch.setLng(object.getString("lng"));
                        systemPitch.setOwnerID(object.getString("user_id"));
                        systemPitch.setComment("3");
                        systemPitch.setOwnerName("Trần Mạnh Tiến");
                        listSystemPitch.add(systemPitch);

                        LatLng mLatLng = new LatLng(Double.valueOf(object.getString("lat")),Double.valueOf(object.getString("lng")));

                        Log.d(TAG,mLatLng.toString());
                    }

                }
            }
        } catch (JSONException e) {
            Utils.openDialog(getContext(),"Không thể tải trang, thử lại sau");
            e.printStackTrace();
        }
    }
}

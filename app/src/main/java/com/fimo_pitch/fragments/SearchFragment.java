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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.SystemPitchAdapter;
import com.fimo_pitch.main.DetailActivity;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.support.ShowToast;
import com.fimo_pitch.support.TrackGPS;
import com.fimo_pitch.support.Utils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
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

import static android.R.layout.select_dialog_item;

/**
 */
public class SearchFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String TAG="SearchFragment";
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
    private AutoCompleteTextView search_box;
    private ImageView bt_currentLocation;
    private static String[] address ={"Ba Đình","Cầu Giấy","Thanh Xuân","Hà Đông","Mai Dịch","Cổ Nhuế","Từ Liêm","Hai Bà Trưng"};

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_search, container, false);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (getContext(), android.R.layout.simple_dropdown_item_1line,address);
            search_box = (AutoCompleteTextView) view.findViewById(R.id.search_box);
            search_box.setThreshold(2);
            search_box.setAdapter(adapter);


            bt_currentLocation = (ImageView) view.findViewById(R.id.bt_currentLocation) ;
            bt_currentLocation.setOnClickListener(this);

            mapFragment = new SupportMapFragment();

            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
            getChildFragmentManager().beginTransaction().add(R.id.fragment_map, mapFragment).commit();

            return view;
        }
        catch (Exception e)
        {
            Log.d(TAG,e.getMessage().toString());
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
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id)
        {
            case R.id.bt_currentLocation :
            {
                Utils.moveCamera(currentLatLng,13,map);
                break;
            }
        }
    }
}

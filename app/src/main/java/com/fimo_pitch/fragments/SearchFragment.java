package com.fimo_pitch.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.NetworkUtils;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.SystemPitchAdapter;
import com.fimo_pitch.main.DetailActivity;
import com.fimo_pitch.main.MainActivity;
import com.fimo_pitch.model.DirectionStep;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.support.ShowToast;
import com.fimo_pitch.support.TrackGPS;
import com.fimo_pitch.support.Utils;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;


import okhttp3.OkHttpClient;
import okhttp3.Request;

import static android.R.layout.select_dialog_item;

/**
 */
public class SearchFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String TAG="SearchFragment";
    private TrackGPS gps;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private LatLng xuanthuy = new LatLng(21.036654,105.78218);
    private int permissionCode=9999;
    private ArrayList<LatLng> latLngs;
    private LatLng currentLatLng;
    private ArrayList<SystemPitch> listSystemPitch;
    private AutoCompleteTextView search_box;
    private ImageView bt_currentLocation;
    private static String[] address ={"Ba Đình","Cầu Giấy","Thanh Xuân","Hà Đông","Mai Dịch","Cổ Nhuế","Từ Liêm","Hai Bà Trưng"};
    private String result,data;
    private LatLng resultLatLng;
    private OkHttpClient okHttpClient;
    private ArrayList<DirectionStep> directionSteps;
    private ArrayList<Polyline> polylines;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_search, container, false);
            okHttpClient = new OkHttpClient();
            directionSteps = new ArrayList<>();
            polylines = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (getContext(), android.R.layout.simple_dropdown_item_1line,address);


            resultLatLng = new LatLng(0.0,0.0);

            search_box = (AutoCompleteTextView) view.findViewById(R.id.search_box);
            search_box.setThreshold(2);
            search_box.setAdapter(adapter);
            search_box.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG,search_box.getText().toString()+"");
                        String url = "http://maps.google.com/maps/api/geocode/json?address="
                                + search_box.getText().toString() + "&sensor=false";
                        if(Utils.isConnected(getContext()))
                        new Sendrequest(url).execute();
                        else Utils.openDialog(getContext(),getString(R.string.no_connection));

                }
            });


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
    private class GetDirections extends AsyncTask<LatLng, Void, String> {
        ProgressDialog progressDialog;
        @Override
        protected String doInBackground(LatLng... params) {
            getDirections(params[0],params[1],"directions_mode");

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            for (int i=0;i<directionSteps.size();i++)
            {
                drawLine(i,directionSteps.get(i).getStart(),directionSteps.get(i).getEnd(),"","");
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.processing));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    public String getDirections(LatLng start, LatLng end, String mode) {
        String url = "http://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + start.latitude + "," + start.longitude
                + "&destination=" + end.latitude + "," + end.longitude
                + "&sensor=false&units=metric&mode=" + mode;
        Log.d(TAG,url);
        Request newsRequest = new Request.Builder()
                .url(url)
                .build();
        try {
            okHttpClient = new OkHttpClient();
            okhttp3.Response response = okHttpClient.newCall(newsRequest).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());

            JSONArray routesArray = jsonObject.getJSONArray("routes");

            if(routesArray.length() > 0)
            {
                JSONObject route = routesArray.getJSONObject(0);
                JSONArray legs = route.getJSONArray("legs");
                JSONObject leg = legs.getJSONObject(0);
                JSONObject durationObject = leg.getJSONObject("duration");
                JSONArray steps = leg.getJSONArray("steps");
                if(steps.length()>0)
                {
                    for(int i =0;i<steps.length();i++)
                    {
                        JSONObject root = steps.getJSONObject(i);
                        JSONObject distance = root.getJSONObject("distance");
                        JSONObject end_location = root.getJSONObject("end_location");
                        JSONObject start_location = root.getJSONObject("start_location");

                        DirectionStep item = new DirectionStep();
                        item.setEnd(new LatLng(Double.valueOf(end_location.getString("lat")),Double.valueOf(end_location.getString("lng"))));
                        item.setStart(new LatLng(Double.valueOf(start_location.getString("lat")),Double.valueOf(start_location.getString("lng"))));
                        item.setDistance(distance.getString("text"));
                        directionSteps.add(item);
                    }

                }
            }
            else
            {
                Utils.openDialog(getContext(),"Không tìm thấy đường đi khả dụng");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url;
    }
    public void drawLine(int index,LatLng start,LatLng end,String distance,String des)
    {
        map.addPolyline(new PolylineOptions().add(start,end).color(Color.BLUE).width(8));
        polylines.add(index, this.map.addPolyline(new PolylineOptions().add(start,end).color(Color.BLUE).width(8)));
    }
    public void initList() {
        Log.d(TAG,data);
        listSystemPitch = new ArrayList<>();
        if (data.contains("success")) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length() - 1; i++) {
                    JSONObject object = data.getJSONObject(i);
                    SystemPitch systemPitch = new SystemPitch();
                    systemPitch.setDescription(object.getString("description"));
                    systemPitch.setId(object.getString("id"));
                    systemPitch.setOwnerName("Tiến TM");
                    systemPitch.setOwnerID("user_id");
                    systemPitch.setName(object.getString("name"));
                    systemPitch.setAddress(object.getString("address"));
                    systemPitch.setId("id");
                    systemPitch.setLat(object.getString("lat"));
                    systemPitch.setLng(object.getString("log"));
                    listSystemPitch.add(systemPitch);

                    MarkerOptions options = new MarkerOptions();
                    Double lat = Double.valueOf(object.getString("lat"));
                    Double lng = Double.valueOf(object.getString("log"));
                    options.position(new LatLng(lat,lng)).title("Sân bóng 123x");
                    map.addMarker(options.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_marker_free)))).showInfoWindow();

                }
            } catch (JSONException e) {

            }
        }
    }
    private class Sendrequest extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        public String url;
        public Sendrequest(String url) {
            this.url=url;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(getString(R.string.processing));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            search_box.setText(result);
            Utils.moveCamera(resultLatLng,result,12,map);

        }


        @Override
        protected String doInBackground(String... params) {
            result=" ";
            Request getLatlng = new Request.Builder()
                    .url(this.url)
                    .build();
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                okhttp3.Response newsResponse = okHttpClient.newCall(getLatlng).execute();
                if (newsResponse.isSuccessful())
                {
                    result = newsResponse.body().string();
                    JSONObject  root = new JSONObject(result) ;
                    if(root.getString("status").contains("OK"))
                    {
                        JSONArray results = root.getJSONArray("results");
                        if(results != null)
                        {
                            JSONObject data = results.getJSONObject(0);
                            JSONObject geometry = data.getJSONObject("geometry");
                            String formatted_address = data.getString("formatted_address");
                            JSONObject location = geometry.getJSONObject("location");
                            double lat = location.getDouble("lat");
                            double lng = location.getDouble("lng");
                            resultLatLng = new LatLng(lat,lng);
                            return formatted_address;
                        }
                    }
                }
            } catch (Exception e) {
                Log.d(TAG,"error");

                e.printStackTrace();
            }
            return result;
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
                    Utils.moveCamera(currentLatLng,"Bạn ở đây",13,map);
                }
            }
        }
        else
            Utils.openDialog(getContext(),"Không định vị được vị trí của bạn");
    }
    public void initMapLicense() {
        if (map != null)
        {
//            getData();
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                }
            });
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
//                    SystemPitch systemPitch = new SystemPitch();
//                    systemPitch.setComment("3.3");
//                    systemPitch.setOwnerName("Tiến TM");
//                    systemPitch.setPhone("0923829832");
//                    systemPitch.setOwnerID("12123123");
//                    systemPitch.setName("Sân bóng Cổ Loa");
//                    systemPitch.setLat(currentLatLng.latitude+"");
//                    systemPitch.setLng(currentLatLng.longitude+"");
//                    systemPitch.setId("1");
//                    systemPitch.setDescription("Sân đẹp sân ngon ahihi");
//                    systemPitch.setAddress("Số 43, Cổ Loa, Đông Anh, Hà Nội");
//                    systemPitch.setRating("3.4");
                      new GetDirections().execute(marker.getPosition(),currentLatLng);

//                      getDirections(marker.getPosition(),currentLatLng,"directions_mode");

//                    Intent intent =new Intent(getActivity(),DetailActivity.class);
//                    intent.putExtra(CONSTANT.SystemPitch_MODEL,systemPitch);
//                    startActivity(intent);
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
    public static SearchFragment newInstance(String mdata, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.data = mdata;
        args.putString(ARG_PARAM1, mdata);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.marker_icon);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(googleMap !=null) {
            map = googleMap;
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},permissionCode);
            initList();
            initMapLicense();
            gps = new TrackGPS(getContext(),getActivity());
            if(gps.canGetLocation())
            {
                double longitude = gps.getLongitude();
                double latitude = gps .getLatitude();
                currentLatLng = new LatLng(gps.getLatitude(),gps.getLongitude());
//                map.addMarker(new MarkerOptions().position(currentLatLng).title("Bạn ở đây")
//                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_ok)))).showInfoWindow();
//                    Utils.showCircle(currentLatLng,5000,map);
                Utils.moveCamera(currentLatLng,"Bạn ở đây",12,map);


            }

        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id)
        {
            case R.id.bt_currentLocation :
            {
                Utils.moveCamera(currentLatLng,"Bạn ở đây",13,map);
                break;
            }
        }
    }


}

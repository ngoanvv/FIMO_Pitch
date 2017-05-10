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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.custom.view.DetailDialog;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.main.DetailActivity;
import com.fimo_pitch.model.DirectionStep;
import com.fimo_pitch.model.SearchPitchModel;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.support.NetworkUtils;
import com.fimo_pitch.support.TrackGPS;
import com.fimo_pitch.support.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 */
public class SearchFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, DetailDialog.OnMoreDetailEvent {
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
    private ArrayList<SystemPitch> listSystemPitch,tmpList;
    private ArrayList<SearchPitchModel> listSearch;
    private String currentTime="07:00";
    private Spinner search_box;
    private ImageView bt_currentLocation,bt_clear;
    private String result,data;
    private LatLng resultLatLng;
    private OkHttpClient okHttpClient;
    private ArrayList<DirectionStep> directionSteps;
    private List<Polyline> polylines;
    private RoundedImageView bt_search;
    private TextView tv_time;
    private int mHour,mMinute;
    private String listSystemData="";
    private int choosePostition;
    private String sHour="7";
    private String sMinute="00";
    private String location="Cầu Giấy";
    private int dateofweek=1;
    private LatLng chooseLatlng;
    private int callRequest=1111;
    private Spinner spinner_location;
    private EditText edt_search;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_search, container, false);
            okHttpClient = new OkHttpClient();
            directionSteps = new ArrayList<>();
            polylines = new ArrayList<>();
            listSystemPitch = new ArrayList<>();
            tmpList =  new ArrayList<>();
            resultLatLng = new LatLng(0.0,0.0);

//            bt_search  =  (RoundedImageView) view.findViewById(R.id.bt_search);
            bt_currentLocation = (ImageView) view.findViewById(R.id.bt_currentLocation) ;
            bt_clear = (ImageView) view.findViewById(R.id.bt_clear) ;
            bt_clear.setOnClickListener(this);
            bt_currentLocation.setOnClickListener(this);
            spinner_location = (Spinner) view.findViewById(R.id.spn_location);

            edt_search = (EditText) view.findViewById(R.id.edt_search);
            edt_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        drawMarker(filterByText(s.toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tmpList = filterByAddress(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item2,getActivity().getResources().getStringArray(R.array.listProvince));
            spinner_location.setAdapter(adapter);
            spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position != 0)
                    {
                        try {
                            drawMarker(filterByAddress(spinner_location.getItemAtPosition(position).toString()));
                        } catch (Exception e) {
                            Utils.openDialog(getContext(),"Đã có lỗi xảy ra. Vui lòng thử lại sau");
                            e.printStackTrace();
                        }
                        tmpList = filterByAddress(spinner_location.getItemAtPosition(position).toString());
                    }
                    else try {
                        drawMarker(listSystemPitch);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.openDialog(getContext(),"Đã có lỗi xảy ra. Vui lòng thử lại sau");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            mapFragment = new SupportMapFragment();
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
            getChildFragmentManager().beginTransaction().add(R.id.fragment_map, mapFragment).commit();
            return view;
        }
        catch (Exception e)
        {
            Utils.openDialog(getContext(),"Đã có lỗi xảy ra. Vui lòng thử lại sau");
            e.printStackTrace();
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
            for(int j=0;j<polylines.size();j++)
            {
                polylines.get(j).remove();
                polylines.remove(j);
            }
            for (int i=0;i<directionSteps.size();i++)
            {
                try {
                    drawLine(i,directionSteps.get(i).getStart(),directionSteps.get(i).getEnd(),"","");
                } catch (Exception e) {
                    Utils.openDialog(getContext(),"Đã có lỗi xảy ra. Vui lòng thử lại sau");
                    e.printStackTrace();
                }
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
            Utils.openDialog(getContext(),"Đã có lỗi xảy ra. Vui lòng thử lại sau");

            e.printStackTrace();
        }
        return url;
    }
    public void drawMarker(String  data) throws Exception
    {
             listSystemPitch = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(data);
            JSONArray array = jsonObject.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                SystemPitch systemPitch = new SystemPitch();
                systemPitch.setDescription(object.getString("description"));
                systemPitch.setId(object.getString("id"));
                systemPitch.setOwnerName("Tiến TM");
                systemPitch.setOwnerID(object.getString("user_id"));
                systemPitch.setName(object.getString("name"));
                systemPitch.setAddress(object.getString("address"));
                systemPitch.setId(object.getString("id"));
                systemPitch.setPhone(object.getString("phone"));
                systemPitch.setLat(object.getString("lat"));
                systemPitch.setLng(object.getString("log"));
                listSystemPitch.add(systemPitch);
            }

        map.clear();
        if(currentLatLng != null)
        map.addMarker(new MarkerOptions().position(currentLatLng).title("Bạn ở đây"));
        for(int i=0;i<listSystemPitch.size();i++)
        {
            MarkerOptions options = new MarkerOptions();
            Double lat = Double.valueOf(listSystemPitch.get(i).getLat());
            Double lng = Double.valueOf(listSystemPitch.get(i).getLng());
            options.position(new LatLng(lat,lng));
            options.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_marker_free)));
            Marker marker = map.addMarker(options);
            marker.setTitle(listSystemPitch.get(i).getName());
            options.title(listSystemPitch.get(i).getName());
            marker.showInfoWindow();
            marker.setTag(i);
            map.addMarker(options);
        }
    }
    public void drawMarker(ArrayList<SystemPitch> data) throws Exception
    {

        map.clear();
        map.addMarker(new MarkerOptions().position(currentLatLng).title("Bạn ở đây"));
        for(int i=0;i<data.size();i++)
        {
            MarkerOptions options = new MarkerOptions();
            Double lat = Double.valueOf(data.get(i).getLat());
            Double lng = Double.valueOf(data.get(i).getLng());
            options.position(new LatLng(lat,lng));
            options.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_marker_free)));
            Marker marker = map.addMarker(options);
            marker.setTitle(data.get(i).getName());
            options.title(data.get(i).getName());
            marker.showInfoWindow();
            marker.setTag(i);
            map.addMarker(options);
        }
    }
    public void drawLine(int index,LatLng start,LatLng end,String distance,String des) throws Exception
    {

        Polyline polyline = map.addPolyline(new PolylineOptions()
                .add(new LatLng(start.latitude, start.longitude), new LatLng(end.latitude, end.longitude))
                .width(8)
                .color(Color.BLUE));
        polylines.add(polyline);
    }

    private class Sendrequest extends AsyncTask<String, Void, LatLng> {
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
        protected void onPostExecute(LatLng result) {
            progressDialog.dismiss();
            if(result != null)
                Utils.moveCamera(result,"Khu vực bạn chọn",12,map);
            else
            {
                Utils.openDialog(getContext(),"Đã có lỗi xảy ra. Vui lòng thử lai");
            }
        }


        @Override
        protected LatLng doInBackground(String... params) {
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
                            return resultLatLng;
                        }
                    }
                }
            } catch (Exception e) {
                Utils.openDialog(getContext(),"Đã có lỗi xảy ra. Vui lòng thử lại sau");
                e.printStackTrace();
                return null;
            }
            return null;
        }
    }
    private ArrayList<SystemPitch> filterByAddress(String s)
    {
        ArrayList result = new ArrayList();
        for(int i=0;i<listSystemPitch.size();i++)
        {
            if(listSystemPitch.get(i).getAddress().contains(s))
                result.add(listSystemPitch.get(i));
        }
        return result;
    }
    private ArrayList<SystemPitch> filterByText(String s)
    {
        ArrayList result = new ArrayList();
        for(int i=0;i<listSystemPitch.size();i++)
        {
            if(listSystemPitch.get(i).getAddress().contains(s) || listSystemPitch.get(i).getName().contains(s) ||
                    listSystemPitch.get(i).getDescription().contains(s) ||
                    listSystemPitch.get(i).getOwnerName().contains(s))
                result.add(listSystemPitch.get(i));
        }
        return result;
    }
    public void initMapLicense() throws Exception{
        if (map != null)
        {
            double lat = Double.parseDouble(getContext().getSharedPreferences("data", Context.MODE_PRIVATE).getString("lat","0.0"));
            double lng = Double.parseDouble(getContext().getSharedPreferences("data", Context.MODE_PRIVATE).getString("lng","0.0"));
            currentLatLng = new LatLng(lat,lng);
            Log.d("latlng",lat+":"+lng);
            if(lat != 0.0 && lng != 0.0)
            {
                Utils.moveCamera(new LatLng(lat,lng),"Bạn đang ở đây",12,map);
            }
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                }
            });
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if(marker.getTag() != null) {
                        int position = (int) marker.getTag();
                        SystemPitch systemPitch = listSystemPitch.get(position);
                        map.addMarker(new MarkerOptions().title("Bạn ở đây").position(currentLatLng));
                        DetailDialog dialog = new DetailDialog(getContext(), systemPitch,position);
                        dialog.setOnArrivalDeliverListener(SearchFragment.this);
                        dialog.show(getFragmentManager(), TAG);
                        choosePostition = position;
                        chooseLatlng = marker.getPosition();
                        Log.d("choose latlng",chooseLatlng.toString()+" ");
                    }
                     return true;
                }
            });
//            map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//                @Override
//                public void onCameraChange(CameraPosition cameraPosition) {
//                    Random random = new Random();
//                    int x = random.nextInt(6 - 1 + 1) + 1;
//                }
//            });
        }
    }
    // xu ly thao tac tu dialog


    @Override
    public void onConfirmed(boolean confirm) {
        // xem chi tiet
        if (confirm) {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            SystemPitch system = listSystemPitch.get(choosePostition);
            intent.putExtra(CONSTANT.KEY_USER,getActivity().getIntent().getSerializableExtra(CONSTANT.KEY_USER));
            intent.putExtra(CONSTANT.SystemPitch_MODEL,system);
            startActivity(intent);
        }
        // nhan chi duong
        else
        {
//             ActivityCompat.requestPermissions(getActivity(),
//                new String[]{Manifest.permission.CALL_PHONE}, callRequest);
            String uri = "http://maps.google.com/maps?saddr="+currentLatLng.latitude+","+currentLatLng.longitude+"&daddr="+
                    chooseLatlng.latitude+","+chooseLatlng.longitude;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(uri));
            startActivity(intent);
//            if(currentLatLng != null) {
//                map.addMarker(new MarkerOptions().position(chooseLatlng));
//                map.addMarker(new MarkerOptions().position(currentLatLng));
//                new GetDirections().execute(chooseLatlng, currentLatLng);
//                bt_clear.setVisibility(View.VISIBLE);
//            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == callRequest)
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "01266343244"));
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
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
            initList();
            try {
                initMapLicense();
            } catch (Exception e) {
                Utils.openDialog(getContext(),"Đã có lỗi xảy ra. Vui lòng thử lại sau");
                e.printStackTrace();
            }
        }
    }

    public void initList()
    {
        try {
            drawMarker(this.data);
        } catch (Exception e) {
            Utils.openDialog(getContext(),"Đã có lỗi xảy ra. Vui lòng thử lại sau");
            e.printStackTrace();
        }
    }
    private class SearchSystemPitch extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        HashMap<String,String> param;
        public SearchSystemPitch(HashMap<String,String> param)
        {
            this.param = param;
        }
        @Override
        protected String doInBackground(String... params) {

            try {
                okHttpClient = new OkHttpClient();
                Response response =
                        okHttpClient.newCall(NetworkUtils.createPostRequest(API.SearchPitch, this.param)).execute();
                if (response.isSuccessful())
                {
                    return response.body().string().toString();

                }
            } catch (Exception e) {
                Utils.openDialog(getContext(),"Đã có lỗi xảy ra. Vui lòng thử lại sau");
                e.printStackTrace();
                return "failed";
            }
            return "failed";

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("searchPitch",s);
            listSearch = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject object = data.getJSONObject(i);
                    SearchPitchModel systemPitch = new SearchPitchModel();
                    systemPitch.setPitch_description(object.getString("description"));
                    systemPitch.setSystem_id(object.getString("system_id"));
                    systemPitch.setUser_id("1");
                    systemPitch.setUser_name("Owner");
                    systemPitch.setSystem_name(object.getString("name"));
                    systemPitch.setAddress(object.getString("address"));
                    systemPitch.setPitch_name(object.getString("pitch_name"));
                    systemPitch.setPitch_id(object.getString("pitch_id"));
                    systemPitch.setPhone(object.getString("phone"));
                    systemPitch.setLat(object.getString("lat"));
                    systemPitch.setLog(object.getString("log"));
                    systemPitch.setTime_start(object.getString("time_start"));
                    systemPitch.setTime_end(object.getString("time_end"));
                    listSearch.add(systemPitch);
                }
                if (listSearch.size() > 0)
                {            }
                else
                {
                    Utils.openDialog(getContext(),"Không có sân bóng khả dụng trong ngày với khu vực lựa chọn. Hãy thử với khu vực khác");
                }
                progressDialog.dismiss();

            }
            catch (Exception e)
            {
                Utils.openDialog(getContext(),"Đã có lỗi xảy ra. Vui lòng thử lại sau");
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG,param.toString());
            listSystemPitch = new ArrayList<>();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
    }



    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id) {
            case R.id.bt_clear:
            {
                Log.d(TAG,"tmp list "+tmpList.size());
                try {
                    drawMarker(tmpList);
                } catch (Exception e) {
                    Utils.openDialog(getContext(),"Đã có lỗi xảy ra. Vui lòng thử lại sau");
                    e.printStackTrace();
                }

                bt_clear.setVisibility(View.GONE);
                break;
            }
            case R.id.bt_currentLocation :
            {
                double lat = Double.parseDouble(getContext().getSharedPreferences("data", Context.MODE_PRIVATE).getString("lat","0.0"));
                double lng = Double.parseDouble(getContext().getSharedPreferences("data", Context.MODE_PRIVATE).getString("lng","0.0"));
                Log.d("latlng",lat+":"+lng);
                currentLatLng = new LatLng(lat,lng);
                if(lat != 0.0 && lng != 0.0)
                {
                    Utils.moveCamera(new LatLng(lat,lng),"Bạn đang ở đây",13,map);
                }
                break;
            }

//            case R.id.tv_time :
//            {
//                mHour = 0;
//                mMinute=0;
//                TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        mHour = hourOfDay;
//                        mMinute = minute;
//                        sHour = mHour+"";
//                        sMinute= mMinute+"";
//                        if(mHour<10&&mMinute<10)
//                        {
//                            tv_time.setText("0"+mHour+":"+"0"+mMinute);
//                        }
//                        if(mHour<10&&mMinute>10)
//                        {
//                            tv_time.setText("0"+mHour+":"+""+mMinute);
//                        }
//                        if(mHour>10&&mMinute<10)
//                        {
//                            tv_time.setText(""+mHour+":"+"0"+mMinute);
//                        }
//                        currentTime = tv_time.getText().toString();
//                        HashMap<String, String> map = new HashMap<String, String>();
//                        map.put("day", Calendar.getInstance().get(Calendar.YEAR) + "-" +
//                                (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
//                        map.put("time_start", currentTime);
//                        map.put("textlocation", search_box.getSelectedItem().toString());
//
//                        new SearchSystemPitch(map).execute();
//                    }
//                },7,00,true);
//                dialog.setTitle("Chọn giờ bắt đầu bạn muốn");
//                dialog.show();
//                break;
//            }
//            case R.id.bt_search :
//            {
//                map.clear();
//                map.addMarker(new MarkerOptions().position(currentLatLng).title("Bạn ở đây"));
//                dateofweek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
//                HashMap<String,String> body = new HashMap<>();
//                body.put("time_start",tv_time.getText().toString()+":00");
//                body.put("day", "2017-01-01");
//                body.put("textlocation",search_box.getSelectedItem().toString());
//                new SearchSystemPitch(body);
//                break;
//
//
//            }
        }
    }


}

package com.fimo_pitch.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.akexorcist.googledirection.model.Route;
import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.model.Price;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.support.ShowToast;
import com.fimo_pitch.support.TrackGPS;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private RoundedImageView bt_call,bt_order;
    private int callRequest = 1;
    private int gpsRequest = 2;

    private String lat;
    private String lng;
    private String TAG = "DetailActivity";
    private TextView bt_now;
    private GoogleMap map;
    private TrackGPS gps;
    private LatLng currentLatLng;
    private LatLng start,end;
    private List<Route> listRoute;
    private SystemPitch mSystemPitch;
    private LatLng target;
    private JSONObject rawDirections;
    private ArrayList<Price> listMondayPrice,listSaturdayPrice;
    private String priceData;
    private OkHttpClient okHttpClient;
    private TableLayout tableLayout;
    private TextView tvSysName,tvDes;
    private TextView tvPhone;
    private Button btView;
    private String listpitchData;
    private List<String> listName;
    private ArrayList<Pitch> listPitch;

    public DetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSystemPitch = (SystemPitch) getIntent().getSerializableExtra(CONSTANT.SystemPitch_MODEL);
        listMondayPrice = new ArrayList<>();
        listSaturdayPrice = new ArrayList<>();
        listName = new ArrayList<>();
        listPitch = new ArrayList<>();

        if (mSystemPitch !=null) {
            Log.d(TAG,mSystemPitch.toString());
            target = new LatLng(Double.valueOf(mSystemPitch.getLat()), Double.valueOf(mSystemPitch.getLng()));
        }
        setContentView(R.layout.activity_pitch_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        bt_order.setOnClickListener(this);
        bt_call.setOnClickListener(this);
//        bt_now.setOnClickListener(this);

    }
    public void initView() {
        bt_call = (RoundedImageView) findViewById(R.id.bt_call);
        bt_order = (RoundedImageView) findViewById(R.id.bt_order);
        tableLayout = (TableLayout) findViewById(R.id.tb_pricing);
        tvDes = (TextView) findViewById(R.id.tv_desc);
        tvSysName = (TextView) findViewById(R.id.tv_syspitch_name);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        btView = (Button) findViewById(R.id.bt_view);
        btView.setOnClickListener(this);

        if(mSystemPitch !=null)
        {
            tvDes.setText(mSystemPitch.getDescription());
            tvSysName.setText(mSystemPitch.getName());
            tvPhone.setText(mSystemPitch.getPhone());
            mSystemPitch.setPhone("092333244");
        }

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.setGravity(Gravity.CENTER);

        TextView b = new TextView(this);
        TextView c = new TextView(this);
        TextView f = new TextView(this);
        TextView g = new TextView(this);

        b.setText("Khung giờ");
        c.setText("Ngày thường");
        f.setText("Thứ 7, Chủ nhật");
        g.setText("Ghi chú");
        b.setTextSize(12);
        c.setTextSize(12);
        f.setTextSize(12);
        g.setTextSize(12);

        b.setPadding(10,0,10,0);
        c.setPadding(10,0,10,0);
        f.setPadding(10,0,10,0);
        g.setPadding(10,0,10,0);

        b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        c.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        f.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        g.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.addView(b);
        tr.addView(c);
        tr.addView(f);
        tr.addView(g);
        tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        new GetPriceTask().execute();
    }
    private class GetPriceTask extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            Request systemPitchRequest = new Request.Builder()
                    .url(API.GetPrice)
                    .build();
            try {
                okHttpClient = new OkHttpClient();
                okhttp3.Response systemPitchResponse = okHttpClient.newCall(systemPitchRequest).execute();
                if (systemPitchResponse.isSuccessful()) {
                    priceData = systemPitchResponse.body().string();
                    JSONObject result = new JSONObject(priceData);
                    if(result.getString("status").contains("success"))
                    {
                        JSONArray data = result.getJSONArray("data");
//                        Log.d("Data",data.toString()+"");
                        for (int i=0;i<data.length();i++)
                        {
                            JSONObject object = data.getJSONObject(i);
                            Price p = new Price();
                            if(!object.getString("dateofweek").equals(null))
                            p.setDayOfWeek(object.getString("dateofweek"));
                            if(!object.getString("description").equals(null))
                                p.setDescription(object.getString("description"));
                            if(!object.getString("price").equals(null))
                                p.setPrice(object.getString("price")+".000vnđ");
                            if(!object.getString("time_start").equals(null))
                                  if(!object.getString("time_end").equals(null))
                                     p.setTime(object.getString("time_start").substring(0,5)+"-"+object.getString("time_end").substring(0,5));

                            if(p.getDayOfWeek().contains("Mon")) listMondayPrice.add(p);
                            else listSaturdayPrice.add(p);
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                ShowToast.showToastLong(DetailActivity.this,e.getMessage().toString());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            for(int j=0;j<listMondayPrice.size();j++)
            {
                Price monday = listMondayPrice.get(j);
                Price saturday = listSaturdayPrice.get(j);
                addRow(monday.getTime(),monday.getPrice(),saturday.getPrice(),monday.getDescription());
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DetailActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
    }
    public void addRow(String time,String mondayPrice,String weekendPrice,String des)
    {
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.setGravity(Gravity.CENTER);
        TextView b = new TextView(this);
        TextView c = new TextView(this);
        TextView f = new TextView(this);
        TextView g = new TextView(this);

        b.setText(time);
        c.setText(mondayPrice);
        f.setText(weekendPrice);
        g.setText(des);
        b.setTextSize(12);
        c.setTextSize(12);
        f.setTextSize(12);
        g.setTextSize(12);

        b.setPadding(10,5,10,5);
        c.setPadding(10,5,10,5);
        f.setPadding(10,5,10,5);
        g.setPadding(10,5,10,5);

        b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        c.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        f.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        g.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        b.setGravity(Gravity.CENTER);
        f.setGravity(Gravity.CENTER);
        c.setGravity(Gravity.CENTER);
        g.setGravity(Gravity.CENTER);

        tr.addView(b);
        tr.addView(c);
        tr.addView(f);
        tr.addView(g);
        tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

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
            case R.id.bt_view: {
                new GetListPitch().execute();
                break;
            }
        }
    }
    private class GetListPitch extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            Request request = new Request.Builder()
                    .url(API.getAllPitchofSystem+mSystemPitch.getId())
                    .build();
            try {
                okHttpClient = new OkHttpClient();
                okhttp3.Response systemPitchResponse = okHttpClient.newCall(request).execute();
                if (systemPitchResponse.isSuccessful()) {
                    listpitchData = systemPitchResponse.body().string().toString();
                    Log.d(TAG,mSystemPitch.getId()+", "+listpitchData.toString());
                    JSONObject result = new JSONObject(listpitchData);
                    if(result.getString("status").contains("success"))
                    {
                        JSONArray data = result.getJSONArray("data");
                        for (int i=0;i<data.length();i++)
                        {
                            JSONObject object = data.getJSONObject(i);
                            Pitch p = new Pitch();
                            p.setId(object.getString("id"));
                            p.setName(object.getString("name"));
                            p.setType(object.getString("type"));
                            p.setSize(object.getString("size"));
                            p.setDescription(object.getString("description"));
                            p.setPhone(mSystemPitch.getPhone()+"");
                            if(mSystemPitch.getPhone().length()>0)
                                p.setPhone(mSystemPitch.getPhone());
                            else
                            p.setPhone("902932023");
                            listPitch.add(p);
                            listName.add(object.getString("name"));
                        }

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.d("listname",listPitch.size()+"");
            Intent intent = new Intent(DetailActivity.this,ListPitchActivity.class);
            intent.putExtra(CONSTANT.LISTPITCH_DATA, (Serializable) listName);
            intent.putExtra(CONSTANT.LISTPITCH,  (Serializable) listPitch);
            intent.putExtra(CONSTANT.SystemPitch_MODEL,mSystemPitch);
            startActivity(intent);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DetailActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
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
                    lat = target.latitude+"";
                    lng = target.longitude+"";
                }
            }
    }
}


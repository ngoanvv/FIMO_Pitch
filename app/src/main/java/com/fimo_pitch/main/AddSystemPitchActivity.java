package com.fimo_pitch.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.NetworkUtils;
import com.fimo_pitch.support.ShowToast;
import com.fimo_pitch.support.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class AddSystemPitchActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private EditText edt_name,edt_phone,edt_address,edt_lat,edt_lng,edt_des;
    private Button bt_addSystem;
    private String TAG="AddSystemPitchActivity";
    private OkHttpClient client;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_systempitch);

        userModel = (UserModel) getIntent().getSerializableExtra(CONSTANT.KEY_USER);
        client = new OkHttpClient();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Thêm mới 1 hệ thống sân");
        initView();
    }
    public void initView()
    {
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_lat = (EditText) findViewById(R.id.edt_lat);
        edt_lng = (EditText) findViewById(R.id.edt_lng);
        edt_des = (EditText) findViewById(R.id.edt_des);
        bt_addSystem = (Button) findViewById(R.id.bt_addSystem);


        edt_name.setText("Demo");
        edt_phone.setText("Demo");
        edt_address.setText("Demo");
        edt_lat.setText("21.09090");
        edt_lng.setText("108.233");
        edt_des.setText("Demo");


        bt_addSystem.setOnClickListener(this);
        mapFragment = new SupportMapFragment();
        if (mapFragment != null)
        {
            mapFragment.getMapAsync(this);
            getSupportFragmentManager().beginTransaction().add(R.id.choose_map, mapFragment).commit();
        }
    }
    public boolean isFilled()
    {
        if(edt_name.getText().toString().length()==0 || edt_phone.getText().toString().length()==0 ||
                edt_address.getText().toString().length()==0 || edt_lat.getText().toString().length()==0 ||
                edt_lng.getText().toString().length()==0 ||edt_des.getText().toString().length()==0  )
            return false;

        return true;
    }
    class MyTask extends AsyncTask<String,String,String>
    {


        HashMap<String,String> param;
        ProgressDialog progressDialog;

        public MyTask(HashMap<String,String> body)
        {
            this.param=body;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG,s);
            progressDialog.dismiss();
            if(s.contains("success")) {
                Intent intent = new Intent(AddSystemPitchActivity.this,PitchManagementActivity.class);
                startActivity(intent);
                finish();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddSystemPitchActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Response response =
                  client.newCall(NetworkUtils.createPostRequest(API.insertSystemPitch, this.param)).execute();
                String results = response.body().string();
                Log.d("run", results);
                if (response.isSuccessful()) {
                    Log.d("run", results);
                    return results;
                }

            }
            catch (Exception e)
            {
                return e.toString();
            }
            return "failed";
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(googleMap != null)
        {
            mMap = googleMap;
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    Utils.moveCamera(latLng,"",12,mMap);
                    edt_lat.setText(latLng.latitude+"");
                    edt_lng.setText(latLng.longitude+"");
                }
            });
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(AddSystemPitchActivity.this,SystemManagementActivity.class);
            intent.putExtra(CONSTANT.KEY_USER,userModel);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.bt_addSystem :
            {
                if(isFilled())
                {
                    HashMap<String,String> param = new HashMap<String, String>();
                    param.put("name",edt_name.getText().toString());
                    param.put("address",edt_address.getText().toString());
                    param.put("lat",edt_lat.getText().toString());
                    param.put("log",edt_lng.getText().toString());
                    param.put("description",edt_des.getText().toString());
                    param.put("phone",edt_phone.getText().toString());
                    param.put("user_id","3");
                    new MyTask(param).execute();
                }
                else
                {
                    Utils.openDialog(AddSystemPitchActivity.this,"Hãy điền đầy đủ các thông tin");
                }
                break;
            }
        }
    }
}

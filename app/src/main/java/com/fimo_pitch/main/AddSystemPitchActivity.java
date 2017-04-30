package com.fimo_pitch.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.fimo_pitch.support.Utils;
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
    private EditText edtname,edtphone,edtaddress,edtlat,edtlng,edtdes;
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
        edtname = (EditText) findViewById(R.id.edtname);
        edtphone = (EditText) findViewById(R.id.edtphone);
        edtaddress = (EditText) findViewById(R.id.edtaddress);
        edtlat = (EditText) findViewById(R.id.edtlat);
        edtlng = (EditText) findViewById(R.id.edtlng);
        edtdes = (EditText) findViewById(R.id.edtdes);
        bt_addSystem = (Button) findViewById(R.id.btaddSystem);


        edtname.setText("Demo");
        edtphone.setText("Demo");
        edtaddress.setText("Demo");
        edtlat.setText("21.09090");
        edtlng.setText("108.233");
        edtdes.setText("Demo");


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
        if(edtname.getText().toString().length()==0 || edtphone.getText().toString().length()==0 ||
                edtaddress.getText().toString().length()==0 || edtlat.getText().toString().length()==0 ||
                edtlng.getText().toString().length()==0 ||edtdes.getText().toString().length()==0  )
            return false;

        return true;
    }
    class AddSystem extends AsyncTask<String,String,String>
    {


        HashMap<String,String> param;
        ProgressDialog progressDialog;

        public AddSystem(HashMap<String,String> body)
        {
            this.param=body;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG,s);
            progressDialog.dismiss();
            if(!s.contains("status:fail")) {
                Intent intent = new Intent(AddSystemPitchActivity.this,PitchManagementActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                if (s.contains("Name System Pitch Already Exists")) Utils.openDialog(AddSystemPitchActivity.this,"Tên Hệ thống sân bị trùng, chọn tên khác");
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
                  client.newCall(NetworkUtils.createPostRequest(API.InsertSystemPitch, this.param)).execute();
                String results = response.body().string();
                Log.d("run", results);
                if (response.isSuccessful()) {
                    Log.d("run", results);
                    return results;
                }

            }
            catch (Exception e)
            {
                return "failed";
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
                    Utils.moveCamera(latLng,"",11,mMap);
                    edtlat.setText(latLng.latitude+"");
                    edtlng.setText(latLng.longitude+"");
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
            case R.id.btaddSystem :
            {
                if(isFilled())
                {
                    HashMap<String,String> param = new HashMap<String, String>();
                    param.put("name",edtname.getText().toString());
                    param.put("address",edtaddress.getText().toString());
                    param.put("lat",edtlat.getText().toString());
                    param.put("log",edtlng.getText().toString());
                    param.put("description",edtdes.getText().toString());
                    param.put("phone",edtphone.getText().toString());
                    param.put("user_id",userModel.getId());
                    new AddSystem(param).execute();
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

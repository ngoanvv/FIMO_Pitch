package com.fimo_pitch.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fimo_pitch.R;
import com.fimo_pitch.support.ShowToast;
import com.fimo_pitch.support.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddSystemPitchActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private EditText edt_name,edt_phone,edt_address,edt_lat,edt_lng,edt_des;
    private Button bt_addSystem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_systempitch);
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
                    ShowToast.showToastLong(AddSystemPitchActivity.this,"Add Success");
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

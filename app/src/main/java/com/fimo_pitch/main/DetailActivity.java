package com.fimo_pitch.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.fimo_pitch.R;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private RoundedImageView bt_call,bt_order;
    private int permissionCode = 11;
    private String TAG = "DetailActivity";
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_detail);
        initView();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        bt_order.setOnClickListener(this);
        bt_call.setOnClickListener(this);

    }

    public void initView() {
        bt_call = (RoundedImageView) findViewById(R.id.bt_call);
        bt_order = (RoundedImageView) findViewById(R.id.bt_order);

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
                        new String[]{Manifest.permission.CALL_PHONE}, permissionCode);

                break;
            }
            case R.id.bt_order: {
                startActivity(new Intent(DetailActivity.this,PricingActivity.class));
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
        if (requestCode == permissionCode)
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "01266343244"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(mapFragment !=null)
            map=googleMap;

    }
}

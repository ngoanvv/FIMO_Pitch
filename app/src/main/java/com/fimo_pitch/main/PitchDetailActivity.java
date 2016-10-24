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
import android.util.Log;
import android.view.View;

import com.fimo_pitch.R;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.support.TrackGPS;

public class PitchDetailActivity extends AppCompatActivity implements View.OnClickListener {
    RoundedImageView bt_call;
    private int permissionCode = 11;
    private String TAG = "PitchDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_detail);
        ActivityCompat.requestPermissions(PitchDetailActivity.this,
                new String[]{Manifest.permission.CALL_PHONE}, permissionCode);
        initView();

    }

    public void initView() {
        bt_call = (RoundedImageView) findViewById(R.id.bt_call);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_call: {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "029302933"));
                Log.d(TAG,"ok call");
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
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permissionCode)
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                bt_call.setOnClickListener(this);


            }
    }
}

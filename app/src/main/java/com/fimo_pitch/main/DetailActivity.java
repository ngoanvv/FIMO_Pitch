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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.PitchAdapter;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.TrackGPS;
import com.fimo_pitch.support.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private SystemPitch mSystemPitch;
    private LatLng target;
    private RecyclerView recyclerView;
    private OkHttpClient okHttpClient;
    private TextView tvSysName,tvDes,tvAddress,tvOwner;
    private TextView tvPhone;
    private String listpitchData;
    private List<String> listName;
    private ArrayList<Pitch> listPitch;
    private PitchAdapter adapter;
    private UserModel userModel;
    private ListView listView;
    private RoundedImageView bt_location;

    public DetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSystemPitch = (SystemPitch) getIntent().getSerializableExtra(CONSTANT.SystemPitch_MODEL);
        userModel = (UserModel) getIntent().getSerializableExtra(CONSTANT.KEY_USER);

        listName = new ArrayList<>();
        listPitch = new ArrayList<>();

        if (mSystemPitch !=null) {
            Log.d(TAG,mSystemPitch.toString());
            target = new LatLng(Double.valueOf(mSystemPitch.getLat()), Double.valueOf(mSystemPitch.getLng()));
        }
        setContentView(R.layout.activity_pitch_detail);
        Utils.setupAnimations(this);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        initView();


        new GetListPitch().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();


//        bt_now.setOnClickListener(this);

    }
    public void initView() {
        bt_call = (RoundedImageView) findViewById(R.id.bt_call);
        bt_order = (RoundedImageView) findViewById(R.id.bt_order);
        bt_location = (RoundedImageView) findViewById(R.id.bt_location);


        tvDes = (TextView) findViewById(R.id.tv_desc);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvSysName = (TextView) findViewById(R.id.tv_syspitch_name);
        tvOwner = (TextView) findViewById(R.id.tvOwner);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tvPhone = (TextView) findViewById(R.id.tvPhone);


        bt_location.setOnClickListener(this);
        bt_order.setOnClickListener(this);
        bt_call.setOnClickListener(this);

        if(mSystemPitch !=null)
        {
            tvDes.setText(mSystemPitch.getDescription());
            tvSysName.setText(mSystemPitch.getName());
            tvPhone.setText(mSystemPitch.getPhone());
            tvOwner.setText(mSystemPitch.getOwnerName());
            tvAddress.setText(mSystemPitch.getAddress());
            mSystemPitch.setPhone("092333244");
        }

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
            case R.id.bt_location: {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Float.valueOf(mSystemPitch.getLat()), Float.valueOf(mSystemPitch.getLng()));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + mSystemPitch.getLat()  + ">,<" + mSystemPitch.getLng() + ">?q=<" + mSystemPitch.getLat()  + ">,<" + mSystemPitch.getLng() + ">(" + mSystemPitch.getName() + ")"));
                startActivity(intent);
                break;
            }
        }
    }
    private class GetListPitch extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            Request request = new Request.Builder()
                    .url(API.GetAllPitchofSystem+mSystemPitch.getId())
                    .build();
            try {
                okHttpClient = new OkHttpClient();
                okhttp3.Response systemPitchResponse = okHttpClient.newCall(request).execute();
                if (systemPitchResponse.isSuccessful()) {
                    listpitchData = systemPitchResponse.body().string().toString();
                    Log.d(TAG,mSystemPitch.getId()+", "+listpitchData.toString());
                    return listpitchData;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
            return "failed";

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s != "failed")
            {
                Log.d("list pitch",s);
                try {
                    JSONObject result = new JSONObject(s);
                    if (result.getString("status").contains("success")) {
                        JSONArray data = result.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            Pitch p = new Pitch();
                            p.setId(object.getString("id"));
                            p.setName(object.getString("name"));
                            p.setType(object.getString("type"));
                            p.setSize(object.getString("size"));
                            p.setDescription(object.getString("description"));
                            p.setPhone(mSystemPitch.getPhone() + "");
                            if (mSystemPitch.getPhone().length() > 0)
                                p.setPhone(mSystemPitch.getPhone());
                            else
                                p.setPhone("902932023");
                            listPitch.add(p);
                            listName.add(object.getString("name"));
                        }
                    }
                    LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(DetailActivity.this);
                    mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
                    adapter = new PitchAdapter(DetailActivity.this, listPitch,listName,mSystemPitch,userModel);
                    recyclerView.setAdapter(adapter);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listPitch = new ArrayList<>();
            listName = new ArrayList<>();
            progressDialog = new ProgressDialog(DetailActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
            Log.d(TAG,"get list picch");
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
    }
}


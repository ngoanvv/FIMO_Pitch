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
import android.widget.TextView;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SystemManagementActivity extends AppCompatActivity {
    private UserModel userModel;
    private TextView tvname,tvphone,tvaddress,tvlat,tvlng,tvdes;
    private RoundedImageView btAdd;
    private OkHttpClient okHttpClient;
    private Button btEdit,btDelete;
    private SystemPitch mSystemPitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_management);
        userModel = (UserModel) getIntent().getSerializableExtra(CONSTANT.KEY_USER);
        mSystemPitch = (SystemPitch) getIntent().getSerializableExtra(CONSTANT.SystemPitch_MODEL);

        tvname = (TextView) findViewById(R.id.tv_name);
        tvaddress = (TextView) findViewById(R.id.tv_address);
        tvphone = (TextView) findViewById(R.id.tv_phone);
        tvaddress = (TextView) findViewById(R.id.tv_address);
        tvdes = (TextView) findViewById(R.id.tv_des);
        btAdd = (RoundedImageView) findViewById(R.id.bt_addSystem2);
        btEdit = (Button) findViewById(R.id.bt_edit);
        btDelete = (Button) findViewById(R.id.bt_delete);

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SystemManagementActivity.this,EditSystemPitchActivity.class);
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                intent.putExtra(CONSTANT.SystemPitch_MODEL,mSystemPitch);
                startActivity(intent);
                finish();
            }
        });
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SystemManagementActivity.this,AddSystemPitchActivity.class);
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                startActivity(intent);
            }
        });
        new GetListSytembyId(userModel.getId()).execute();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Quản lý hệ thống sân");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private class GetListSytembyId extends AsyncTask<String,Void,String> {
        String id;
        public  GetListSytembyId(String userId)
        {
            this.id= userId;
        }
        ProgressDialog progressDialog;
        @Override
        protected String doInBackground(String... params) {
            try {
                okHttpClient = new OkHttpClient();
                Request request = NetworkUtils.createGetRequest(API.GetListSystemById+this.id);
                Response systemPitchResponse = okHttpClient.newCall(request).execute();
                if (systemPitchResponse.isSuccessful()) {
                    String data = systemPitchResponse.body().string().toString();
                    Log.d("hello",data.toString());
                    return data;
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
            if(!s.contains("failed"))
            {
                try {
                    JSONObject result = new JSONObject(s);
                    if (result.getString("status").contains("success")) {
                        JSONArray data = result.getJSONArray("data");
                        if(data.length()>0)
                        {
                            JSONObject object = data.getJSONObject(0);
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

                            tvaddress.setText(systemPitch.getAddress());
                            tvname.setText(systemPitch.getName());
                            tvdes.setText(systemPitch.getDescription());
                            tvphone.setText(systemPitch.getPhone());
                            btAdd.setVisibility(View.GONE);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            progressDialog.dismiss();

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SystemManagementActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
    }
}

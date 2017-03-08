package com.fimo_pitch.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.PriceAdapter;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.model.Price;
import com.fimo_pitch.model.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PriceManagementActivity extends AppCompatActivity{
    private RoundedImageView btAdd;
    private RecyclerView recyclerView;
    private PriceAdapter adapter;
    private ArrayList<Price> listPrice;
    private OkHttpClient okHttpClient;
    private String listpitchData;
    private UserModel userModel;
    private Spinner spinnerPitch;
    private List<String> listName;
    private ArrayList<Pitch> listPitch;
    private String idPitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_management);
        userModel = (UserModel) getIntent().getSerializableExtra(CONSTANT.KEY_USER);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Quản lý giá tiền, khung giờ ");
        initList();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new GetListPitch().execute();
    }

    public void initList()
    {
        listPrice = new ArrayList<>();
        listName = new ArrayList<>();
        listPitch = new ArrayList<>();
    }
    
    public void initView()
    {

        btAdd = (RoundedImageView) findViewById(R.id.bt_addPitch);
        spinnerPitch = (Spinner) findViewById(R.id.spnPitch);


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PriceManagementActivity.this,AddPriceActivity.class);
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                startActivity(intent);
                finish();
            }
        });


    }

    private class GetListPrice extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        String id;
        String pitchName;
        public GetListPrice(String x,String pitch)
        {
            this.pitchName=pitch;
            id=x;
        }
        @Override
        protected String doInBackground(String... params) {
            Request request = new Request.Builder()
                    .url(API.GetPrice+id)
                    .build();
            Log.d("url ",API.GetPrice+id);
            try {
                okHttpClient = new OkHttpClient();
                okhttp3.Response systemPitchResponse = okHttpClient.newCall(request).execute();
                if (systemPitchResponse.isSuccessful()) {
                    listpitchData = systemPitchResponse.body().string().toString();
                    JSONObject result = new JSONObject(listpitchData);
                    if(result.getString("status").contains("success"))
                    {
                        listPrice.clear();
                        JSONArray data = result.getJSONArray("data");
                        for (int i=0;i<data.length();i++)
                        {
                            JSONObject object = data.getJSONObject(i);
                            Price p = new Price();
                            p.setId(object.getString("id"));
                            p.setTime(object.getString("time_start")+"-"+object.getString("time_end"));
                            p.setDayOfWeek(object.getString("typedate"));
                            p.setPrice(object.getString("price"));
                            p.setSystemId(object.getString("system_id"));
                            p.setPitchId(id+"");
                            p.setPitchName(pitchName);
                            p.setDescription(object.getString("description"));
                            listPrice.add(p);
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
            recyclerView = (RecyclerView) findViewById(R.id.list_pitch);
            LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(PriceManagementActivity.this); // (Context context)
            mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
            adapter = new PriceAdapter(PriceManagementActivity.this,listPrice);
            recyclerView.setAdapter(adapter);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PriceManagementActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
    }
    private class GetListPitch extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            Request request = new Request.Builder()
                    .url(API.GetAllPitchofSystem+1)
                    .build();
            try {
                okHttpClient = new OkHttpClient();
                okhttp3.Response systemPitchResponse = okHttpClient.newCall(request).execute();
                if (systemPitchResponse.isSuccessful()) {
                    listpitchData = systemPitchResponse.body().string().toString();
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(PriceManagementActivity.this,android.R.layout.simple_list_item_1,listName);
            spinnerPitch.setAdapter(adapter);
            spinnerPitch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    new GetListPrice(listPitch.get(position).getId(), listPitch.get(position).getName()).execute();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listPitch = new ArrayList<>();
            progressDialog = new ProgressDialog(PriceManagementActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
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
}

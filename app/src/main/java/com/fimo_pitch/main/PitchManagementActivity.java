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
import com.fimo_pitch.adapter.PitchManagementAdapter;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PitchManagementActivity extends AppCompatActivity {
    private RoundedImageView btAddPitch;
    private RecyclerView recyclerView;
    private PitchManagementAdapter adapter;
    private ArrayList<Pitch> listPitch;
    private OkHttpClient okHttpClient;
    private String listpitchData;
    private UserModel userModel;
    private Spinner spn_listSystem;
    private String TAG="PitchManagementActivity";
    private ArrayList<SystemPitch> listSystem;
    private List<String> listName;
    private SystemPitch mSystemPitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userModel = (UserModel) getIntent().getSerializableExtra(CONSTANT.KEY_USER);
        mSystemPitch = (SystemPitch) getIntent().getSerializableExtra(CONSTANT.SystemPitch_MODEL);
        listSystem = new ArrayList();
        listName = new ArrayList<>();
        try {
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Quản lý sân ");
        try {
            initList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void initList() throws Exception
    {
        listPitch = new ArrayList<>();

        new GetListPitch(mSystemPitch.getId()).execute();
    }
    private class GetListPitch extends AsyncTask<String,Void,String>{
        ProgressDialog progressDialog;
        String sId;
        public GetListPitch(String id)
        {
            this.sId = id;
        }

        @Override
        protected String doInBackground(String... params) {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            Request request = new Request.Builder()
                    .url(API.GetAllPitchofSystem+mSystemPitch.getId())
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "b9494f39-8e39-7533-1896-281ee653703b")
                    .build();
            try {
                okHttpClient = new OkHttpClient();
                okhttp3.Response systemPitchResponse = okHttpClient.newCall(request).execute();
                if (systemPitchResponse.isSuccessful()) {
                    listpitchData = systemPitchResponse.body().string().toString();
                    Log.d("data",listpitchData.toString());
                    JSONObject result = new JSONObject(listpitchData);
                    if(result.getString("status").contains("success"))
                    {
                        listPitch.clear();
                        JSONArray data = result.getJSONArray("data");
                        for (int i=0;i<data.length();i++)
                        {
                            JSONObject object = data.getJSONObject(i);
                            Pitch p = new Pitch();
                            if(!object.getString("id").equals(null))
                                p.setId(object.getString("id"));
                            if(!object.getString("name").equals(null))
                                p.setName(object.getString("name"));
                            if(!object.getString("type").equals(null))
                                p.setType(object.getString("type"));
                            if(!object.getString("size").equals(null))
                                p.setSize(object.getString("size"));
                            if(!object.getString("description").equals(null))
                                p.setDescription(object.getString("description"));
                            if(!object.getString("system_id").equals(null))
                                p.setSystemId(object.getString("system_id"));
                            listPitch.add(p);
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
            LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(PitchManagementActivity.this); // (Context context)
            mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
            adapter = new PitchManagementAdapter(PitchManagementActivity.this,listPitch);
            recyclerView.setAdapter(adapter);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PitchManagementActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
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
                        listSystem.clear();
                        JSONArray data = result.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++)
                        {
                            JSONObject object = data.getJSONObject(i);
                            SystemPitch p = new SystemPitch();
                            listName.add(object.getString("name"));
                            p.setId(object.getString("id"));
                            p.setId(object.getString("id"));
                            listSystem.add(p);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PitchManagementActivity.this,android.R.layout.simple_list_item_1,listName);
                        spn_listSystem.setAdapter(adapter);
                        spn_listSystem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                new GetListPitch(listSystem.get(position).getId()).execute();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }
                    if(listSystem.size()>0) new GetListPitch(listSystem.get(0).getId()).execute();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            progressDialog.dismiss();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PitchManagementActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
    }
    public void initView() throws Exception
    {
        setContentView(R.layout.activity_pitch_management);
        btAddPitch = (RoundedImageView) findViewById(R.id.bt_addPitch);
        spn_listSystem = (Spinner) findViewById(R.id.spn_system);
        btAddPitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PitchManagementActivity.this,AddPitchActivity.class);
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                startActivity(intent);
            }
        });
        listName.add(mSystemPitch.getName());
        listSystem.add(mSystemPitch);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PitchManagementActivity.this,android.R.layout.simple_list_item_1,listName);
        spn_listSystem.setAdapter(adapter);
        spn_listSystem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new GetListPitch(listSystem.get(position).getId()).execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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

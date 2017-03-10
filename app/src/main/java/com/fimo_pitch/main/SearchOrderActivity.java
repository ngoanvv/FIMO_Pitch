package com.fimo_pitch.main;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.OrderAdapter;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.model.TimeTable;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.NetworkUtils;
import com.fimo_pitch.support.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class SearchOrderActivity extends AppCompatActivity implements View.OnClickListener,OrderAdapter.OnCallEvent {
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private ArrayList<Pitch> listPitches;
    private TextView tv_timeFilter,tv_dateFilter;
    private Spinner spinner;
    private List<String> listName;
    private static String TAG=SearchOrderActivity.class.getName();
    private SystemPitch mSystemPitch;
    private OkHttpClient okHttpClient;
    private String listPriceData="";
    private RoundedImageView btSearch;
    private String pitchId="1";
    private ArrayList<TimeTable> listTime;
    private String listimeData;
    private int pitchPos=1;
    private int callRequest=1;
    private String phoneNumber;
    private String dateOfWeek= String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
    private Pitch crPitch;
    private String crDay="2017-01-12";
    private UserModel userModel;
    private TextView mText;
    private ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSystemPitch = (SystemPitch) getIntent().getSerializableExtra(CONSTANT.SystemPitch_MODEL);
        listName = (List<String>) getIntent().getSerializableExtra(CONSTANT.LISTPITCH_DATA);
        listPitches = (ArrayList<Pitch>) getIntent().getSerializableExtra(CONSTANT.LISTPITCH);
        userModel = (UserModel) getIntent().getSerializableExtra(CONSTANT.KEY_USER);
        if(listPitches != null && listPitches.size()>0)
        crPitch = listPitches.get(0);
        setContentView(R.layout.activity_list_pitch);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Tìm kiếm sân trống");
        initView();
    }
    public void initView()
    {

        tv_dateFilter = (TextView) findViewById(R.id.date_filter);
        spinner = (Spinner) findViewById(R.id.pitch_filter);
        btSearch = (RoundedImageView) findViewById(R.id.btsearch);
        mText = (TextView) findViewById(R.id.mText);

        btSearch.setOnClickListener(this);
        listTime = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.list_pitch);
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(SearchOrderActivity.this); // (Context context)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
        adapter = new OrderAdapter(SearchOrderActivity.this,listTime,userModel);
        adapter.setOnCallEvent(SearchOrderActivity.this);
        recyclerView.setAdapter(adapter);
        tv_dateFilter.setText(Calendar.getInstance().get(Calendar.YEAR)+"-"+(Calendar.getInstance().get(Calendar.MONTH)+1)+"-"+Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        tv_dateFilter.setOnClickListener(this);

        if(listName.size()>0&&listPitches.size()>0) {
            dataAdapter = new ArrayAdapter<String>(SearchOrderActivity.this, android.R.layout.simple_spinner_dropdown_item, listName);
            spinner.setAdapter(dataAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("spinner", listPitches.get(position).getId() + "");
                    pitchId = listPitches.get(position).getId();
                    crPitch = listPitches.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
    private class GetTime extends AsyncTask<String,Void,String> implements OrderAdapter.OnCallEvent  {
        ProgressDialog progressDialog;
        String id;
        String pitchName;
        HashMap<String,String> param;
        public GetTime(String name,HashMap<String,String> body)
        {
            this.param = body;
            this.pitchName=name;
            id=pitchId;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SearchOrderActivity.this);
            progressDialog.setMessage("Đang thao tác");
//            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                okHttpClient = new OkHttpClient();
                Response response =
                        okHttpClient.newCall(NetworkUtils.createPostRequest(API.GetTime, this.param)).execute();
                String results = response.body().string();
                Log.d("run", results);
                if (response.isSuccessful()) {
                    Log.d("run", results);
                    return results;
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "failed";
            }
            return "failed";

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.d(TAG,s);
            if(s != "failed")
            try {
                JSONObject result = new JSONObject(s);
                if (result.getString("status").contains("success")) {
                    listTime.clear();
                    JSONArray data = result.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        TimeTable p = new TimeTable();
                        p.setId(object.getString("id"));
                        p.setStart_time(object.getString("time_start"));
                        p.setEnd_time(object.getString("time_end"));
                        p.setType(object.getString("typedate"));
                        p.setPrice(object.getString("price"));
                        p.setSystemId(object.getString("system_id"));
                        p.setPitchId(object.getString("pitch_id"));
                        p.setDay(crDay);
                        p.setDescription(object.getString("description"));
                        listTime.add(p);
                    }

                }
                recyclerView = (RecyclerView) findViewById(R.id.list_pitch);
                LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(SearchOrderActivity.this); // (Context context)
                mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
                adapter = new OrderAdapter(SearchOrderActivity.this, listTime,userModel);
                adapter.setOnCallEvent(this);
                recyclerView.setAdapter(adapter);
                if(listTime.size()>0) mText.setVisibility(View.GONE);
                else mText.setVisibility(View.VISIBLE);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCallEvent(String number) {
            if(number.contains("null"))
                Utils.openDialog(SearchOrderActivity.this,"Không có số điện thoại khả dụng");
            else
            {
                phoneNumber = number;
                ActivityCompat.requestPermissions(SearchOrderActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, callRequest);
            }
        }
    }
    @Override
    public void onCallEvent(String number) {

        if(number.contains("null"))
            Utils.openDialog(SearchOrderActivity.this,"Không có số điện thoại khả dụng");
        else
        {
            phoneNumber = number;
            ActivityCompat.requestPermissions(SearchOrderActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, callRequest);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == callRequest)
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
    }

    private class GetTimeTable extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        Pitch mPitch;
        public GetTimeTable(Pitch p)
        {
            this.mPitch = p;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SearchOrderActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
            listTime = new ArrayList<>();
        }
        @Override
        protected String doInBackground(String... params) {
            return listimeData;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public void showDatePicker()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(SearchOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (view.isShown())
                {
                    tv_dateFilter.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                    Log.d("dateofweek",dateOfWeek.toString());
                    crDay =year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                }
            }
        }, 2017,Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
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
        int i=v.getId();
        switch (i)
        {
            case R.id.date_filter :
            {
                showDatePicker();
                break;
            }
            case R.id.btsearch :
            {
//                new GetTimeTable(crPitch).execute(pitchId,mSystemPitch.getId(),"Mon");
                HashMap<String,String> params = new HashMap<>();
                params.put("pitch_id","1");
                params.put("day",crDay);
                Log.d(TAG,crDay);
                new GetTime(crPitch.getName(),params).execute();
                break;
            }
        }
    }
}

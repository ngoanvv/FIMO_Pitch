package com.fimo_pitch.main;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.TimePicker;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.PitchAdapter;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.model.Price;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.model.TimeTable;
import com.fimo_pitch.support.TrackGPS;
import com.fimo_pitch.support.Utils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.w3c.dom.ls.LSInput;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.R.id.list;

public class ListPitchActivity extends AppCompatActivity implements View.OnClickListener,PitchAdapter.OnCallEvent {
    private RecyclerView recyclerView;
    private PitchAdapter adapter;
    private ArrayList<Pitch> listPitches;
    private TextView tv_timeFilter,tv_dateFilter;
    private Spinner spinner;
    private List<String> listName;
    private static String TAG=ListPitchActivity.class.getName();
    private SystemPitch mSystemPitch;
    private OkHttpClient okHttpClient;
    private String listpitchData="";
    private RoundedImageView btSearch;
    private String pitchId="1";
    private ArrayList<TimeTable> listTime;
    private String listimeData;
    private int pitchPos=1;
    private int callRequest=1;
    private String phoneNumber;
    private String dateOfWeek= String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
    private Pitch crPitch;
    private TextView mText;
    private ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSystemPitch = (SystemPitch) getIntent().getSerializableExtra(CONSTANT.SystemPitch_MODEL);
        listName = (List<String>) getIntent().getSerializableExtra(CONSTANT.LISTPITCH_DATA);
        listPitches = (ArrayList<Pitch>) getIntent().getSerializableExtra(CONSTANT.LISTPITCH);

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
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(ListPitchActivity.this); // (Context context)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
        adapter = new PitchAdapter(ListPitchActivity.this,listTime);
        adapter.setOnCallEvent(ListPitchActivity.this);
        recyclerView.setAdapter(adapter);

        tv_dateFilter.setOnClickListener(this);

        if(listName.size()>0&&listPitches.size()>0) {
            dataAdapter = new ArrayAdapter<String>(ListPitchActivity.this, android.R.layout.simple_spinner_dropdown_item, listName);
            spinner.setAdapter(dataAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("spinner", listPitches.get(position).getId() + "");
                    pitchId = listPitches.get(position).getid();
                    crPitch = listPitches.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    @Override
    public void onCallEvent(String number) {

        if(number.contains("null"))
            Utils.openDialog(ListPitchActivity.this,"Không có số điện thoại khả dụng");
        else
        {
            phoneNumber = number;
            ActivityCompat.requestPermissions(ListPitchActivity.this,
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
            progressDialog = new ProgressDialog(ListPitchActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();

            listTime = new ArrayList<>();
        }
        @Override
        protected String doInBackground(String... params) {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "pitchid="+params[0]+"&systemPID="+params[1]+"&dateofweek="+params[2]);


            Request request = new Request.Builder()
                    .url("https://pitchwebservice.herokuapp.com/pitchs/getTimeTableOfDay/")
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "12ceab54-a66d-5b81-85d9-06b43cee4f40")
                    .build();
            try {
                okHttpClient = new OkHttpClient();
                okhttp3.Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    listimeData = response.body().string().toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listimeData;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject result = null;
            try {
                result = new JSONObject(s);
                Log.d("time table",s.toString());
                if(result.getString("status").contains("success")) {
                    JSONArray data = result.getJSONArray("data");
                    if (data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            TimeTable p = new TimeTable();
                            p.setId(mPitch.getid());
                            p.setName(mPitch.getName());
                            p.setType(mPitch.getType());
                            p.setSize(mPitch.getSize());
                            p.setDescription(mPitch.getDescription());
                            p.setEnd_time(object.getString("time_end"));
                            p.setStart_time(object.getString("time_start"));
                            if (mSystemPitch.getPhone().length() > 0)
                                p.setPhone(mSystemPitch.getPhone());
                            listTime.add(p);
                        }
                        progressDialog.dismiss();
                        mText.setVisibility(View.INVISIBLE);
                        adapter = new PitchAdapter(ListPitchActivity.this, listTime);
                        adapter.setOnCallEvent(ListPitchActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                }
                else
                {
                    mText.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    private class BookPitchTask extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        Pitch mPitch;
        public BookPitchTask(Pitch p)
        {
            this.mPitch = p;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ListPitchActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();

            listTime = new ArrayList<>();
        }
        @Override
        protected String doInBackground(String... params) {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "pitchid="+params[0]+"&systemPID="+params[1]+"&dateofweek="+params[2]);


            Request request = new Request.Builder()
                    .url("https://pitchwebservice.herokuapp.com/pitchs/getTimeTableOfDay/")
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "12ceab54-a66d-5b81-85d9-06b43cee4f40")
                    .build();
            try {
                okHttpClient = new OkHttpClient();
                okhttp3.Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    listimeData = response.body().string().toString();
                    JSONObject result = new JSONObject(listimeData);
                    Log.d("time table",listimeData.toString());
                    if(result.getString("status").contains("success"))
                    {
                        JSONArray data = result.getJSONArray("data");
                        for (int i=0;i<data.length();i++)
                        {
                            JSONObject object = data.getJSONObject(i);
                            TimeTable p = new TimeTable();
                            p.setId(mPitch.getid());
                            p.setName(mPitch.getName());
                            p.setType(mPitch.getType());
                            p.setSize(mPitch.getSize());
                            p.setDescription(mPitch.getDescription());
                            p.setEnd_time(object.getString("time_end"));
                            p.setStart_time(object.getString("time_start"));
                            if(mSystemPitch.getPhone().length()>0)
                                p.setPhone(mSystemPitch.getPhone());
                            listTime.add(p);
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
            adapter = new PitchAdapter(ListPitchActivity.this,listTime);
            adapter.setOnCallEvent(ListPitchActivity.this);
            recyclerView.setAdapter(adapter);
        }
    }

    public void showDatePicker()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(ListPitchActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (view.isShown())
                {
                    tv_dateFilter.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                    Log.d("dateofweek",dateOfWeek.toString());
                }
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
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

                break;
            }
        }
    }
}

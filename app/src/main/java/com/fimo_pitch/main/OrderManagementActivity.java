package com.fimo_pitch.main;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.fimo_pitch.API;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.ManageOrderAdapter;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.model.Order;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.support.NetworkUtils;
import com.fimo_pitch.support.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderManagementActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Order> listOrder;
    private ArrayList<Pitch> listPitch;
    private OkHttpClient okHttpClient;
    private ManageOrderAdapter manageOrderAdapter;
    private RecyclerView recyclerView;
    private TextView dayFilter;
    private Spinner pitchFilter;
    private RoundedImageView btSearch;
    private Pitch crPitch;
    private String listpitchData;
    private List<String> listName;
    private String pitchId,day;
    private String TAG=OrderManagementActivity.class.getName();
    private String crDay=Calendar.getInstance().get(Calendar.YEAR)+"-"+
                         Calendar.getInstance().get(Calendar.MONTH)+"-"+
                         Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Quản lý yêu cầu đặt sân");
        initView();
        initList();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void initList()
    {
        listOrder = new ArrayList<>();
        listPitch = new ArrayList<>();
        listName = new ArrayList<>();
        new GetListPitch().execute();
    }
    public void initView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.list_manage);
        dayFilter = (TextView) findViewById(R.id.date_filter);
        btSearch = (RoundedImageView) findViewById(R.id.btSearch);
        pitchFilter = (Spinner) findViewById(R.id.pitch_filter) ;
        dayFilter.setOnClickListener(this);
        btSearch.setOnClickListener(this);
    }
    class GetOrders extends AsyncTask<String,String,String>
    {
        HashMap<String,String> param;
        ProgressDialog progressDialog;
        int position;
        public GetOrders(HashMap<String,String> body)
        {
            this.param=body;
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Response response = okHttpClient.newCall(NetworkUtils.createPostRequest(API.GetOrders,this.param)).execute();
                if (response.isSuccessful()) {
                    String results = response.body().string();
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
            if(s != "failed") {
                listOrder = new ArrayList<>();
                try {
                    JSONObject result = new JSONObject(s);
                    if(result.getString("status").contains("success"))
                    {
                        JSONArray data = result.getJSONArray("data");
                        for (int i=0;i<data.length();i++)
                        {
                            JSONObject object = data.getJSONObject(i);
                            Order p = new Order();
                            p.setId(object.getString("order_id"));
                            p.setUserName(object.getString("name"));
                            p.setUserPhone(object.getString("phone"));
                            p.setTime_end(object.getString("time_end"));
                            p.setTime_start(object.getString("time_start"));
                            p.setUserId("user_id");
                            p.setDay(day);
                            Log.d(TAG,p.toString());
                            listOrder.add(p);
                        }
                    }
                    if(listOrder.size()>0) {
                        manageOrderAdapter = new ManageOrderAdapter(OrderManagementActivity.this, listOrder);
                        recyclerView.setLayoutManager(new LinearLayoutManager(OrderManagementActivity.this));
                        recyclerView.setAdapter(manageOrderAdapter);
                        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(OrderManagementActivity.this); // (Context context)
                        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
                        Utils.openDialog(OrderManagementActivity.this,"Có "+listOrder.size()+" kết quả");

                    }
                    else {
                        Utils.openDialog(OrderManagementActivity.this,"Không có yêu cầu nào !");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            else
            {
                Utils.openDialog(OrderManagementActivity.this,"Đã có lỗi xảy ra ");
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OrderManagementActivity.this);
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
                    Log.d("run",listpitchData);
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
            if (s != "failed") {
                JSONObject result = null;
                try {
                    result = new JSONObject(s);
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
                    if(listPitch.size()>0) {
                        pitchId = listPitch.get(0).getId();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrderManagementActivity.this, R.layout.spinner_item2, listName);
                        pitchFilter.setAdapter(adapter);
                        pitchFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                pitchId = listPitch.get(position).getId();
                                HashMap<String, String> body = new HashMap<>();
                                body.put("day", crDay);
                                body.put("pitch_id", pitchId);
                                Log.d(TAG, day + "-" + pitchId);
                                new GetOrders(body).execute();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        if (listPitch.size() > 0) {
                            pitchId = listPitch.get(0).getId();
                            HashMap<String, String> body = new HashMap<>();
                            body.put("day", crDay);
                            body.put("pitch_id", pitchId);
                            Log.d(TAG, day + "-" + pitchId);
                            new GetOrders(body).execute();
                        }
                    }
                    else
                    {
                        Utils.openDialog(OrderManagementActivity.this,"Không có yêu cầu nào !");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.openDialog(OrderManagementActivity.this,"Đã có lỗi xảy ra, thử lại sau");

                }

            }
            else
            {
                Utils.openDialog(OrderManagementActivity.this,"Đã có lỗi xảy ra, thử lại sau");
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listPitch = new ArrayList<>();
            progressDialog = new ProgressDialog(OrderManagementActivity.this);
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
    @Override
    public void onClick(final View v) {
        switch (v.getId())
        {
            case R.id.date_filter :
            {
                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderManagementActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                    day = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                                    dayFilter.setText(day);
                                    crDay=day;

                                HashMap<String,String> body = new HashMap<>();
                                body.put("day",crDay);
                                body.put("pitch_id",pitchId);
                                Log.d(TAG,day+"-"+pitchId);
                                new GetOrders(body).execute();
                            }
                        },2017, Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();


                break;
            }
            case R.id.btSearch:
            {
                HashMap<String,String> body = new HashMap<>();
                body.put("day",crDay);
                body.put("pitch_id",pitchId);
                Log.d(TAG,day+"-"+pitchId);
                new GetOrders(body).execute();
            }
        }
    }
}

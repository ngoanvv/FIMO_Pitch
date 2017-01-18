package com.fimo_pitch.main;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.adapter.OrderAdapter;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.model.Order;
import com.fimo_pitch.model.Pitch;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Order> listOrder;
    private ArrayList<Pitch> listPitch;
    private OkHttpClient okHttpClient;
    private OrderAdapter orderAdapter;
    private RecyclerView recyclerView;
    private TextView dayFilter;
    private Spinner pitchFilter;
    private RoundedImageView btSearch;
    private Pitch crPitch;
    private String listpitchData;
    private List<String> listName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Quản lý yêu cầu đặt sân");

        initList();
        initView();

    }
    public void initList()
    {
        listOrder = new ArrayList<>();
        listPitch = new ArrayList<>();
        listName = new ArrayList<>();

    }
    public void initView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.list_manage);
        dayFilter = (TextView) findViewById(R.id.date_filter);
        pitchFilter = (Spinner) findViewById(R.id.pitch_filter);
        btSearch = (RoundedImageView) findViewById(R.id.btSearch);

        dayFilter.setOnClickListener(this);
        btSearch.setOnClickListener(this);

        new GetListPitch().execute();

        pitchFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                crPitch = listPitch.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listOrder.add( new Order());
        listOrder.add( new Order());
        listOrder.add( new Order());
        listOrder.add( new Order());
        listOrder.add( new Order());

        orderAdapter = new OrderAdapter(OrderActivity.this, listOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderActivity.this));
        recyclerView.setAdapter(orderAdapter);
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(OrderActivity.this); // (Context context)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);


    }


    private class GetListPitch extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "systemPID="+1);
            Request request = new Request.Builder()
                    .url("https://pitchwebservice.herokuapp.com/pitch/getAllPitchsOfSystem/1")
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "b9494f39-8e39-7533-1896-281ee653703b")
                    .build();
            try {
                okHttpClient = new OkHttpClient();
                okhttp3.Response systemPitchResponse = okHttpClient.newCall(request).execute();
                if (systemPitchResponse.isSuccessful()) {
                    listpitchData = systemPitchResponse.body().string().toString();

//                    Log.d(TAG,mSystemPitch.getId()+", "+listpitchData.toString());

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
//                            if(mSystemPitch.getPhone().length()>0)
//                                p.setPhone(mSystemPitch.getPhone());
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
            if(listName.size()>0) {
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(OrderActivity.this, android.R.layout.simple_spinner_dropdown_item, listName);
                pitchFilter.setAdapter(dataAdapter);
            }
            else
            {
                listName.add("Sân 1 demo ");
                listName.add("Sân 2 demo");
                listName.add("Sân 3 demo");

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(OrderActivity.this, android.R.layout.simple_spinner_dropdown_item,listName);
                pitchFilter.setAdapter(dataAdapter);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OrderActivity.this);
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
    public void onClick(View v) {

    }
}

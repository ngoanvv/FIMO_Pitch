package com.fimo_pitch.main;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddPriceActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edt_startTime,edt_endTime,edt_price,edt_description;
    private List<String> listDate;
    private Spinner spinnerDate,spinnerPitch;
    private Button button;
    private String TAG="AddPriceActivity";
    private OkHttpClient client;
    private UserModel userModel;
    private OkHttpClient okHttpClient;
    private String listpitchData;
    private ArrayList<Pitch> listPitch;
    private List<String> listName;
    private String idPitch;
    private String typeofDate="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_price);
        userModel = (UserModel) getIntent().getSerializableExtra(CONSTANT.KEY_USER);
        okHttpClient = new OkHttpClient();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Thêm một khung giờ mới");
        initList();
        initView();


    }
    public void initList()
    {
        client = new OkHttpClient();
        listDate = new ArrayList<>();
        listName = new ArrayList<>();
        listPitch = new ArrayList<>();
        listDate.add("Ngày thường");
        listDate.add("Ngày nghỉ,Thứ 7 - Chủ nhật");

    }
    public void initView()
    {
        edt_startTime = (EditText) findViewById(R.id.edt_startTime);
        edt_endTime = (EditText) findViewById(R.id.edt_endTime);
        edt_price = (EditText) findViewById(R.id.edt_price);
        edt_description = (EditText) findViewById(R.id.edt_des);
        spinnerDate = (Spinner) findViewById(R.id.spnDate);
        spinnerPitch = (Spinner) findViewById(R.id.spnPitch);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        edt_startTime.setOnClickListener(this);
        edt_endTime.setOnClickListener(this);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddPriceActivity.this,android.R.layout.simple_list_item_1,listDate);
        spinnerDate.setAdapter(arrayAdapter);
        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeofDate= String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        new GetListPitch().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddPriceActivity.this,android.R.layout.simple_list_item_1,listName);
            spinnerPitch.setAdapter(adapter);
            spinnerPitch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        idPitch = listPitch.get(position).getId();
                    Log.d(TAG,"id pitch : "+idPitch);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddPriceActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
    }
    class AddPrice extends AsyncTask<String,String,String>
    {
        HashMap<String,String> param;
        ProgressDialog progressDialog;

        public AddPrice(HashMap<String,String> body)
        {
            this.param=body;
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Response response = client.newCall(NetworkUtils.createPostRequest(API.NewPrice,this.param)).execute();
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
            Log.d(TAG,s);
            progressDialog.dismiss();
            if(s != null) {
                Intent intent = new Intent(AddPriceActivity.this,PitchManagementActivity.class);
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                startActivity(intent);
                finish();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddPriceActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.edt_startTime :
            {
                TimePickerDialog dialog = new TimePickerDialog(AddPriceActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String h="",m="";
                        if(hourOfDay>9) h=hourOfDay+""; else h="0"+""+hourOfDay;
                        if(minute>9) m=minute+""; else m="0"+""+minute;
                        edt_startTime.setText(h+":"+m);
                        Log.d("time",h+":"+m);
                    }
                },0,0,true);
                dialog.setTitle("Chọn giờ bắt đầu");
                dialog.show();
                break;
            }
            case R.id.edt_endTime :
            {
                TimePickerDialog dialog = new TimePickerDialog(AddPriceActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String h="",m="";
                        if(hourOfDay>9) h=hourOfDay+""; else h="0"+""+hourOfDay;
                        if(minute>9) m=minute+""; else m="0"+""+minute;
                        edt_endTime.setText(h+":"+m);
                        Log.d("time",h+":"+m);
                    }
                },0,0,true);
                dialog.setTitle("Chọn giờ kết thúc");
                dialog.show();
                break;
            }
            case R.id.button :
            {
                HashMap<String,String> body = new HashMap<>();
                body.put("system_id","1");
                body.put("pitch_id",idPitch);
                Log.d("add price",idPitch+":"+typeofDate+":"+edt_description.getText().toString());
                body.put("time_start",edt_startTime.getText().toString());
                body.put("time_end",edt_endTime.getText().toString());
                body.put("price",edt_price.getText().toString());
                body.put("typedate",typeofDate);
                body.put("description",edt_description.getText().toString());
                new AddPrice(body).execute();

                break;
            }
        }
    }
}

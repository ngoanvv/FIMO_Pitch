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
import android.widget.TextView;
import android.widget.TimePicker;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.model.Price;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.NetworkUtils;
import com.fimo_pitch.support.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditPriceActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edt_startTime,edt_endTime,edt_price,edt_description;
    private List<String> listDate;
    private Spinner spinnerDate;
    private TextView tvName;
    private Button button;
    private String TAG="AddPriceActivity";
    private OkHttpClient client;
    private UserModel userModel;
    private OkHttpClient okHttpClient;
    private String listpitchData;
    private ArrayList<Pitch> listPitch;
    private List<String> listName;
    private String idPitch;
    private Price price;
    private String typeofDate="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_price);
        userModel = (UserModel) getIntent().getSerializableExtra(CONSTANT.KEY_USER);
        okHttpClient = new OkHttpClient();
        price = (Price) getIntent().getSerializableExtra(CONSTANT.PRICE);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Cập nhật thông tin");
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

        Log.d(TAG,price.toString());

    }
    public void initView()
    {
        edt_startTime = (EditText) findViewById(R.id.edt_startTime);
        edt_endTime = (EditText) findViewById(R.id.edt_endTime);
        edt_price = (EditText) findViewById(R.id.edt_price);
        edt_description = (EditText) findViewById(R.id.edt_des);
        spinnerDate = (Spinner) findViewById(R.id.spnDate);
        tvName = (TextView) findViewById(R.id.tvName);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        edt_startTime.setOnClickListener(this);
        edt_endTime.setOnClickListener(this);

        edt_description.setText(price.getDescription());
        edt_price.setText(price.getPrice());
        tvName.setText(price.getPitchName());


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditPriceActivity.this,android.R.layout.simple_list_item_1,listDate);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class EditPrice extends AsyncTask<String,String,String>
    {
        HashMap<String,String> param;
        ProgressDialog progressDialog;

        public EditPrice(HashMap<String,String> body)
        {
            this.param=body;
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Response response = client.newCall(NetworkUtils.createPutRequest(API.updatePrice+price.getId(),this.param)).execute();
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
            if(s != "failed") {
                Intent intent = new Intent(EditPriceActivity.this,PriceManagementActivity.class);
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                startActivity(intent);
                finish();
            }
            else
            {
                Utils.openDialog(EditPriceActivity.this,"Đã có lỗi xảy ra ");
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditPriceActivity.this);
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
                TimePickerDialog dialog = new TimePickerDialog(EditPriceActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            edt_startTime.setText(hourOfDay+":"+minute);
                    }
                },0,0,true);
                dialog.setTitle("Chọn giờ bắt đầu");
                dialog.show();
                break;
            }
            case R.id.edt_endTime :
            {
                TimePickerDialog dialog = new TimePickerDialog(EditPriceActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        edt_startTime.setText(hourOfDay+":"+minute);

                    }
                },0,0,true);
                dialog.setTitle("Chọn giờ kết thúc");
                dialog.show();
                break;
            }
            case R.id.button :
            {
                HashMap<String,String> body = new HashMap<>();
                body.put("id",price.getId());
                body.put("system_id",price.getSystemId());
                body.put("pitch_id",price.getPitchId());
                body.put("time_start","15:30"+":00");
                body.put("time_end","17:30"+":00");
                body.put("price",edt_price.getText().toString());
                body.put("typedate",typeofDate);
                body.put("description",edt_description.getText().toString());
                new EditPrice(body).execute();

                break;
            }
        }
    }
}

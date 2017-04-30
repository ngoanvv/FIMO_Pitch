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
import android.widget.EditText;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.NetworkUtils;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class AddPitchActivity extends AppCompatActivity {
    private HashMap<String,String> param;
    private EditText edtName,edtType,edtSize,edtDes;
    private Button btAdd;
    private OkHttpClient client;
    private String TAG="AddPitchActivity";
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pitch);
        userModel = (UserModel) getIntent().getSerializableExtra(CONSTANT.KEY_USER);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Thêm một sân mới");

        initData();
        initView();
    }

    public void initData()
    {
        client = new OkHttpClient();
        param = new HashMap<>();
    }
    public void initView()
    {
        edtName = (EditText) findViewById(R.id.edtname);
        edtType = (EditText) findViewById(R.id.edttype);
        edtSize = (EditText) findViewById(R.id.edtsize);
        edtDes = (EditText) findViewById(R.id.edtdes);
        btAdd = (Button) findViewById(R.id.bt_addPitch);

        edtName.setText("Sân Demo");
        edtType.setText("Sân Cỏ");
        edtSize.setText("9");
        edtDes.setText("Sân đẹp lắm");

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> param = new HashMap<String, String>();
                if(edtName.getText().toString().length()>0
                && edtType.getText().toString().length()>0
                && edtSize.getText().toString().length()>0
                && edtDes.getText().toString().length()>0)
                param.put("name",edtName.getText().toString());
                param.put("type",edtType.getText().toString());
                param.put("size",edtSize.getText().toString());
                param.put("description",edtDes.getText().toString());
                param.put("system_id","2");
                param.put("image","image");
                new MyTask(param).execute();
            }
        });
    }
    class MyTask extends AsyncTask<String,String,String>
    {

        
        HashMap<String,String> param;
        ProgressDialog progressDialog;

        public MyTask(HashMap<String,String> body)
        {
            this.param=body;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG,s);
            progressDialog.dismiss();
            if(s.contains("success")) {
                Intent intent = new Intent(AddPitchActivity.this,PitchManagementActivity.class);
                startActivity(intent);
                finish();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddPitchActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Response response = client.newCall(NetworkUtils.createPostRequest(API.InsertPitch, this.param)).execute();
                if (response.isSuccessful()) {
                    String results = response.body().string();
                    Log.d("run", results);
                    return results;
                }
            }
            catch (Exception e)
            {
                return null;
            }
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(AddPitchActivity.this,PitchManagementActivity.class);
            intent.putExtra(CONSTANT.KEY_USER,userModel);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

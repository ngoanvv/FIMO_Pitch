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

import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.PitchAdapter;
import com.fimo_pitch.adapter.PitchManagementAdapter;
import com.fimo_pitch.adapter.PriceAdapter;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.model.Price;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.ShowToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PriceManagementActivity extends AppCompatActivity {
    private RoundedImageView btAdd;
    private RecyclerView recyclerView;
    private PriceAdapter adapter;
    private ArrayList<Price> listPrice;
    private OkHttpClient okHttpClient;
    private String listpitchData;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_management);
        userModel = (UserModel) getIntent().getSerializableExtra(CONSTANT.KEY_USER);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Quản lý giá tiền, khung giờ ");
        initList();
        initView();
    }
    public void initList()
    {
        listPrice = new ArrayList<>();
        listPrice.add( new Price());
        listPrice.add( new Price());

    }
    
    public void initView()
    {

        btAdd = (RoundedImageView) findViewById(R.id.bt_addPitch);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PriceManagementActivity.this,AddPriceActivity.class);
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.list_pitch);
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(PriceManagementActivity.this); // (Context context)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
        adapter = new PriceAdapter(PriceManagementActivity.this,listPrice);
        recyclerView.setAdapter(adapter);
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

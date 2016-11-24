package com.fimo_pitch.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.fimo_pitch.R;
import com.fimo_pitch.adapter.ManageAdapter;

import java.util.ArrayList;

public class ManageActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ArrayList<String> list;
    private ManageAdapter manageAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        toolbar = (Toolbar) findViewById(R.id.mng_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_mng);
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this); // (Context context)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
        initView();
        toolbar.setTitle("Quản lý");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    public void initView()
    {
        list = new ArrayList<>();
        list.add(new String("Quản lý đặt sân"));
        list.add(new String("Quản lý sân bóng"));
        list.add(new String("Địa điểm yêu thích"));
        list.add(new String("Đánh giá của khách hàng"));
        list.add(new String("Hỏi đáp"));
        manageAdapter = new ManageAdapter(ManageActivity.this,list);
        recyclerView.setAdapter(manageAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}

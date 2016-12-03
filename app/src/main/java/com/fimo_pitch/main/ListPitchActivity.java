package com.fimo_pitch.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.fimo_pitch.R;
import com.fimo_pitch.adapter.PitchAdapter;
import com.fimo_pitch.model.Pitch;

import java.util.ArrayList;

public class ListPitchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PitchAdapter adapter;
    private ArrayList<Pitch> listPitches;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pitch);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chọn sân bóng");
        initView();
    }
    public void initView()
    {
        listPitches = new ArrayList<>();
        listPitches.add(new Pitch());
        listPitches.add(new Pitch());
        listPitches.add(new Pitch());
        listPitches.add(new Pitch());
        listPitches.add(new Pitch());
        listPitches.add(new Pitch());

        recyclerView = (RecyclerView) findViewById(R.id.list_pitch);
        adapter = new PitchAdapter(ListPitchActivity.this,listPitches);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(ListPitchActivity.this); // (Context context)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
    }
}

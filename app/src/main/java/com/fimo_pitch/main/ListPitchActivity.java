package com.fimo_pitch.main;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.PitchAdapter;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.model.SystemPitch;

import org.w3c.dom.Text;
import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.Calendar;

public class ListPitchActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private PitchAdapter adapter;
    private ArrayList<Pitch> listPitches;
    private TextView tv_timeFilter,tv_dateFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pitch);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initView();
    }
    public void initView()
    {


        tv_dateFilter = (TextView) findViewById(R.id.date_filter);
        tv_dateFilter.setOnClickListener(this);

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
    public void showTimePicker()
    {
        TimePickerDialog timePickerDialog = new TimePickerDialog(ListPitchActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(view.isShown())
                {
                    tv_timeFilter.setText(hourOfDay+"h"+" - "+(hourOfDay+2)+"h");
                }
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }
    public void showDatePicker()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(ListPitchActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (view.isShown())
                {
                    tv_dateFilter.setText(dayOfMonth+" / "+monthOfYear+" / "+year);
//                    showTimePicker();
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
//            SystemPitch systemPitch = new SystemPitch();
//            systemPitch.setComment("3.3");
//            systemPitch.setOwnerName("Tiến TM");
//            systemPitch.setPhone("0923829832");
//            systemPitch.setOwnerID("12123123");
//            systemPitch.setName("Sân bóng Cổ Loa");
//            systemPitch.setLat("25.090921");
//            systemPitch.setLng("108.2222");
//            systemPitch.setId("1");
//            systemPitch.setDescription("Sân đẹp sân ngon ahihi");
//            systemPitch.setAddress("Số 43, Cổ Loa, Đông Anh, Hà Nội");
//            systemPitch.setRating("3.4");
//            Intent intent =new Intent(ListPitchActivity.this,DetailActivity.class);
//            intent.putExtra(CONSTANT.SystemPitch_MODEL,systemPitch);
//            startActivity(intent);
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
        }
    }
}

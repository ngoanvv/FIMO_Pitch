package com.fimo_pitch.main;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.ShowToast;

import java.util.ArrayList;
import java.util.List;

public class EditPriceActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edt_startTime,edt_endTime,edt_price,edt_description;
    private List<String> listDate;
    private Spinner spinnerDate;
    private Button  button;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_price);
        userModel = (UserModel) getIntent().getSerializableExtra(CONSTANT.KEY_USER);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Chỉnh sửa khung giờ");
        initList();
        initView();


    }
    public void initList()
    {
        listDate = new ArrayList<>();
        listDate.add("Ngày thường");
        listDate.add("Ngày nghỉ,Thứ 7 - Chủ nhật");

    }
    public void initView()
    {
        edt_startTime       = (EditText) findViewById(R.id.edt_startTime);
        edt_endTime         = (EditText) findViewById(R.id.edt_endTime);
        edt_price           = (EditText) findViewById(R.id.edt_price);
        edt_description     = (EditText) findViewById(R.id.edt_des);
        spinnerDate         = (Spinner) findViewById(R.id.spnDate);
        button              = (Button) findViewById(R.id.button);

        edt_price.setText("100000");
        edt_description.setText("Description");

        edt_startTime.setOnClickListener(this);
        edt_endTime.setOnClickListener(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditPriceActivity.this,android.R.layout.simple_list_item_1,listDate);
        spinnerDate.setAdapter(arrayAdapter);

        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
                break;
            }
        }
    }
}

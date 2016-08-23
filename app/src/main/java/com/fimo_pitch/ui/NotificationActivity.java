package com.fimo_pitch.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.fimo_pitch.R;
import com.fimo_pitch.adapter.GridViewAdapter;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    GridView gridView;
    ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        list = new ArrayList<>();
        list.add("2323");
        list.add("2323");
        list.add("2323");
        list.add("2323");
        list.add("2323");
        list.add("2323");
        list.add("2323");
        list.add("2323");

        gridView = (GridView) findViewById(R.id.gridView);
        GridViewAdapter adapter = new GridViewAdapter(this,R.layout.item_grid_view,list);
        gridView.setAdapter(adapter);
        gridView.setBackgroundColor(Color.WHITE);
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
    }
}

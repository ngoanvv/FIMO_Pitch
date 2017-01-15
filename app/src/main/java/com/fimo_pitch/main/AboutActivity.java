package com.fimo_pitch.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.model.Pitch;

public class AboutActivity extends AppCompatActivity {

    ImageView imageView;
    TextView namePitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
            

    }
}

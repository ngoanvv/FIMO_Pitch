package com.fimo_pitch.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.model.Pitch;

public class MatchActivity extends AppCompatActivity {

    ImageView imageView;
    TextView namePitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        Pitch pitch = (Pitch) getIntent().getSerializableExtra("ObjMatch");

        imageView = (ImageView) findViewById(R.id.imagePitch);
        namePitch = (TextView) findViewById(R.id.namePitch);

        imageView.setImageResource(pitch.getImage());
        namePitch.setText(pitch.getAddress());

    }
}

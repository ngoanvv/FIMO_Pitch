package com.fimo_pitch.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fimo_pitch.R;

public class FirstActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Boolean           seen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(sharedPreferences!=null)
                {
                    seen = sharedPreferences.getBoolean("seen",false);
                    if(seen==true)
                    {
                        Intent intent = new Intent(FirstActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(FirstActivity.this,IntroductionActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, 3000);

    }
}

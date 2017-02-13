package com.fimo_pitch.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fimo_pitch.R;
import com.fimo_pitch.support.NetworkUtils;
import com.fimo_pitch.support.ShowToast;
import com.fimo_pitch.support.Utils;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FirstActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Boolean           seen;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        client = new OkHttpClient();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Utils.isConnected(FirstActivity.this)) {
            sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (sharedPreferences != null) {
                        seen = sharedPreferences.getBoolean("seen", false);
                        if (seen == true) {
                            Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(FirstActivity.this, IntroductionActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }, 1000);

        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
            builder.setMessage(getString(R.string.no_connection));
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.create().show();
        }
    }
}

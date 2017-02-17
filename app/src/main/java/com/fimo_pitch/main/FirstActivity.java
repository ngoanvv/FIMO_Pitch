package com.fimo_pitch.main;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.IntegerRes;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.fimo_pitch.R;
import com.fimo_pitch.support.NetworkUtils;
import com.fimo_pitch.support.ShowToast;
import com.fimo_pitch.support.Utils;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        FirebaseApp.initializeApp(FirstActivity.this);

    }
    private void makeNotification(Context context,String content)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("FIMO MSN")
                        .setContentText(content);
        Intent resultIntent = new Intent(context, FirstActivity.class);
        TaskStackBuilder stackBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(FirstActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT  );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(10, mBuilder.build());
        }
        else
        {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(10, mBuilder.build());
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1000);
        }
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

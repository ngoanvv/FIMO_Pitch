package com.fimo_pitch.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fimo_pitch.R;
import com.fimo_pitch.adapter.NotificationAdapter;
import com.fimo_pitch.model.NotificationModel;
import com.fimo_pitch.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NotificationActivity";
    private ListView mListview;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationModel> mlistNotification;
    private UserModel mUserModel;
    private String mToken;
    private int mPage = 0;
    private int mPageSize = 20;
    private ProgressBar progressBar;
    private View view;
    private boolean isLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Log.d(TAG,"on create");

        mListview = (ListView) findViewById(R.id.listview_notification);
        initNotificationList();
        initAdapter();
    }


    private void initAdapter() {
            notificationAdapter = new NotificationAdapter(NotificationActivity.this,mlistNotification);
            mListview.setAdapter(notificationAdapter);
//        notificationAdapter.notifyDataSetChanged();
    }
    private void initNotificationList()
    {
        mlistNotification = new ArrayList<>();
        mlistNotification.add(new NotificationModel("Cần LX",""," Dương NV đã đánh giá về sân bóng của bạn","Ngày hôm qua, lúc 12h30","",true));
        mlistNotification.add(new NotificationModel("Cần LX",""," Dương NV đã đánh giá về sân bóng của bạn","Ngày hôm qua, lúc 12h30","",true));
        mlistNotification.add(new NotificationModel("Cần LX",""," Dương NV đã đánh giá về sân bóng của bạn","Ngày hôm qua, lúc 12h30","",true));
        mlistNotification.add(new NotificationModel("Cần LX",""," Dương NV đã đánh giá về sân bóng của bạn","Ngày hôm qua, lúc 12h30","",true));
        mlistNotification.add(new NotificationModel("Cần LX",""," Dương NV đã đánh giá về sân bóng của bạn","Ngày hôm qua, lúc 12h30","",true));
        mlistNotification.add(new NotificationModel("Cần LX",""," Dương NV đã đánh giá về sân bóng của bạn","Ngày hôm qua, lúc 12h30","",true));
        mlistNotification.add(new NotificationModel("Cần LX",""," Dương NV đã đánh giá về sân bóng của bạn","Ngày hôm qua, lúc 12h30","",true));
        mlistNotification.add(new NotificationModel("Cần LX",""," Dương NV đã đánh giá về sân bóng của bạn","Ngày hôm qua, lúc 12h30","",true));
        mlistNotification.add(new NotificationModel("Cần LX",""," Dương NV đã đánh giá về sân bóng của bạn","Ngày hôm qua, lúc 12h30","",true));
        Log.d(TAG,mlistNotification.size()+"sjze");

    }



}

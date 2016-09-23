package com.fimo_pitch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fimo_pitch.R;
import com.fimo_pitch.adapter.NotificationAdapter;
import com.fimo_pitch.model.NotificationModel;
import com.fimo_pitch.model.UserModel;

import java.util.ArrayList;

/**
 * Created by TranManhTien on 23/08/2016.
 */
public class NotifcationFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "NotifcationFragment";
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationModel> mlistNotification;
    private UserModel mUserModel;
    private boolean isLoading = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification,container,false);
        initView(view);
        initNotificationList();
        initAdapter();
        return view;
    }
    public void initView(View v)
    {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_notification);

    }
    private void initAdapter() {
        notificationAdapter = new NotificationAdapter(getActivity(),mlistNotification);
        recyclerView.setAdapter(notificationAdapter);
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity()); // (Context context)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
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
    public NotifcationFragment() {

    }
    public static NotifcationFragment newInstance(String param1, String param2) {
        NotifcationFragment fragment = new NotifcationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
}

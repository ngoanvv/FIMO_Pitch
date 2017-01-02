package com.fimo_pitch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.adapter.NotificationAdapter;
import com.fimo_pitch.model.NotificationModel;
import com.fimo_pitch.model.UserModel;

import java.util.ArrayList;

/**
 * Created by TranManhTien on 23/08/2016.
 */
public class PaymentSuccessFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "PaymentSuccessFragment";
    private TextView tv_continue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_success,container,false);
        initView(view);
        return view;
    }
    public void initView(View v)
    {
        tv_continue = (TextView) v.findViewById(R.id.bt_continue);
        tv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
    public PaymentSuccessFragment() {

    }
    public static PaymentSuccessFragment newInstance(String param1, String param2) {
        PaymentSuccessFragment fragment = new PaymentSuccessFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
}

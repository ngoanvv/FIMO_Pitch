package com.fimo_pitch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fimo_pitch.R;


/**
 * Created by Diep_Chelsea on 13/07/2016.
 */
public class UserOrderManagement extends Fragment implements View.OnClickListener {
    public static final String TAG = "PaymentFragment";
    public ImageView img_payment;
    public RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_user_order, container, false);
        initView(rootView);

        return rootView;
    }


    private void initView(View view) {

            recyclerView = (RecyclerView) view.findViewById(R.id.listOrder);

    }


    public UserOrderManagement(String s) {

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
    }
}


package com.fimo_pitch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fimo_pitch.R;

import org.w3c.dom.Text;

/**
 * Created by Diep_Chelsea on 13/07/2016.
 */
public class OwnerFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "NewsFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button btManageOrder,btAddPitch,btManageSystem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.activity_owner, container, false);
        return rootView;
    }

    public void initView(View v)
    {
        btManageOrder = (Button) v.findViewById(R.id.bt_manageOrder);
        btAddPitch = (Button) v.findViewById(R.id.bt_add_pitch);
        btManageSystem = (Button) v.findViewById(R.id.bt_manageSystem);

        btManageSystem.setOnClickListener(this);
        btAddPitch.setOnClickListener(this);
        btManageOrder.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_add_pitch :
            {
                break;
            }
            case R.id.bt_manageOrder :
            {

                break;
            }
            case R.id.bt_manageSystem :
            {

                break;
            }
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    public OwnerFragment() {

    }
    public static OwnerFragment newInstance(String param1, String param2) {
        OwnerFragment fragment = new OwnerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


}


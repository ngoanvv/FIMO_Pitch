package com.fimo_pitch.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.main.OrderManagementActivity;
import com.fimo_pitch.main.PitchManagementActivity;
import com.fimo_pitch.main.PriceManagementActivity;
import com.fimo_pitch.main.SystemManagementActivity;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Diep_Chelsea on 13/07/2016.
 */
public class OwnerFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "NewsFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button btManageOrder,btManagePitch,btManageSystem,btManagePrice;
    private UserModel userModel;
    private OkHttpClient okHttpClient;
    private SystemPitch mSystemPitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_owner, container, false);
        userModel = (UserModel) getActivity().getIntent().getSerializableExtra(CONSTANT.KEY_USER);
        initView(rootView);
        new GetListSytembyId(userModel.getId()).execute();
        return rootView;
    }

    public void initView(View v)
    {
        btManageOrder = (Button) v.findViewById(R.id.bt_manageOrder);
        btManagePitch = (Button) v.findViewById(R.id.btManagePitch);
        btManageSystem = (Button) v.findViewById(R.id.bt_manageSystem);
        btManagePrice = (Button) v.findViewById(R.id.bt_managerPrice);

        btManagePrice.setOnClickListener(this);
        btManageSystem.setOnClickListener(this);
        btManagePitch.setOnClickListener(this);
        btManageOrder.setOnClickListener(this);

    }
    private class GetListSytembyId extends AsyncTask<String,Void,String> {
        String id;
        public  GetListSytembyId(String userId)
        {
            this.id= userId;
        }
        ProgressDialog progressDialog;
        @Override
        protected String doInBackground(String... params) {
            try {
                okHttpClient = new OkHttpClient();
                Request request = NetworkUtils.createGetRequest(API.GetListSystemById+this.id);
                Response systemPitchResponse = okHttpClient.newCall(request).execute();
                if (systemPitchResponse.isSuccessful()) {
                    String data = systemPitchResponse.body().string().toString();
                    Log.d("hello",data.toString());
                    return data;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
            return "failed";

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!s.contains("failed"))
            {
                try {
                    JSONObject result = new JSONObject(s);
                    if (result.getString("status").contains("success")) {
                        JSONArray data = result.getJSONArray("data");
                            JSONObject object = data.getJSONObject(0);
                            mSystemPitch = new SystemPitch();
                            mSystemPitch.setId(object.getString("id"));
                            mSystemPitch.setName(object.getString("name"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            progressDialog.dismiss();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btManagePitch :
            {
                Intent intent = new Intent(getActivity(), PitchManagementActivity.class);
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                intent.putExtra(CONSTANT.SystemPitch_MODEL,mSystemPitch);
                startActivity(intent);
                break;
            }
            case R.id.bt_manageOrder :
            {
                Intent intent = new Intent(getActivity(), OrderManagementActivity.class);
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                intent.putExtra(CONSTANT.SystemPitch_MODEL,mSystemPitch);
                startActivity(intent);
                break;
            }
            case R.id.bt_manageSystem :
            {
                Intent intent = new Intent(getActivity(), SystemManagementActivity.class);
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                intent.putExtra(CONSTANT.SystemPitch_MODEL,mSystemPitch);
                startActivity(intent);
//                Utils.openDialog(getContext(),"Chức năng này chưa khả dụng");
                break;
            }
            case R.id.bt_managerPrice :
            {
                Intent intent = new Intent(getActivity(), PriceManagementActivity.class);
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                intent.putExtra(CONSTANT.SystemPitch_MODEL,mSystemPitch);
                startActivity(intent);
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


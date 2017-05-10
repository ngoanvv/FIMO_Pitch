package com.fimo_pitch.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.UserOrderAdapter;
import com.fimo_pitch.model.TimeTable;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.NetworkUtils;
import com.fimo_pitch.support.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Response;


/**
 * Created by Diep_Chelsea on 13/07/2016.
 */
public class UserOrderManagement extends Fragment implements View.OnClickListener {
    public static final String TAG = "UserOrder";
    public ImageView img_payment;
    public RecyclerView recyclerView;
    public UserOrderAdapter adapter;
    private OkHttpClient okHttpClient;
    private UserModel userModel;
    private ArrayList<TimeTable> listTimes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_user_order, container, false);
        initView(rootView);
        userModel = (UserModel) getActivity().getIntent().getSerializableExtra(CONSTANT.KEY_USER);
        return rootView;
    }


    private void initView(View view)
    {
        recyclerView = (RecyclerView) view.findViewById(R.id.listOrder);
        new GetTime().execute();
    }
    private class GetTime extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        String id;
        String pitchName;
        HashMap<String,String> param;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Đang thao tác");
//            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                okHttpClient = new OkHttpClient();
                Response response =
                        okHttpClient.newCall(NetworkUtils.createGetRequest(API.GetOrderbyUser+userModel.getId())).execute();
                String results = response.body().string();
                if (response.isSuccessful()) {
                    return results;
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "failed";
            }
            return "failed";

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listTimes = new ArrayList<>();
            progressDialog.dismiss();
            Log.d(TAG,s);
            if(s != "failed")
                try {
                    JSONObject result = new JSONObject(s);
                    if (result.getString("status").contains("success")) {
                        JSONArray data = result.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            TimeTable t = new TimeTable();
//                            t.setManagement_id(object.getString("management_id"));
                            t.setType(object.getString("status"));
                            t.setId(object.getString("order_id"));
                            t.setPrice(object.getString("price"));
//                            t.setPitchId(object.getString("pitch_id"));
                            t.setDay(object.getString("day").substring(0,10));
                            t.setStart_time(object.getString("time_start").substring(0,5));
                            t.setEnd_time(object.getString("time_end").substring(0,5));
                            listTimes.add(t);
                        }

                    }
                    LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getContext()); // (Context context)
                    mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
                    adapter = new UserOrderAdapter(getContext(), listTimes,userModel);
                    recyclerView.setAdapter(adapter);
                    if(listTimes.size()>0)
                    {
                        Utils.openDialog(getContext(),"Có "+listTimes.size()+" kết quả");
                    }
                    else
                    {
                        Utils.openDialog(getContext(),"Không có khung giờ trống trong ngày");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
        }

    }

    public UserOrderManagement(String s) {

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
    }
}


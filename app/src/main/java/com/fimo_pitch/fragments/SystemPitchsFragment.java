package com.fimo_pitch.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.SystemPitchAdapter;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Response;


public class SystemPitchsFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "SystemPitchsFragment";
    private Spinner spinner_location;
    private RecyclerView recyclerView;
    private RelativeLayout menuView;
    private SystemPitchAdapter adapter;
    private ImageView buttonView2;
    private ImageView buttonView4;
    public static String data;
    public OkHttpClient okHttpClient;
    private UserModel userModel;
    private ArrayList<SystemPitch> listSystemPitch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_matchs, container, false);
        listSystemPitch = new ArrayList<>();
//        Log.d(TAG,data);

        userModel = (UserModel) getActivity().getIntent().getSerializableExtra(CONSTANT.KEY_USER);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initView(final View rootView)
    {
        menuView = (RelativeLayout) rootView.findViewById(R.id.menu_view);
        buttonView2 = (ImageView) rootView.findViewById(R.id.view2);
        buttonView4 = (ImageView) rootView.findViewById(R.id.view4);
        spinner_location = (Spinner) rootView.findViewById(R.id.spn_location);
        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> body = new HashMap<>();
                body.put("location",spinner_location.getSelectedItem().toString());
                new MyTask(rootView,getContext(),body).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_dropdown_item_1line,getActivity().getResources().getStringArray(R.array.listProvince));
        spinner_location.setAdapter(adapter);
        addListenerOnButton(rootView);


    }
    private void addListenerOnButton(View v) {

        buttonView2.setOnClickListener(this);
        buttonView4 .setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.view2:
            {
                Log.d(TAG,listSystemPitch.size()+"");
                LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity()); // (Context context)
                mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
                break;
            }
            case R.id.view4:
            {
                StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
                recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
                break;
            }
        }
    }
    class MyTask extends AsyncTask<String,String,String>
    {
        Context context;
        HashMap<String,String> param;
        ProgressDialog progressDialog;
        OkHttpClient client;
        View view;
        public MyTask(View v,Context ct,HashMap<String,String> body)
        {
            view =v;
            client = new OkHttpClient();
            this.context = ct;
            this.param=body;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Response response =
                        client.newCall(NetworkUtils.createPostRequest(API.GetSystemByLocation, this.param)).execute();
                String results = response.body().string();
                Log.d("run", results);
                if (response.isSuccessful()) {
                    return results;
                }

            }
            catch (Exception e)
            {
                return "failed";
            }
            return "failed";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.contains("success")) {
                listSystemPitch = new ArrayList<>();
                String result = s.toString();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            SystemPitch systemPitch = new SystemPitch();
                            systemPitch.setDescription(object.getString("description"));
                            systemPitch.setId(object.getString("id"));
                            systemPitch.setOwnerName("Tiến TM");
                            systemPitch.setOwnerID(object.getString("user_id"));
                            systemPitch.setName(object.getString("name"));
                            systemPitch.setAddress(object.getString("address"));
                            systemPitch.setId(object.getString("id"));
                            systemPitch.setPhone(object.getString("phone"));
                            systemPitch.setLat(object.getString("lat"));
                            systemPitch.setLng(object.getString("log"));
                            listSystemPitch.add(systemPitch);
                        }
                        Log.d(TAG,listSystemPitch.size()+"sadsad");
                        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
                        recyclerView.setHasFixedSize(true);
                        adapter = new SystemPitchAdapter(getActivity(), listSystemPitch,userModel);

                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(adapter);

                        StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new
                                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
                        recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

        }
            progressDialog.dismiss();

        }
    }
    public SystemPitchsFragment(String s)
    {
        data=s;
    }

}
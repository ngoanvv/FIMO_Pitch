package com.fimo_pitch.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.SystemPitchAdapter;
import com.fimo_pitch.db.MyDatabaseHelper;
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
    private LinearLayout menuView;
    private SystemPitchAdapter adapter;
    private ImageView buttonView2;
    private EditText edt_search;
    private ImageView buttonView4;
    public static String data;
    public OkHttpClient okHttpClient;
    public MyDatabaseHelper myDatabaseHelper;
    private UserModel userModel;
    private ArrayList<SystemPitch> rootList;
    private ArrayList<SystemPitch> listSystemPitch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_matchs, container, false);
        listSystemPitch = new ArrayList<>();
        myDatabaseHelper = new MyDatabaseHelper(getContext());
        userModel = (UserModel) getActivity().getIntent().getSerializableExtra(CONSTANT.KEY_USER);
        initView(view);
        initRecyclerView(data);
        initRootList(data);

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
    private ArrayList<SystemPitch> filterByAddress(String s)
    {
        ArrayList result = new ArrayList();
        for(int i=0;i<rootList.size();i++)
        {
            if(rootList.get(i).getAddress().contains(s))
                result.add(rootList.get(i));
        }
        return result;
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    private void initRootList(String s)
    {
            rootList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray array = jsonObject.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
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
                    rootList.add(systemPitch);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    }
    private void initView(final View rootView)
    {
        menuView = (LinearLayout) rootView.findViewById(R.id.menu_view);
        spinner_location = (Spinner) rootView.findViewById(R.id.spn_location);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
                @Override
            public void afterTextChanged(Editable s) {

            }
        });
        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0)
                initRecyclerView(filterByAddress(spinner_location.getSelectedItem().toString()));
                else initRecyclerView(rootList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item2,getActivity().getResources().getStringArray(R.array.listProvince));
        spinner_location.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
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

                initRecyclerView(s);

            }
            progressDialog.dismiss();

        }
    }
    public void initRecyclerView(ArrayList<SystemPitch> list)
    {
            recyclerView.setHasFixedSize(true);
            adapter = new SystemPitchAdapter(getActivity(), list,userModel);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
            StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
            recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);

    }
    public void initRecyclerView(String data)
    {
        listSystemPitch = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray array = jsonObject.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
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
    public SystemPitchsFragment(String s)
    {
        data=s;
    }

}
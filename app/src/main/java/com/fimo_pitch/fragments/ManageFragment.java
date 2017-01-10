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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.adapter.CustomExpandListviewAdapter;
import com.fimo_pitch.adapter.NewsFragmentAdapter;
import com.fimo_pitch.adapter.OrderAdapter;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.main.ListPitchActivity;
import com.fimo_pitch.model.Order;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.model.SystemPitch;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Diep_Chelsea on 13/07/2016.
 */
public class ManageFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "ManageFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ArrayList<Order> listOrder;
    private ArrayList<Pitch> listPitch;
    private OkHttpClient okHttpClient;
    private OrderAdapter orderAdapter;
    private RecyclerView recyclerView;
    private TextView dayFilter;
    private Spinner pitchFilter;
    private RoundedImageView btSearch;
    private Pitch crPitch;
    private String listpitchData;
    private List<String> listName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     try {
         listOrder = new ArrayList<>();
         listPitch = new ArrayList<>();
         listName = new ArrayList<>();
         View rootView= inflater.inflate(R.layout.fragment_manage, container, false);
        initView(rootView);
        return rootView;
    }
    catch (Exception e)
    {
        return inflater.inflate(R.layout.empty, container, false);
    }
    }
    public void initView(View v)
    {
        recyclerView = (RecyclerView) v.findViewById(R.id.list_manage);
        dayFilter = (TextView) v.findViewById(R.id.date_filter);
        pitchFilter = (Spinner) v.findViewById(R.id.pitch_filter);
        btSearch = (RoundedImageView) v.findViewById(R.id.btSearch);

        dayFilter.setOnClickListener(this);
        btSearch.setOnClickListener(this);

        pitchFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                crPitch = listPitch.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listOrder.add( new Order());
        listOrder.add( new Order());
        listOrder.add( new Order());
        listOrder.add( new Order());
        listOrder.add( new Order());

        orderAdapter = new OrderAdapter(getActivity(), listOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(orderAdapter);
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity()); // (Context context)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        new GetListPitch().execute();
    }

    private class GetListPitch extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "systemPID="+1);
            Request request = new Request.Builder()
                    .url("https://pitchwebservice.herokuapp.com/pitchs/getallpitchofsystem")
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "b9494f39-8e39-7533-1896-281ee653703b")
                    .build();
            try {
                okHttpClient = new OkHttpClient();
                okhttp3.Response systemPitchResponse = okHttpClient.newCall(request).execute();
                if (systemPitchResponse.isSuccessful()) {
                    listpitchData = systemPitchResponse.body().string().toString();

//                    Log.d(TAG,mSystemPitch.getId()+", "+listpitchData.toString());

                    JSONObject result = new JSONObject(listpitchData);
                    if(result.getString("status").contains("success"))
                    {
                        JSONArray data = result.getJSONArray("data");
                        for (int i=0;i<data.length();i++)
                        {
                            JSONObject object = data.getJSONObject(i);
                            Pitch p = new Pitch();
                            p.setId(object.getString("id"));
                            p.setName(object.getString("name"));
                            p.setType(object.getString("type"));
                            p.setSize(object.getString("size"));
                            p.setDescription(object.getString("description"));
//                            if(mSystemPitch.getPhone().length()>0)
//                                p.setPhone(mSystemPitch.getPhone());
                            listPitch.add(p);
                            listName.add(object.getString("name"));
                        }

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,listName);
            pitchFilter.setAdapter(dataAdapter);
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
        int id = v.getId();
        switch (id)
        {
            case R.id.btSearch:
            {
                break;
            }
            case R.id.date_filter:
            {
                break;
            }
        }
    }
    public ManageFragment() {

    }
    public static ManageFragment newInstance(String param1, String param2) {
        ManageFragment fragment = new ManageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

}


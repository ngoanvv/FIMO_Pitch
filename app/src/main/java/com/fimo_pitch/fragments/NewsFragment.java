package com.fimo_pitch.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;


import com.fimo_pitch.API;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.NewsFragmentAdapter;
import com.fimo_pitch.model.News;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.support.NetworkUtils;
import com.fimo_pitch.support.ShowToast;
import com.fimo_pitch.support.Utils;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by Diep_Chelsea on 13/07/2016.
 */
public class NewsFragment extends Fragment {
    public static final String TAG = "NewsFragment";
    private RecyclerView recyclerView;
    private ArrayList<String> arrayLocation, arrayTime;
    private EditText edt_input_search;
    private NewsFragmentAdapter adapter;
    private ArrayList<News> list;
    private OkHttpClient okHttpClient;
    public String data;
    private String listNews="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_news, container, false);
        Log.d(TAG,data);
        initView(rootView);
        return rootView;
    }

    public void initView(View v)
    {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        edt_input_search = (EditText) v.findViewById(R.id.edt_input);
        edt_input_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        initList();
    }

    public void initList() {

        list = new ArrayList<>();
        String result = data.toString();
        if (result.contains("success")) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length() - 1; i++) {
                    JSONObject object = data.getJSONObject(i);
                    News news = new News();
                    news.setDescription(object.getString("description"));
                    news.setId(object.getString("id"));
                    news.setTitle(object.getString("title"));
                    news.setLocation(object.getString("address"));
                    news.setHostID(object.getString("user_id"));
                    news.setHostName("Trần Mạnh Tiến UET");
                    news.setTime(object.getString("time_start"));
                    list.add(news);
                }
                adapter = new NewsFragmentAdapter(getActivity(), list);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
                LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity()); // (Context context)
                mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                                new GetNews().execute();
                        }
                    }
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                    }
                });
            }
            catch (JSONException e) {
                    e.printStackTrace();
            }
        }
    }

    private class GetNews extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        @Override
        protected String doInBackground(String... params) {
            try {
                okHttpClient = new OkHttpClient();
                Response response = okHttpClient.newCall(NetworkUtils.createGetRequest(API.getNews)).execute();
                if (response.isSuccessful())
                {

                        listNews = response.body().string();
                        return listNews;
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
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length() - 1; i++) {
                    JSONObject object = data.getJSONObject(i);
                    News news = new News();
                    news.setDescription(object.getString("description"));
                    news.setId(object.getString("id"));
                    news.setTitle(object.getString("title"));
                    news.setLocation(object.getString("address"));
                    news.setHostID(object.getString("user_id"));
                    news.setHostName("Trần Mạnh Tiến UET");
                    news.setTime(object.getString("time_start"));
                    list.add(news);
                }
            adapter.updateData(list);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    public NewsFragment(String s) {
        data=s;

    }


}


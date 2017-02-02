package com.fimo_pitch.fragments;

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
import android.widget.EditText;


import com.fimo_pitch.R;
import com.fimo_pitch.adapter.NewsFragmentAdapter;
import com.fimo_pitch.model.News;
import com.fimo_pitch.support.ShowToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_news, container, false);
        Log.d(TAG,data);
        initView(rootView);
        initList();
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
                    news.setTitle("title");
                    news.setLocation("address");
                    news.setHostID(object.getString("user_id"));
                    news.setHostName("Trần Mạnh Tiến UET");
                    news.setTime("time_start");
                    news.setDescription(object.getString("description"));
                    list.add(news);
                }
                adapter = new NewsFragmentAdapter(getActivity(), list);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
                LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity()); // (Context context)
                mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
            }
            catch (JSONException e) {
                ShowToast.showToastLong(getContext(),e.getMessage().toString());

            }
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


package com.fimo_pitch.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.EditText;


import com.fimo_pitch.R;
import com.fimo_pitch.adapter.NewsFragmentAdapter;
import com.fimo_pitch.model.Match;

import java.util.ArrayList;

/**
 * Created by Diep_Chelsea on 13/07/2016.
 */
public class NewsFragment extends Fragment {
    public static final String TAG = "NewsFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private ArrayList<String> arrayLocation, arrayTime;
    private EditText edt_input_search;
    private NewsFragmentAdapter adapter;
    private ArrayList<Match> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_news, container, false);
        initList();
        initView(rootView);
        return rootView;
    }

    public void initView(View v)
    {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        edt_input_search = (EditText) v.findViewById(R.id.edt_input);
        adapter = new NewsFragmentAdapter(getActivity(),list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
//        StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
//        recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity()); // (Context context)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
        edt_input_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.initList();
                    adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void initList()
    {
        list = new ArrayList<>();
        list.add(new Match("1","18h ngày 11-7","Chelsea FC","FECON  Stadium","Đá giao lưu nhẹ nhàng, mong gặp đối thủ lâu dài thường xuyên, liên hệ số 0998989898","144 Xuân Thủy, Cầu Giấy","100k chia đôi"));
        list.add(new Match("1","18h ngày 12-7","MU FC","Old Traford Stadium","Đá giao lưu","144 Hồ tùng mậu , Cầu Giấy sdadasdasdsadasdsadasdasdasdasd","100k chia đôi"));
        list.add(new Match("1","18h ngày 13-7","MC FC","ETIHAD Stadium","Đá giao lưu","144 Xuân Thủy, Cầu Giấy","100k chia đôi"));
        list.add(new Match("1","18h ngày 14-7","Arsenal FC","Emirates Bridge Stadium","Đá giao lưu","144 Xuân Thủy, Cầu Giấy","100k chia đôi"));
        list.add(new Match("1","18h ngày 11-7","Chelsea FC","Stamford Bridge Stadium","Đá giao lưu","144 Xuân Thủy, Cầu Giấy, Hà Nội","100k chia đôi"));

    }
    private class MyTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getActivity().getString(R.string.processing));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    public NewsFragment() {

    }
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

}


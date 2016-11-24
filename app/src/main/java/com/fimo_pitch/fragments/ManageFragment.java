package com.fimo_pitch.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.fimo_pitch.R;
import com.fimo_pitch.adapter.CustomExpandListviewAdapter;
import com.fimo_pitch.adapter.NewsFragmentAdapter;
import com.fimo_pitch.model.Match;
import com.fimo_pitch.model.SystemPitch;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Diep_Chelsea on 13/07/2016.
 */
public class ManageFragment extends Fragment {
    public static final String TAG = "ManageFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private HashMap<String, ArrayList<SystemPitch>> mData;
    private ExpandableListView expandableListView;
    private CustomExpandListviewAdapter adapter;
    private ArrayList<String> mHeader;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_manage, container, false);
        initView(rootView);
        Log.d(TAG,"manage");
        return rootView;
    }

    public void initView(View v)
    {
//        expandableListView = (ExpandableListView) v.findViewById(R.id.expandlistview);
//        initList();

    }
    public void initList()
    {
            mHeader = new ArrayList<>();
            mHeader.add(new String("Ngày 17-09-1995"));
            mHeader.add(new String("Ngày 17-09-2005"));
            mHeader.add(new String("Ngày 17-09-2015"));
            mHeader.add(new String("Ngày 17-09-2025"));

            mData = new HashMap<>();

            ArrayList<SystemPitch> list1 = new ArrayList<>();
            SystemPitch a = new SystemPitch();
            a.setAddress("sadsad");
            list1.add(a);
            list1.add(a);
            list1.add(a);
            list1.add(a);
            list1.add(a);


            mData.put(mHeader.get(0),list1);
            mData.put(mHeader.get(1),list1);
            mData.put(mHeader.get(2),list1);
            mData.put(mHeader.get(3),list1);

            adapter = new CustomExpandListviewAdapter(getContext(),mHeader,mData);
            if(adapter !=null)
            expandableListView.setAdapter(adapter);
             expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return true;
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d(TAG, "onGroupClick: " + groupPosition);
                return false;
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.d(TAG, "onGroupCollapse: " + groupPosition);
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.d(TAG, "onGroupExpand: " + groupPosition);
            }
        });
        
        
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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


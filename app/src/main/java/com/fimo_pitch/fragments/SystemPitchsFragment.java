package com.fimo_pitch.fragments;


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
import android.widget.ImageButton;
import android.widget.RelativeLayout;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fimo_pitch.API;
import com.fimo_pitch.HttpRequest;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.SystemPitchAdapter;
import com.fimo_pitch.model.SystemPitch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SystemPitchsFragment extends Fragment implements View.OnClickListener, Response.ErrorListener, Response.Listener {
    private static final String TAG = "SystemPitchsFragment";
    private EditText edt_search;
    private RecyclerView recyclerView;
    private RelativeLayout menuView;
    private SystemPitchAdapter adapter;
    private ImageButton buttonView2;
    private ImageButton buttonView4;
    public static String data;
    private ArrayList<SystemPitch> listSystemPitch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_matchs, container, false);
        listSystemPitch = new ArrayList<>();
        Log.d(TAG,data);
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

    private void initView(View view)
    {
        menuView = (RelativeLayout) view.findViewById(R.id.menu_view);
        buttonView2 = (ImageButton) view.findViewById(R.id.view2);
        buttonView4 = (ImageButton) view.findViewById(R.id.view4);
        edt_search = (EditText) view.findViewById(R.id.edt_search);
        addListenerOnButton(view);
        getData(view);
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    adapter.initList();
//                    adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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
    private void getData(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        listSystemPitch = new ArrayList<>();
        String result = data.toString();
        if (result.contains("success")) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length() - 1; i++) {
                    JSONObject object = data.getJSONObject(i);
                    SystemPitch systemPitch = new SystemPitch();
                    systemPitch.setDescription(object.getString("description"));
                    systemPitch.setId(object.getString("id"));
                    systemPitch.setOwnerName("Tiến TM");
                    systemPitch.setOwnerID("user_id");
                    systemPitch.setName(object.getString("name"));
                    systemPitch.setAddress(object.getString("address"));
                    systemPitch.setId("id");
                    systemPitch.setLat(object.getString("lat"));
                    systemPitch.setLng(object.getString("log"));
                    listSystemPitch.add(systemPitch);
                }
                Log.d(TAG, listSystemPitch.size() + "");
                adapter = new SystemPitchAdapter(getActivity(), listSystemPitch);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
                LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity()); // (Context context)
                mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
            }
            catch (JSONException e)
            {

            }
        }
    }
    public SystemPitchsFragment(String s)
    {
        data=s;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
//        Utils.openDialog(getContext(),"Không thể tải trang, thử lại sau");

    }

    @Override
    public void onResponse(Object response) {
        Log.d("get data",response.toString());

    }
}
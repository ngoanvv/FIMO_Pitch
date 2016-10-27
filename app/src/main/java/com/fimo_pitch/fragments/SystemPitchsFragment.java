package com.fimo_pitch.fragments;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;



import com.fimo_pitch.R;
import com.fimo_pitch.adapter.SystemPitchAdapter;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.model.SystemPitch;

import java.util.ArrayList;


public class SystemPitchsFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "SystemPitchsFragment";
    private static final String ARG_PARAM2 = "param2";
    private EditText edt_search;
    private RecyclerView recyclerView;
    private RelativeLayout menuView;
    private SystemPitchAdapter adapter;
    private ImageButton buttonView2;
    private ImageButton buttonView4;
    private ArrayList<SystemPitch> data = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matchs, container, false);
        initView(view);
        return view;
    }

    
    private int pxToDp(int px) {
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int dp = Math.round(px / (dm.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    private void initView(View view)
    {
        menuView = (RelativeLayout) view.findViewById(R.id.menu_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        buttonView2 = (ImageButton) view.findViewById(R.id.view2);
        buttonView4 = (ImageButton) view.findViewById(R.id.view4);
        edt_search = (EditText) view.findViewById(R.id.edt_search);
        recyclerView.setHasFixedSize(true);
        makeData();
        adapter = new SystemPitchAdapter(getActivity(), data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        addOnTouchListener();
        addListenerOnButton(view);
        StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
        recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);

        edt_search.addTextChangedListener(new TextWatcher() {
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
    private void hideMenu() {

        AnimatorSet animSet = new AnimatorSet();
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(menuView, View.TRANSLATION_Y, -menuView.getHeight());
        animSet.playTogether(anim1);
        animSet.setDuration(300);
        animSet.start();
    }
    private void showMenu() {
        AnimatorSet animSet = new AnimatorSet();

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(menuView, View.TRANSLATION_Y, 0);

        animSet.playTogether(anim1);
        animSet.setDuration(300);
        animSet.start();
    }
    private void addOnTouchListener()
    {
        recyclerView.setOnTouchListener(new View.OnTouchListener(){
            final int DISTANCE = 3;

            float startY = 0;
            float dist = 0;
            boolean isMenuHide = false;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = event.getAction();

                if(action == MotionEvent.ACTION_DOWN) {
                    startY = event.getY();
                } else if(action == MotionEvent.ACTION_MOVE) {
                    dist = event.getY() - startY;

                    if((pxToDp((int)dist) <= -DISTANCE) && !isMenuHide) {
                        isMenuHide = true;
                        hideMenu();
                    } else if((pxToDp((int)dist) > DISTANCE) && isMenuHide) {
                        isMenuHide = false;
                        showMenu();
                    }

                    if((isMenuHide && (pxToDp((int)dist) <= -DISTANCE))
                            || (!isMenuHide && (pxToDp((int)dist) > 0))) {
                        startY = event.getY();
                    }
                } else if(action == MotionEvent.ACTION_UP) {
                    startY = 0;

                }

                return false;
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
    private  void makeData() {
        SystemPitch systemPitch = new SystemPitch();
        systemPitch.setAddress("144 Xuân thủy, Cầu giấy");
        systemPitch.setComment("20");
        systemPitch.setOwnerName("Phương LX");
        systemPitch.setContact("0989238923");
        systemPitch.setName("Sân Trần quốc hoàn");
        systemPitch.setRating("3.5");
        data.add(systemPitch);

        SystemPitch systemPitch1 = new SystemPitch();

        systemPitch1.setAddress("223 Xuân thủy, Cầu giấy");
        systemPitch1.setComment("20");
        systemPitch1.setOwnerName("Dương LX");
        systemPitch1.setContact("0989238923");
        systemPitch1.setName("Sân LÊ quốc hoàn");
        systemPitch1.setRating("3.5");
        data.add(systemPitch1);

        SystemPitch systemPitch2 = new SystemPitch();

        systemPitch2.setAddress("456 Xuân thủy, Cầu giấy");
        systemPitch2.setComment("20");
        systemPitch2.setOwnerName("Hoàng LX");
        systemPitch2.setContact("0989238923");
        systemPitch2.setName("Sân Trần Đại Nghĩa");
        systemPitch2.setRating("3.5");
        data.add(systemPitch2);

        SystemPitch systemPitch3 = new SystemPitch();

        systemPitch3.setAddress("124 Xuân thủy, Cầu giấy");
        systemPitch3.setComment("20");
        systemPitch3.setOwnerName("Hoàng LX");
        systemPitch3.setContact("0989238923");
        systemPitch3.setName("Sân Trần Đại Nghĩa");
        systemPitch3.setRating("3.5");
        data.add(systemPitch3);

        SystemPitch systemPitch4 = new SystemPitch();

        systemPitch4.setAddress("987 Xuân thủy, Cầu giấy");
        systemPitch4.setComment("20");
        systemPitch4.setOwnerName("Hoàng LX");
        systemPitch4.setContact("0989238923");
        systemPitch4.setName("Sân Trần Đại Nghĩa");
        systemPitch4.setRating("3.5");
        data.add(systemPitch4);

    }
    public SystemPitchsFragment() {

    }
    public static SystemPitchsFragment newInstance(String param1, String param2) {
        SystemPitchsFragment fragment = new SystemPitchsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
}

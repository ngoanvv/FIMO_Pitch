package com.fimo_pitch.fragments;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;



import com.fimo_pitch.R;
import com.fimo_pitch.adapter.MatchsFragmentAdapter;
import com.fimo_pitch.model.Pitch;

import java.util.ArrayList;


public class MatchsFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "MatchsFragment";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private RelativeLayout menuView;
    private MatchsFragmentAdapter adapter;
    private ImageButton buttonView2;
    private ImageButton buttonView4;
    private ArrayList<Pitch> data = new ArrayList<>();
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
    private void initView(View view)
    {
        menuView = (RelativeLayout) view.findViewById(R.id.menu_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        buttonView2 = (ImageButton) view.findViewById(R.id.view2);
        buttonView4 = (ImageButton) view.findViewById(R.id.view4);
        recyclerView.setHasFixedSize(true);
        adapter = new MatchsFragmentAdapter(getActivity(), data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        addOnTouchListener();
        makeData();
        addListenerOnButton(view);
        StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
        recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
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
        Pitch pitch1 = new Pitch();
        pitch1.setImage(R.drawable.san1);
        pitch1.setName("San PVV");
        pitch1.setAddress("So 92 Tran Thai Tong");

        Pitch pitch2 = new Pitch();
        pitch2.setImage(R.drawable.san2);
        pitch2.setName("San Phuc Anh");
        pitch2.setAddress("So 92 Tran Thai Tong");

        Pitch pitch3 = new Pitch();
        pitch3.setImage(R.drawable.san3);
        pitch3.setName("San FECON");
        pitch3.setAddress("So 92 Tran Thai Tong, Quận Cầu Giấy, Tỉnh Hà Nội");

        Pitch pitch4 = new Pitch();
        pitch4.setImage(R.drawable.san4);
        pitch4.setName("San FECON1");
        pitch4.setAddress("So 92 Tran Thai Tong");

        Pitch pitch5 = new Pitch();
        pitch5.setImage(R.drawable.san5);
        pitch5.setName("San FECON2");
        pitch5.setAddress("So 92 Tran Thai Tong");

        Pitch pitch6 = new Pitch();
        pitch6.setImage(R.drawable.san6_2);
        pitch6.setName("San FECON3");
        pitch6.setAddress("So 92 Tran Thai Tong");

        data.add(pitch1);
        data.add(pitch2);
        data.add(pitch3);
        data.add(pitch4);
        data.add(pitch5);
        data.add(pitch6);

    }
    public MatchsFragment() {

    }
    public static MatchsFragment newInstance(String param1, String param2) {
        MatchsFragment fragment = new MatchsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }
}

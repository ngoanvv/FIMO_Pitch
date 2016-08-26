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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;



import com.fimo_pitch.R;
import com.fimo_pitch.adapter.MatchsFragmentAdapter;
import com.fimo_pitch.object.Pitch;
import java.util.ArrayList;


public class MatchsFragment extends Fragment  {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;

    RelativeLayout menuView;

    MatchsFragmentAdapter adapter;

    Button buttonView1;
    Button buttonView2;
    Button buttonView3;
    Button buttonView4;

    ArrayList<Pitch> data = new ArrayList<>();

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matchs, container, false);

        makeData();
        menuView = (RelativeLayout) view.findViewById(R.id.menu_view);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        adapter = new MatchsFragmentAdapter(getActivity(), data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        addListenerOnButton(view);

        //

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

    private void addListenerOnButton(View v) {

        buttonView1 = (Button) v.findViewById(R.id.view1);

        buttonView1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                LinearLayoutManager mLinearLayoutManagerHorizontal = new LinearLayoutManager(getActivity()); // (Context context)
                mLinearLayoutManagerHorizontal.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(mLinearLayoutManagerHorizontal);
            }

        });

        buttonView2 = (Button) v.findViewById(R.id.view2);

        buttonView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity()); // (Context context)
                mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
            }
        });

        buttonView3 = (Button) v.findViewById(R.id.view3);

        buttonView3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 3); // (Context context, int spanCount)
                recyclerView.setLayoutManager(mGridLayoutManager);
            }

        });

        buttonView4 = (Button) v.findViewById(R.id.view4);

        buttonView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)

                recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
            }
        });


    }




}

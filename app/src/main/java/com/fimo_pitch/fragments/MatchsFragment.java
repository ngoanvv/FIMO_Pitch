package com.fimo_pitch.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.fimo_pitch.R;
import com.fimo_pitch.adapter.MatchFragmentAdapter;
import com.fimo_pitch.object.Pitch;
import java.util.ArrayList;


public class MatchsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;

    MatchFragmentAdapter adapter;

    ImageButton buttonView1;
    ImageButton buttonView2;
    ImageButton buttonView3;
    ImageButton buttonView4;

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
        View v = inflater.inflate(R.layout.fragment_matchs, container, false);

        makeData();

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        adapter = new MatchFragmentAdapter(getActivity(), data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        addListenerOnButton(v);

        return v;
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
        pitch3.setImage(R.drawable.san4);
        pitch3.setName("San FECON");
        pitch3.setAddress("So 92 Tran Thai Tong");

        data.add(pitch1);
        data.add(pitch2);

    }

    private void addListenerOnButton(View v) {

        buttonView1 = (ImageButton) v.findViewById(R.id.view1);

        buttonView1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                LinearLayoutManager mLinearLayoutManagerHorizontal = new LinearLayoutManager(getActivity()); // (Context context)
                mLinearLayoutManagerHorizontal.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(mLinearLayoutManagerHorizontal);
            }

        });

        buttonView2 = (ImageButton) v.findViewById(R.id.view2);

        buttonView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity()); // (Context context)
                mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
            }
        });

        buttonView3 = (ImageButton) v.findViewById(R.id.view3);

        buttonView3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 3); // (Context context, int spanCount)
                recyclerView.setLayoutManager(mGridLayoutManager);
            }

        });

        buttonView4 = (ImageButton) v.findViewById(R.id.view4);

        buttonView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
                recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
            }
        });


    }

}

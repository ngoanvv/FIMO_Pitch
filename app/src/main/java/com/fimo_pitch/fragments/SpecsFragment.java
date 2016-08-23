package com.fimo_pitch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by TranManhTien on 23/08/2016.
 */
public class SpecsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static SpecsFragment newInstance(String param1, String param2) {
        SpecsFragment fragment = new SpecsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
}

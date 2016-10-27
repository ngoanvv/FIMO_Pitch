package com.fimo_pitch.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.model.SystemPitch;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by diep1_000 on 9/30/2016.
 */

public class CustomExpandListviewAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "CustomExpandAdapter";
    private Context mContext;
    private ArrayList<String> mHeaderGroup;
    private HashMap<String, ArrayList<SystemPitch>> mData;
    private LayoutInflater inflater;
    public CustomExpandListviewAdapter(Context context, ArrayList<String> headerGroup, HashMap<String, ArrayList<SystemPitch>> datas) {
        mContext = context;
        mHeaderGroup = headerGroup;
        mData = datas;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getGroupCount() {
        return mHeaderGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mData.get(mHeaderGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        Log.d(TAG,mHeaderGroup.get(groupPosition)+"");
        return mHeaderGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mData.get(mHeaderGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_group, parent, false);
//            TextView tvHeader = (TextView) convertView.findViewById(R.id.tv_header);

        }

//        tvHeader.setText(mHeaderGroup.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView != null)
        {
            convertView = inflater.inflate(R.layout.item_group_child, parent, false);
            return convertView;

        }

            return inflater.inflate(R.layout.item_group_child, parent, false);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

package com.fimo_pitch.adapter;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.model.SearchPitchModel;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

/**
 * Created by TranManhTien on 22/08/2016.
 */
public class SearchSystemAdapter extends RecyclerView.Adapter<SearchSystemAdapter.MyViewHolder> implements View.OnClickListener{

    private Activity activity;
    private String TAG=SearchSystemAdapter.class.getName();
    private ArrayList<SearchPitchModel> data;
    private LayoutInflater inflater;
    private int callRequest = 1;
    private OkHttpClient client;
    private OkHttpClient okHttpClient;
    private String currentphone="";

    public SearchSystemAdapter(Activity context, ArrayList<SearchPitchModel> data) {
        this.activity = context;
        this.data = data;
        this.inflater = LayoutInflater.from(activity.getApplicationContext());
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_search_pitch, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_name.setText(data.get(position).getName());
        holder.tv_des.setText(data.get(position).getAddress());
        holder.tv_size.setText(data.get(position).getTime_start().substring(0,5)+"-"+data.get(position).getTime_end().substring(0,5));
        holder.tv_type.setText(data.get(position).getPhone());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CALL_PHONE}, callRequest);
                        currentphone=data.get(position).getPhone();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        LinearLayout wrapper;
        Button edit,del;
        TextView tv_time,tv_des,tv_size,tv_type;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.pitch_name);
            tv_des = (TextView) itemView.findViewById(R.id.pitch_description);
            tv_size = (TextView) itemView.findViewById(R.id.pitch_size);
            tv_type = (TextView) itemView.findViewById(R.id.pitch_type);
            wrapper = (LinearLayout) itemView.findViewById(R.id.ll_match);
            edit    = (Button) itemView.findViewById(R.id.bt_edit);
            del     = (Button) itemView.findViewById(R.id.bt_delete);
        }


    }

}

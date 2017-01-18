package com.fimo_pitch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.model.TimeTable;
import com.fimo_pitch.support.ShowToast;

import java.util.ArrayList;

/**
 * Created by TranManhTien on 22/08/2016.
 */
public class PitchManagementAdapter extends RecyclerView.Adapter<PitchManagementAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private String TAG=PitchManagementAdapter.class.getName();
    private ArrayList<Pitch> data;
    private LayoutInflater inflater;
    private int callRequest = 1;


    public PitchManagementAdapter(Context context, ArrayList<Pitch> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public PitchManagementAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_management_pitch, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_name.setText(data.get(position).getName());
        holder.tv_des.setText(data.get(position).getDescription());
        holder.tv_size.setText(data.get(position).getSize());
        holder.tv_type.setText(data.get(position).getType());
        holder.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowToast.showToastLong(context,position+"");
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
        TextView tv_time,tv_des,tv_size,tv_type;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.pitch_name);
            tv_des = (TextView) itemView.findViewById(R.id.pitch_description);
            tv_size = (TextView) itemView.findViewById(R.id.pitch_size);
            tv_type = (TextView) itemView.findViewById(R.id.pitch_type);
            wrapper = (LinearLayout) itemView.findViewById(R.id.ll_match);

        }


    }
}

package com.fimo_pitch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.main.SearchOrderActivity;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.model.UserModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TranManhTien on 22/08/2016.
 */
public class PitchAdapter extends RecyclerView.Adapter<PitchAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private String TAG=PitchAdapter.class.getName();
    private ArrayList<Pitch> data;
    private List<String> listName;
    private SystemPitch systemPitch;
    private UserModel userModel;
    private LayoutInflater inflater;
    private int callRequest = 1;



    public PitchAdapter(Context context, ArrayList<Pitch> data,List<String> listName,SystemPitch systemPitch,UserModel userModel) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
        this.listName = listName;
        this.systemPitch =systemPitch;
        this.userModel = userModel;
    }

    @Override
    public PitchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_pitch, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_name.setText(data.get(position).getName());
        holder.tv_des.setText(data.get(position).getDescription());
        holder.tv_size.setText(data.get(position).getSize());
        holder.tv_type.setText(data.get(position).getType());
        holder.btShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SearchOrderActivity.class);
                intent.putExtra(CONSTANT.LISTPITCH_DATA, (Serializable) listName);
                intent.putExtra(CONSTANT.LISTPITCH,  (Serializable) data);
                intent.putExtra(CONSTANT.SystemPitch_MODEL,systemPitch);
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                intent.putExtra("pos",position);
                Log.d("list",data.toString());
                context.startActivity(intent);
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
        Button btShow;
        TextView tv_time,tv_des,tv_size,tv_type;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.pitch_name);
            tv_des = (TextView) itemView.findViewById(R.id.pitch_description);
            tv_size = (TextView) itemView.findViewById(R.id.pitch_size);
            tv_type = (TextView) itemView.findViewById(R.id.pitch_type);
            btShow = (Button) itemView.findViewById(R.id.btShow);
            wrapper = (LinearLayout) itemView.findViewById(R.id.ll_match);
        }


    }
}

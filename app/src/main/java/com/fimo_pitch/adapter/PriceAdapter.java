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
import com.fimo_pitch.model.Price;
import com.fimo_pitch.model.TimeTable;

import java.util.ArrayList;

/**
 * Created by TranManhTien on 22/08/2016.
 */
public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private String TAG=PriceAdapter.class.getName();
    private ArrayList<Price> data;
    private LayoutInflater inflater;


    public PriceAdapter(Context context, ArrayList<Price> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public PriceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_price, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        holder.tv_des.setText(data.get(position).getDescription());
//        holder.tv_date.setText(data.get(position).getDayOfWeek());
//        holder.tv_time.setText(data.get(position).getTimeStart().substring(0,5)+"-"+data.get(position).getTimeEnd().substring(0,5));
//        holder.tv_price.setText(data.get(position).getPrice());
//        holder.btEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }


    @Override
    public int getItemCount() {

        return data.size();
    }

    private Price getPitch(int position){

        return data.get(position);
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
        ImageView imageView;
        TextView tv_name;
        TextView tv_time,tv_des,tv_price,tv_date;
        LinearLayout wrapper;
        Button btEdit;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.pitch_name);
            tv_des = (TextView) itemView.findViewById(R.id.pitch_description);
            tv_time = (TextView) itemView.findViewById(R.id.pitch_time);
            tv_date = (TextView) itemView.findViewById(R.id.item_date);
            tv_price = (TextView) itemView.findViewById(R.id.item_price);
            btEdit = (Button) itemView.findViewById(R.id.bt_edit);

        }


    }
}

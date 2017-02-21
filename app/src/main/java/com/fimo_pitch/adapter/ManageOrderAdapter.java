package com.fimo_pitch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.model.Order;

import java.util.ArrayList;

/**
 * Created by TranManhTien on 22/08/2016.
 */
public class ManageOrderAdapter extends RecyclerView.Adapter<ManageOrderAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private String TAG=ManageOrderAdapter.class.getName();
    private ArrayList<Order> data;
    private LayoutInflater inflater;
    private int callRequest = 1;


    public ManageOrderAdapter(Context context, ArrayList<Order> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public ManageOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_manage_order, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


    }
    public OnCallEvent mOnCallEvent;
    public void setOnCallEvent(OnCallEvent onCallEvent)
    {
        this.mOnCallEvent = onCallEvent;
    }
    public interface OnCallEvent
    {
        public void onCallEvent(String number);
    }


    @Override
    public int getItemCount() {

        return data.size();
    }

    private Order getPitch(int position){

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
        TextView tv_address,tv_time,tv_day,tv_phone,tv_userName;
        LinearLayout wrapper;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.pitch_name);
            tv_address = (TextView) itemView.findViewById(R.id.order_address);
            tv_day = (TextView) itemView.findViewById(R.id.order_day);
            tv_time = (TextView) itemView.findViewById(R.id.pitch_time);
            tv_phone = (TextView) itemView.findViewById(R.id.order_phone);
            tv_phone = (TextView) itemView.findViewById(R.id.order_userName);

        }


    }
}

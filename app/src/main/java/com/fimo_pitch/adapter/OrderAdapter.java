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
import com.fimo_pitch.model.TimeTable;
import com.fimo_pitch.support.ShowToast;

import java.util.ArrayList;

/**
 * Created by TranManhTien on 22/08/2016.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private String TAG=OrderAdapter.class.getName();
    private ArrayList<TimeTable> data;
    private LayoutInflater inflater;
    private int callRequest = 1;


    public OrderAdapter(Context context, ArrayList<TimeTable> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_order, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_des.setText(data.get(position).getDescription());
        holder.tv_time.setText(data.get(position).getStart_time().substring(0,5)+"-"+data.get(position).getEnd_time().substring(0,5));
        holder.tv_type.setText(data.get(position).getType());

        holder.btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(data.get(position).getPhone()!=null) {
                   ShowToast.showToastLong(context,data.get(position).getPhone()+" ");
                   mOnCallEvent.onCallEvent(data.get(position).getPhone());
               }
               else mOnCallEvent.onCallEvent("null");

            }
        });
        holder.btBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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

    private TimeTable getPitch(int position){

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
        TextView tv_time,tv_des,tv_size,tv_type,tv_price;
        LinearLayout wrapper;
        Button btBook,btCall;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_des = (TextView) itemView.findViewById(R.id.pitch_description);
            tv_time = (TextView) itemView.findViewById(R.id.pitch_time);
            tv_price = (TextView) itemView.findViewById(R.id.item_price);

            tv_type = (TextView) itemView.findViewById(R.id.item_typeDate);
            btBook = (Button) itemView.findViewById(R.id.bt_book);
            btCall = (Button) itemView.findViewById(R.id.bt_call);

        }


    }
}

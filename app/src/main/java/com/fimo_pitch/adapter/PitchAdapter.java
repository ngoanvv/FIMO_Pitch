package com.fimo_pitch.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.main.DetailActivity;
import com.fimo_pitch.main.PaymentActivity;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.model.TimeTable;
import com.fimo_pitch.support.TrackGPS;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by TranManhTien on 22/08/2016.
 */
public class PitchAdapter extends RecyclerView.Adapter<PitchAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private String TAG=PitchAdapter.class.getName();
    private ArrayList<TimeTable> data;
    private LayoutInflater inflater;
    private int callRequest = 1;


    public PitchAdapter(Context context, ArrayList<TimeTable> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
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
        holder.tv_time.setText(data.get(position).getStart_time().substring(0,5)+"-"+data.get(position).getEnd_time().substring(0,5));
        holder.tv_size.setText(data.get(position).getType());

        holder.btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(data.get(position).getPhone().length()>0)
               mOnCallEvent.onCallEvent(data.get(position).getPhone());
               else
               mOnCallEvent.onCallEvent("null");

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
        TextView tv_name;
        TextView tv_time,tv_des,tv_size,tv_type;
        LinearLayout wrapper;
        Button btBook,btCall;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.pitch_name);
            tv_des = (TextView) itemView.findViewById(R.id.pitch_description);
            tv_time = (TextView) itemView.findViewById(R.id.pitch_time);
            tv_size = (TextView) itemView.findViewById(R.id.pitch_size);
            tv_type = (TextView) itemView.findViewById(R.id.pitch_type);
            btBook = (Button) itemView.findViewById(R.id.bt_book);
            btCall = (Button) itemView.findViewById(R.id.bt_call);

        }


    }
}

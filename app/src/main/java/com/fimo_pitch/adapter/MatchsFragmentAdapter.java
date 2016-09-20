package com.fimo_pitch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.ui.MatchActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by TranManhTien on 22/08/2016.
 */
public class MatchsFragmentAdapter extends RecyclerView.Adapter<MatchsFragmentAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<Pitch> data;

    private LayoutInflater inflater;


    public MatchsFragmentAdapter(Context context, ArrayList<Pitch> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public MatchsFragmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.match_item, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if(position==0) Picasso.with(context).load(R.drawable.ic_pitch).resize(0,400).into(holder.imageView);
        if(position==1) Picasso.with(context).load(R.drawable.ic_pitch2).resize(0,650).into(holder.imageView);
        if(position==2) Picasso.with(context).load(R.drawable.ic_pitch2).resize(0,650).into(holder.imageView);
        if(position==3) Picasso.with(context).load(R.drawable.ic_pitch2).resize(0,650).into(holder.imageView);
        if(position==4) Picasso.with(context).load(R.drawable.ic_pitch).resize(0,400).into(holder.imageView);
        if(position==5) Picasso.with(context).load(R.drawable.ic_pitch2).resize(0,650).into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    private Pitch getPitch(int position){

        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textviewname;
        TextView textviewaddress;

        public MyViewHolder(View itemView) {
            super(itemView);
            textviewname = (TextView) itemView.findViewById(R.id.item_name);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
            textviewaddress = (TextView) itemView.findViewById(R.id.item_address);
        }


    }
}

package com.fimo_pitch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.object.Pitch;

import java.util.ArrayList;

/**
 * Created by TranManhTien on 22/08/2016.
 */
public class MatchFragmentAdapter extends RecyclerView.Adapter<MatchFragmentAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<Pitch> data;

    private LayoutInflater inflater;

    private int previousPosition = 0;

    public MatchFragmentAdapter(Context context, ArrayList<Pitch> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public MatchFragmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.match_item, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.imageView.setImageResource(data.get(position).getImage());
        holder.textviewname.setText(data.get(position).getName());
        holder.textviewaddress.setText(data.get(position).getAddress());

        Log.d("Name", data.get(position).getName());
        Log.d("Address", data.get(position).getAddress());
        previousPosition = position;
    }

    @Override
    public int getItemCount() {
        Log.d("Data size", data.size() + "");
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textviewname;
        TextView textviewaddress;

        public MyViewHolder(View itemView) {
            super(itemView);

            textviewname = (TextView) itemView.findViewById(R.id.name);
            imageView = (ImageView) itemView.findViewById(R.id.img_match);
            textviewaddress = (TextView) itemView.findViewById(R.id.address);
        }
    }
}

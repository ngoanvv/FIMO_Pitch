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
import com.fimo_pitch.object.Pitch;
import com.fimo_pitch.sub_activity.MatchActivity;

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

        holder.imageView.setImageResource(data.get(position).getImage());
        holder.textviewname.setText(data.get(position).getName());
        holder.textviewaddress.setText(data.get(position).getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MatchActivity.class);
                intent.putExtra("ObjMatch",data.get(position));
                context.startActivity(intent);
            }
        });
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


            textviewname = (TextView) itemView.findViewById(R.id.name);
            imageView = (ImageView) itemView.findViewById(R.id.img_match);
            textviewaddress = (TextView) itemView.findViewById(R.id.address);
        }


    }
}

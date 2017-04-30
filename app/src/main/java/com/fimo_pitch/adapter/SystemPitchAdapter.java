package com.fimo_pitch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.main.DetailActivity;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by TranManhTien on 22/08/2016.
 */
public class SystemPitchAdapter extends RecyclerView.Adapter<SystemPitchAdapter.MyViewHolder>
                                    implements Filterable
{

    private Context context;
    private String TAG=SystemPitchAdapter.class.getName();
    private ArrayList<SystemPitch> data;
    private UserModel userModel;
    private LayoutInflater inflater;
    private ArrayList<SystemPitch> results;
    private ArrayList<SystemPitch> list;
    private ArrayList<SystemPitch> copyData;

    public SystemPitchAdapter(Context context, ArrayList<SystemPitch> data,UserModel u) {
        this.context = context;
        this.data = data;
        this.copyData = data;
        this.userModel = u;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public SystemPitchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_systempitch, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Random random =new Random();
        int x = random.nextInt(10 - 1 + 1) + 1;
        if(position%2==0) {
            Picasso.with(context).load(R.drawable.ic_pitch2).resize(0,400).into(holder.imageView);
        }
        else
            Picasso.with(context).load(R.drawable.ic_pitch).resize(0, 400).into(holder.imageView);
        holder.textviewaddress.setText(data.get(position).getAddress());
        holder.textviewname.setText(data.get(position).getName());
        holder.textviewComment.setText(data.get(position).getComment());
        holder.textviewRating.setText("3.2");
        holder.textviewOwner.setText(data.get(position).getOwnerName());


        holder.item_pitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(CONSTANT.SystemPitch_MODEL,data.get(position));
                intent.putExtra(CONSTANT.KEY_USER,userModel);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(data == null) return 0;
        return data.size();
    }

    private SystemPitch getPitch(int position){

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
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<SystemPitch>) results.values;
                data = list;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<SystemPitch> filteredArray = getFilteredResults(constraint);
                results.count = filteredArray.size();
                results.values = filteredArray;
                return results;
            }
        };

        return filter;
    }

    private ArrayList<SystemPitch> getFilteredResults(CharSequence constraint) {

        int count=0;
        results = new ArrayList<>();
        constraint=constraint.toString().toLowerCase();
        for(int i =0;i<copyData.size();i++)
        {
            if(copyData.get(i).getAddress().toLowerCase().contains(constraint) ||
                    copyData.get(i).getName().toLowerCase().contains(constraint))
            {
                results.add(copyData.get(i));
                count++;
            }
        }
        return results;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_pitch;
        ImageView imageView;
        TextView textviewname;
        TextView textviewaddress;
        TextView textviewRating;
        TextView textviewComment,textviewOwner;

        public MyViewHolder(View itemView) {
            super(itemView);
            textviewname = (TextView) itemView.findViewById(R.id.item_name);
            imageView = (ImageView) itemView.findViewById(R.id.imageSystem);
            textviewaddress = (TextView) itemView.findViewById(R.id.item_address);
            textviewRating = (TextView) itemView.findViewById(R.id.item_rating);
            textviewComment = (TextView) itemView.findViewById(R.id.item_comment);
            textviewOwner = (TextView) itemView.findViewById(R.id.item_ownerName);

            item_pitch = (LinearLayout) itemView.findViewById(R.id.itempitch);

        }


    }
}

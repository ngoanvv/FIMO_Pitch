package com.fimo_pitch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.main.DetailActivity;
import com.fimo_pitch.model.SystemPitch;
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

    private LayoutInflater inflater;
    private ArrayList<SystemPitch> results;
    private ArrayList<SystemPitch> list;


    public SystemPitchAdapter(Context context, ArrayList<SystemPitch> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public SystemPitchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_pitch, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Random random =new Random();
        int x = random.nextInt(10 - 1 + 1) + 1;
        if(x%2==0)  Picasso.with(context).load(R.drawable.ic_pitch).resize(0,400).into(holder.imageView);
        else Picasso.with(context).load(R.drawable.ic_pitch2).resize(0,650).into(holder.imageView);

        holder.textviewaddress.setText(data.get(position).getAddress());
        holder.textviewname.setText(data.get(position).getName());
        holder.textviewComment.setText(data.get(position).getComment());
        holder.textviewRating.setText(data.get(position).getRating());
        holder.textviewOwner.setText(data.get(position).getOwnerName());


        holder.item_pitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DetailActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {

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
    public void initList() {
        list = new ArrayList<>();
        SystemPitch systemPitch = new SystemPitch();
        systemPitch.setAddress("144 Xuân thủy, Cầu giấy");
        systemPitch.setComment("20");
        systemPitch.setOwnerName("Phương LX");
        systemPitch.setContact("0989238923");
        systemPitch.setName("Sân Trần quốc hoàn");
        systemPitch.setRating("3.5");
        list.add(systemPitch);

        SystemPitch systemPitch1 = new SystemPitch();

        systemPitch1.setAddress("223 Xuân thủy, Cầu giấy");
        systemPitch1.setComment("20");
        systemPitch1.setOwnerName("Dương LX");
        systemPitch1.setContact("0989238923");
        systemPitch1.setName("Sân LÊ quốc hoàn");
        systemPitch1.setRating("3.5");
        list.add(systemPitch1);

        SystemPitch systemPitch2 = new SystemPitch();

        systemPitch2.setAddress("456 Xuân thủy, Cầu giấy");
        systemPitch2.setComment("20");
        systemPitch2.setOwnerName("Hoàng LX");
        systemPitch2.setContact("0989238923");
        systemPitch2.setName("Sân Trần Đại Nghĩa");
        systemPitch2.setRating("3.5");
        list.add(systemPitch2);

        SystemPitch systemPitch3 = new SystemPitch();

        systemPitch3.setAddress("124 Xuân thủy, Cầu giấy");
        systemPitch3.setComment("20");
        systemPitch3.setOwnerName("Hoàng LX");
        systemPitch3.setContact("0989238923");
        systemPitch3.setName("Sân Trần Đại Nghĩa");
        systemPitch3.setRating("3.5");
        list.add(systemPitch3);

        SystemPitch systemPitch4 = new SystemPitch();

        systemPitch4.setAddress("987 Xuân thủy, Cầu giấy");
        systemPitch4.setComment("20");
        systemPitch4.setOwnerName("Hoàng LX");
        systemPitch4.setContact("0989238923");
        systemPitch4.setName("Sân Trần Đại Nghĩa");
        systemPitch4.setRating("3.5");
        list.add(systemPitch4);

    }
    private ArrayList<SystemPitch> getFilteredResults(CharSequence constraint) {
        Log.d(TAG,"constraint "+constraint.toString());

        int count=0;
        results = new ArrayList<>();
        constraint=constraint.toString().toLowerCase();
        for(int i =0;i<list.size();i++)
        {
            if(list.get(i).getAddress().toLowerCase().contains(constraint) ||
                    list.get(i).getName().toLowerCase().contains(constraint)
                    )
            {
                results.add(list.get(i));
                count++;
            }
        }
        Log.d(TAG,"count "+count);
        Log.d(TAG,"result size: "+results.size());
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
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
            textviewaddress = (TextView) itemView.findViewById(R.id.item_address);
            textviewRating = (TextView) itemView.findViewById(R.id.item_rating);
            textviewComment = (TextView) itemView.findViewById(R.id.item_comment);
            textviewOwner = (TextView) itemView.findViewById(R.id.item_ownerName);

            item_pitch = (LinearLayout) itemView.findViewById(R.id.itempitch);

        }


    }
}

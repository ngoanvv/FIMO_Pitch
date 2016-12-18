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
import com.fimo_pitch.main.PaymentActivity;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.model.SystemPitch;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by TranManhTien on 22/08/2016.
 */
public class PitchAdapter extends RecyclerView.Adapter<PitchAdapter.MyViewHolder>
                                    implements Filterable
{

    private Context context;
    private String TAG=PitchAdapter.class.getName();
    private ArrayList<Pitch> data;
    private LayoutInflater inflater;
    private ArrayList<Pitch> results;
    private ArrayList<Pitch> list;


    public PitchAdapter(Context context, ArrayList<Pitch> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public PitchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_order, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        holder.tv_name.setText(list.get(position).getName());
        if(position%2==0)
        {
//            holder.wrapper.setBackgroundResource(R.color.com_facebook_blue);
        }
        else
        {

        }
        holder.bt_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PaymentActivity.class);
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
                list = (ArrayList<Pitch>) results.values;
                data = list;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<Pitch> filteredArray = getFilteredResults(constraint);
                results.count = filteredArray.size();
                results.values = filteredArray;
                return results;
            }
        };

        return filter;
    }
    private ArrayList<Pitch> getFilteredResults(CharSequence constraint) {
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
        return results;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_name;
        TextView tv_money;
        TextView tv_price;
        TextView tv_time,bt_order;
        LinearLayout wrapper;
        public MyViewHolder(View itemView) {
            super(itemView);
//            tv_name = (TextView) itemView.findViewById(R.id.item_name);
//            imageView = (ImageView) itemView.findViewById(R.id.item_image);
//            tv_money = (TextView) itemView.findViewById(R.id.item_money);
//            tv_price = (TextView) itemView.findViewById(R.id.item_price);
//            tv_time = (TextView) itemView.findViewById(R.id.item_time);
            bt_order = (TextView) itemView.findViewById(R.id.bt_order);
            wrapper = (LinearLayout) itemView.findViewById(R.id.wrapper);

        }


    }
}

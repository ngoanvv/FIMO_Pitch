package com.fimo_pitch.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.model.News;

import java.util.ArrayList;


/**
 * Created by Diep_Chelsea on 13/07/2016.
 */
public class NewsFragmentAdapter extends RecyclerView.Adapter<NewsFragmentAdapter.RecyclerViewHolder> implements Filterable{
    public static String TAG="FindMacthAdapter";
    public ArrayList<News> list;
    public Activity context;
    public NewsFragmentAdapter(Activity context, ArrayList<News> listData) {
        this.context = context;
        this.list = listData;

    }
    ArrayList<News> results;

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemview = inflater.inflate(R.layout.item_news, parent, false);
            return new RecyclerViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {

        holder.tv_descrip.setText(list.get(position).getDescription());
        holder.tv_hostname.setText(list.get(position).getHostName());
        holder.tv_time.setText(list.get(position).getTime());
        holder.tv_location.setText(list.get(position).getLocation());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                list = (ArrayList<News>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<News> filteredArray = getFilteredResults(constraint);
                results.count = filteredArray.size();
                results.values = filteredArray;
                return results;
            }
        };

        return filter;
    }
    public void updateData(ArrayList<News> myData)
    {
        Log.d("newsAdapter","old : "+list.size()+" new "+myData.size());
        list.clear();
        list.addAll(myData);
        notifyDataSetChanged();
    }
    private ArrayList<News> getFilteredResults(CharSequence constraint) {

        int count=0;
        results = new ArrayList<>();
        constraint=constraint.toString().toLowerCase();
        for(int i =0;i<list.size();i++)
        {
            if(list.get(i).getLocation().toLowerCase().contains(constraint) || list.get(i).getTitle().toLowerCase().contains(constraint)
              ||      list.get(i).getHostName().toLowerCase().contains(constraint) ||list.get(i).getDescription().toLowerCase().contains(constraint) ||
            list.get(i).getMoney().toLowerCase().contains(constraint) || list.get(i).getTime().toLowerCase().contains(constraint)
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
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tv_hostname,tv_time,tv_location,tv_descrip;
        LinearLayout ll_match;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_hostname = (TextView) itemView.findViewById(R.id.items_name);
            tv_time = (TextView) itemView.findViewById(R.id.item_time);
            tv_location = (TextView) itemView.findViewById(R.id.item_location);
            tv_descrip = (TextView) itemView.findViewById(R.id.item_title);
            ll_match = (LinearLayout) itemView.findViewById(R.id.ll_match);
        }

    }


}

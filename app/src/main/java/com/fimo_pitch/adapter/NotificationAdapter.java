package com.fimo_pitch.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.model.Match;
import com.fimo_pitch.model.NotificationModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Diep_Chelsea on 28/03/2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{

    public static String TAG="FindMacthAdapter";
    public ArrayList<NotificationModel> list;
    public Activity context;
    public ArrayList<NotificationModel> results;
    private LayoutInflater inflater;
    public NotificationAdapter(Activity context, ArrayList<NotificationModel> listData) {
        this.context = context;
        this.list = listData;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
        Log.d("list",listData.size()+"");

    }

    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_listview_notification, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.MyViewHolder holder, int position) {
        Picasso.with(context).load(R.drawable.ic_avatar).resize(100,100).placeholder(R.drawable.ic_avatar).error(R.drawable.ic_avatar).resize(100,100).into(holder.avatar);
        holder.content.setText(list.get(position).getMessage());
        holder.time.setText(list.get(position).getDate());
//        if(list.get(position).isRead()==true) convertView.setBackgroundColor(Color.parseColor("#ffffff"));
//        else convertView.setBackgroundColor(Color.parseColor("#FFCEFFD0"));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView avatar;
        TextView content;
        TextView time;

        public MyViewHolder(View itemView) {
            super(itemView);
            avatar = (RoundedImageView) itemView.findViewById(R.id.notification_avatar);
            content = (TextView) itemView.findViewById(R.id.notification_content);
            time = (TextView) itemView.findViewById(R.id.notification_date);
        }
    }

//            holder = new ViewHolder();
//            convertView = mLayoutInflater.inflate(R.layout.item_listview_notification,null);
//            holder.tvNotification = (TextView) convertView.findViewById(R.id.notification_content);
//            holder.imgAvatar = (RoundedImageView) convertView.findViewById(R.id.notification_avatar);
//            holder.tvTime = (TextView) convertView.findViewById(R.id.notification_date);
//            convertView.setTag(holder);



}

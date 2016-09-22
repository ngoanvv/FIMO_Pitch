package com.fimo_pitch.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.model.NotificationModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Diep_Chelsea on 28/03/2016.
 */
public class NotificationAdapter extends BaseAdapter {

    private ArrayList<NotificationModel> notificationModels;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> notificationModels) {
        this.mContext = context;
        this.notificationModels = notificationModels;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return notificationModels.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificationModel notificationModel = notificationModels.get(position);
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_listview_notification,null);
            holder.tvNotification = (TextView) convertView.findViewById(R.id.notification_content);
            holder.imgAvatar = (RoundedImageView) convertView.findViewById(R.id.notification_avatar);
            holder.tvTime = (TextView) convertView.findViewById(R.id.notification_date);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(mContext).load(R.drawable.ic_avatar).resize(100,100).placeholder(R.drawable.ic_avatar).error(R.drawable.ic_avatar).into(holder.imgAvatar);
        holder.tvNotification.setText(Html.fromHtml("<strong>"+notificationModel.getName()+"</strong>" + " " + notificationModel.getMessage()));
        holder.tvTime.setText(notificationModel.getDate());
        if(notificationModel.isRead()==true) convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        else convertView.setBackgroundColor(Color.parseColor("#FFCEFFD0"));
            return convertView;
    }

    class ViewHolder{
        RoundedImageView imgAvatar;
        TextView tvNotification;
        TextView tvTime;
        TextView tvContent;
    }
}

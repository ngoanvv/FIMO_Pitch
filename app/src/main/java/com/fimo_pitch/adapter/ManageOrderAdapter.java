package com.fimo_pitch.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fimo_pitch.API;
import com.fimo_pitch.R;
import com.fimo_pitch.model.Order;
import com.fimo_pitch.support.NetworkUtils;
import com.fimo_pitch.support.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by TranManhTien on 22/08/2016.
 */
public class ManageOrderAdapter extends RecyclerView.Adapter<ManageOrderAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private String TAG=ManageOrderAdapter.class.getName();
    private ArrayList<Order> data;
    private LayoutInflater inflater;
    private int callRequest = 1;
    private OkHttpClient okHttpClient;

    public ManageOrderAdapter(Context context, ArrayList<Order> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public ManageOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_manage_order, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Log.d(TAG,data.get(position).toString());
        holder.tv_name.setText(data.get(position).getUserName());
        holder.tv_time.setText(data.get(position).getTime_start()+"-"+data.get(position).getTime_end());
        holder.tv_day.setText(data.get(position).getDay());
        holder.tv_phone.setText(data.get(position).getUserPhone());
        holder.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okHttpClient = new OkHttpClient();
                HashMap<String,String> body = new HashMap<String, String>();
                body.put("id",data.get(position).getId());
                body.put("user_id",data.get(position).getUserId());
                body.put("pitch_id",data.get(position).getPitchId());
                body.put("status","1");

                new UpdateOrder(body,position).execute();


            }
        });
        holder.btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okHttpClient = new OkHttpClient();
                HashMap<String,String> body = new HashMap<String, String>();
                body.put("id",data.get(position).getId());
                body.put("user_id",data.get(position).getUserId());
                body.put("pitch_id",data.get(position).getPitchId());
                body.put("status","2");

                new UpdateOrder(body,position).execute();

            }
        });

    }
    public class UpdateOrder extends AsyncTask<String,String,String>
    {
        OkHttpClient client;
        HashMap<String,String> body;
        private ProgressDialog progressDialog;
        int position;

        public UpdateOrder(HashMap<String,String> body,int position)
        {
            this.position = position;
            this.body = body;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            client = new OkHttpClient();
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Response response =
                        client.newCall(NetworkUtils.createPutRequest(API.UpdateOrder,
                                this.body)).execute();
                if (response.isSuccessful()) {
                    String results = response.body().string();
                    Log.d("run", results);
                    return results;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "failed";
            }
            return "failed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.d(TAG,s);
            data.remove(position);
            notifyDataSetChanged();
            notifyItemRangeRemoved(position,data.size());
            notifyItemRemoved(position);
        }
    }
    @Override
    public int getItemCount() {

        return data.size();
    }

    private Order getPitch(int position){

        return data.get(position);
    }

    private class MyTask extends AsyncTask<String,String,String>
    {
        String mUrl;
        HashMap<String,String> param;
        private ProgressDialog progressDialog;

        public MyTask(String url,HashMap<String,String> body)
        {
            this.param=body;
            this.mUrl = url;
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Response response = okHttpClient.newCall(NetworkUtils.createPostRequest(mUrl,this.param)).execute();
                if (response.isSuccessful()) {
                    String results = response.body().string();
                    Log.d("run", results);
                    return results;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "failed";
            }
            return "failed";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s != "failed") {

            }
            else
            {
                Utils.openDialog(context,"Đã có lỗi xảy ra ");
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
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
        TextView tv_name;
        TextView tv_time,tv_day,tv_phone,tv_userName;
        Button btOk,btCancel;
        LinearLayout wrapper;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.order_userName);
            tv_day = (TextView) itemView.findViewById(R.id.order_day);
            tv_time = (TextView) itemView.findViewById(R.id.order_time);
            tv_phone = (TextView) itemView.findViewById(R.id.order_phone);
            btOk = (Button) itemView.findViewById(R.id.bt_ok);
            btCancel = (Button) itemView.findViewById(R.id.bt_cancel);


        }


    }
}

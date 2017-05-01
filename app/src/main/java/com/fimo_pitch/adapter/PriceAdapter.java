package com.fimo_pitch.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.main.EditPriceActivity;
import com.fimo_pitch.model.Price;
import com.fimo_pitch.support.NetworkUtils;
import com.fimo_pitch.support.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by TranManhTien on 22/08/2016.
 */
public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private String TAG=PriceAdapter.class.getName();
    private ArrayList<Price> data;
    private LayoutInflater inflater;
    private OkHttpClient okHttpClient;
    private OkHttpClient client;

    public PriceAdapter(Context context, ArrayList<Price> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public PriceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_price, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_des.setText(data.get(position).getDescription());

        if(data.get(position).getDayOfWeek().equals("1"))
        holder.tv_date.setText("Ngày nghỉ");
        else holder.tv_date.setText("Ngày thường");


        holder.tv_time.setText(data.get(position).getTime());
        holder.tv_price.setText(data.get(position).getPrice());
        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditPriceActivity.class);
                intent.putExtra(CONSTANT.PRICE,data.get(position));
                context.startActivity(intent);
                ((Activity)context).finish();

            }
});
        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeletePrice(data.get(position).getId(),position).execute();
            }
        });

    }


    @Override
    public int getItemCount() {

        return data.size();
    }

    private Price getPitch(int position){

        return data.get(position);
    }
    public class UpdateOrder extends AsyncTask<String,String,String> {
        OkHttpClient client;
        HashMap<String, String> body;
        private ProgressDialog progressDialog;
        int position;
        String id;

        public UpdateOrder(String id, HashMap<String, String> body, int position) {
            this.id = id;
            this.position = position;
            this.body = body;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s.contains("failed")) Utils.openDialog(context,"Thao tác thất bại. Hãy thử lại");
            else
            {
                Utils.openDialog(context,"Thao tác thành công");
                data.remove(position);
                notifyDataSetChanged();
                notifyItemRemoved(position);
                notifyItemRangeRemoved(position,data.size()-1);
            }
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
                        client.newCall(NetworkUtils.createPutRequest(API.UpdateOrder + "/" + id,
                                this.body)).execute();
                if (response.isSuccessful()) {
                    String results = response.body().string();
                    Log.d("run", results);
                    return results;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
            return "failed";
        }

    }
    class ChoiceDialog extends Dialog implements View.OnClickListener {
        String title;
        Context mContext;
        int pos ;
        public ChoiceDialog(Context context, String content,int position) {
            super(context);
            this.pos = position;
            this.title = content;
            this.mContext = context;
        }
        DialogInterface.OnClickListener clickYes;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_choice_image);
            TextView msg = (TextView) findViewById(R.id.id_message);
            msg.setText(title);
            Button yes = (Button) findViewById(R.id.id_dialog_ok);
            yes.setOnClickListener(this);
            Button no = (Button) findViewById(R.id.id_cancel);
            no.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.id_dialog_ok:
                    okHttpClient = new OkHttpClient();
                    HashMap<String, String> body = new HashMap<String, String>();
                    body.put("id", data.get(pos).getId());
                    body.put("status", "0");

                    dismiss();

                    break;
                case R.id.id_cancel:
                    dismiss();
                    break;
                default:
                    break;
            }
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
    class DeletePrice extends AsyncTask<String,String,String>
    {
        ProgressDialog progressDialog;
        String id;
        int position;
        public DeletePrice(String id,int position)
        {
            this.position = position;
            this.id = id;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                client = new OkHttpClient();
                Response response =
                        client.newCall(NetworkUtils.createDeleteRequest(API.DeletePrice+this.id)).execute();
                String results = response.body().string();
                Log.d("delete", results);
                if (response.isSuccessful()) {
                    return results;
                }

            }
            catch (Exception e)
            {
                return "failed";
            }
            return "failed";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.contains("success")) {
                data.remove(position);
                notifyDataSetChanged();
                notifyItemRemoved(position);
                notifyItemRangeRemoved(position,data.size());
            }
            progressDialog.dismiss();

        }

    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_name;
        TextView tv_time,tv_des,tv_price,tv_date;
        LinearLayout wrapper;
        Button btEdit,btDelete;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_des = (TextView) itemView.findViewById(R.id.item_description);
            tv_time = (TextView) itemView.findViewById(R.id.item_time);
            tv_date = (TextView) itemView.findViewById(R.id.item_date);
            tv_price = (TextView) itemView.findViewById(R.id.item_price);
            btEdit = (Button) itemView.findViewById(R.id.bt_edit);
            btDelete = (Button) itemView.findViewById(R.id.bt_delete);
        }


    }
}

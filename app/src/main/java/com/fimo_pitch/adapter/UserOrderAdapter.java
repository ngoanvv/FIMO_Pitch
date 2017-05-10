package com.fimo_pitch.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.fimo_pitch.R;
import com.fimo_pitch.model.TimeTable;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.NetworkUtils;
import com.fimo_pitch.support.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private String TAG=UserOrderAdapter.class.getName();
    private ArrayList<TimeTable> data;
    private LayoutInflater inflater;
    private int callRequest = 1;
    private UserModel userModel;
    private OkHttpClient okHttpClient;

    public UserOrderAdapter(Context context, ArrayList<TimeTable> data, UserModel u) {
        this.context = context;
        this.data = data;
        this.userModel = u;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public UserOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_user_order, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_day.setText(data.get(position).getDay());
//        holder.tv_price.setText(data.get(position).getPrice());
        holder.tv_time.setText(data.get(position).getStart_time()+"-"+data.get(position).getEnd_time());
        if(data.get(position).getType().equals("1")) {
            holder.tv_des.setText("Đã được chấp nhận");
            holder.btCancel.setVisibility(View.INVISIBLE);
        }
        else holder.tv_des.setText("Đang đợi");

        holder.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.get(position).getType().equals("2")) {
                    ChoiceDialog dialog = new ChoiceDialog(context,"Bạn có muốn hủy yêu cầu đặt sân này hay không ?",position);
                    dialog.show();
                }
                else
                {
                    Utils.openDialog(context,"Không thể hủy yêu càu đã được chấp nhận");
                }
            }

        });
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
        OnClickListener clickYes;
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
                    new UpdateOrder(data.get(pos).getId(), body, pos).execute();
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
                notifyItemRangeRemoved(position,data.size());
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
    @Override
    public int getItemCount() {

        return data.size();
    }

    private TimeTable getPitch(int position){

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
    public void onClick(View v) {
        int id = v.getId();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_time,tv_des,tv_size,tv_day,tv_price;
        LinearLayout wrapper;
        Button btCancel,btCall;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_des = (TextView) itemView.findViewById(R.id.order_descriptipn);
            tv_time = (TextView) itemView.findViewById(R.id.order_time);
            tv_price = (TextView) itemView.findViewById(R.id.order_price);

            tv_day = (TextView) itemView.findViewById(R.id.order_day);
            btCancel = (Button) itemView.findViewById(R.id.bt_cancel);

        }


    }
}

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
import com.fimo_pitch.custom.view.MyCustomDialog;
import com.fimo_pitch.model.TimeTable;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.NetworkUtils;
import com.fimo_pitch.support.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private String TAG=OrderAdapter.class.getName();
    private ArrayList<TimeTable> data;
    private LayoutInflater inflater;
    private int callRequest = 1;
    private UserModel userModel;
    ChoiceDialog dialog;
    public OrderAdapter(Context context, ArrayList<TimeTable> data,UserModel u) {
        this.context = context;
        this.data = data;
        this.userModel = u;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_order, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_time.setText(data.get(position).getStart_time().substring(0,5)+"-"+data.get(position).getEnd_time().substring(0,5));
        holder.tv_price.setText(data.get(position).getPrice());
        if(data.get(position).getType().contains("1"))
        holder.tv_type.setText("Ngày nghỉ");
        else holder.tv_type.setText("Ngày thường");
        holder.tv_des.setText(data.get(position).getDescription());

        holder.btBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> body = new HashMap<String, String>();
                body.put("user_id",userModel.getId());
                body.put("pitch_id",data.get(position).getPitchId());
                body.put("management_id",data.get(position).getManagement_id());
                body.put("status","2");
                body.put("token",userModel.getToken());
                body.put("day",data.get(position).getDay());

                Log.d("pitch id",data.get(position).getPitchId()+"-"+data.get(position).getManagement_id());
                dialog = new ChoiceDialog(context,"Bạn có muốn đặt sân này không ?",data.get(position),body,position);
                dialog.show();
            }
        });

    }
    class ChoiceDialog extends Dialog implements View.OnClickListener {
        String title;
        Context mContext;
        TimeTable timeTable;
        HashMap<String,String> params;
        int position;
        public ChoiceDialog(Context context, String content,TimeTable a,HashMap<String,String> bd,int p)
        {

            super(context);
            this.position=p;
            this.params=bd;
            this.timeTable=a;
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
                    new BookPitch(this.params,position).execute();
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


    public class BookPitch extends AsyncTask<String,String,String>
    {
        OkHttpClient client;
        HashMap<String,String> body;
        private ProgressDialog progressDialog;
        int position;
        public BookPitch(HashMap<String, String> body, int position)
        {
                this.body = body;
                this.position = position;
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
                        client.newCall(NetworkUtils.createPostRequest(API.BookPitch,
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
            Log.d(TAG, s);
            if (s.contains("success")) {
                notifyDataSetChanged();
                notifyItemRemoved(position);
                notifyItemRangeRemoved(position, data.size());
                MyCustomDialog dialog = new MyCustomDialog(context, context.getString(R.string.booksuccesss1)
                        + " "
                        + data.get(position).getPitchName()
                        + context.getString(R.string.booksuccesss2) + " "
                        + data.get(position).getStart_time() + " "
                        + context.getString(R.string.booksuccess3));
                dialog.show();
                data.remove(position);

            }
            else
            {
                Utils.openDialog(context,"Thao tác không thành công. Hãy thử lại !");
            }
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
        if(id==R.id.bt_book)
        {

        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_time,tv_des,tv_size,tv_type,tv_price;
        LinearLayout wrapper;
        Button btBook;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_des = (TextView) itemView.findViewById(R.id.pitch_description);
            tv_time = (TextView) itemView.findViewById(R.id.pitch_time);
            tv_price = (TextView) itemView.findViewById(R.id.item_price);
            tv_type = (TextView) itemView.findViewById(R.id.item_typeDate);
            btBook = (Button) itemView.findViewById(R.id.bt_book);

        }


    }
}

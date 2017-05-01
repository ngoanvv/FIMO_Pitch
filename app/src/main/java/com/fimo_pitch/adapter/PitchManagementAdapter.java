package com.fimo_pitch.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.main.EditPitchActivity;
import com.fimo_pitch.model.Pitch;
import com.fimo_pitch.support.NetworkUtils;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by TranManhTien on 22/08/2016.
 */
public class PitchManagementAdapter extends RecyclerView.Adapter<PitchManagementAdapter.MyViewHolder> implements View.OnClickListener {

    private Activity activity;
    private String TAG=PitchManagementAdapter.class.getName();
    private ArrayList<Pitch> data;
    private LayoutInflater inflater;
    private int callRequest = 1;
    private OkHttpClient client;


    public PitchManagementAdapter(Activity context, ArrayList<Pitch> data) {
        this.activity = context;
        this.data = data;
        this.inflater = LayoutInflater.from(activity.getApplicationContext());
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_management_pitch, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_name.setText(data.get(position).getName());
        holder.tv_des.setText(data.get(position).getDescription());
        holder.tv_size.setText(data.get(position).getSize());
        holder.tv_type.setText(data.get(position).getType());
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeletePitch(data.get(position).getId(),position).execute();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditPitchActivity.class);
                intent.putExtra(CONSTANT.PITCH_MODEL,data.get(position));
                intent.putExtra(CONSTANT.KEY_USER,activity.getIntent().getSerializableExtra(CONSTANT.KEY_USER));
                activity.startActivity(intent);

            }
        });
    }


    class DeletePitch extends AsyncTask<String,String,String>
    {
        ProgressDialog progressDialog;
        String id;
        int position;
        public DeletePitch(String id,int position)
        {
            this.position = position;
           this.id = id;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                client = new OkHttpClient();
                Response response =
                        client.newCall(NetworkUtils.createDeleteRequest(API.DeletePitch+this.id)).execute();
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
    @Override
    public int getItemCount() {
        return data.size();
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
        LinearLayout wrapper;
        Button edit,del;
        TextView tv_time,tv_des,tv_size,tv_type;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.pitch_name);
            tv_des = (TextView) itemView.findViewById(R.id.pitch_description);
            tv_size = (TextView) itemView.findViewById(R.id.pitch_size);
            tv_type = (TextView) itemView.findViewById(R.id.pitch_type);
            wrapper = (LinearLayout) itemView.findViewById(R.id.ll_match);
            edit    = (Button) itemView.findViewById(R.id.bt_edit);
            del     = (Button) itemView.findViewById(R.id.bt_delete);
        }


    }
}

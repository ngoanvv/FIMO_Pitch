package com.fimo_pitch.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.fimo_pitch.API;
import com.fimo_pitch.R;
import com.fimo_pitch.support.NetworkUtils;
import com.fimo_pitch.support.Utils;

import java.util.Calendar;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by TranManhTien on 23/08/2016.
 */
public class PostNewsFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public final int SOFTKEYBOARDHEIGHT=100;
    public static String TAG="PostNewsFragment";
    private EditText edt_time,edt_title,edt_description,edt_location;
    private Button bt_post;
    // 144 xuan thuy : lat : 21.036654, lng 105.781218
    private Button img_send;
    private OkHttpClient client;
    private String time=" ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_postnews, container, false);
        client = new OkHttpClient();
        initView(view);
        return view;
    }
    public void initView(View view)
    {
        edt_description = (EditText) view.findViewById(R.id.edt_description);
        edt_time = (EditText) view.findViewById(R.id.edt_time);
        edt_title = (EditText) view.findViewById(R.id.edt_title);
        edt_location = (EditText) view.findViewById(R.id.edt_location);
        img_send = (Button) view.findViewById(R.id.img_send);


        edt_time.setOnClickListener(this);
        img_send.setOnClickListener(this);

    }
    public static PostNewsFragment newInstance(String param1, String param2) {
        PostNewsFragment fragment = new PostNewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public void showTimePicker(final int year, final int month, final int day)
    {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(view.isShown())
                {
                    String time = hourOfDay + ":" + minute + " ngày " + day + "-" + month + "-" + year;
                    edt_time.setText(time.toString());
                }
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }
    public void showDatePicker()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (view.isShown())
                {
                    Log.d(TAG,"year : "+year +" month :"+monthOfYear+" day"+dayOfMonth);
                    showTimePicker(year,monthOfYear,dayOfMonth);
                }
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public boolean validate() {
        boolean valid = true;
        String description,address,title,time,money;
        description = edt_description.getText().toString().replaceAll("\\s+", " ");;
        time        = edt_time.getText().toString().replaceAll("\\s+", " ");
        title       = edt_title.getText().toString().replaceAll("\\s+", " ");
        address     = edt_location.getText().toString().replaceAll("\\s+", " ");

        if (description.isEmpty() || description.length() < 6)
        {
            edt_description.setError(getString(R.string.invalid_length));
            valid = false;
        }
        else
        {
            edt_description.setError(null);
        }

        if (time.isEmpty() || time.length() < 6 )
        {
            edt_time.setError(getString(R.string.invalid_length));
            valid = false;
        }
        else
        {
            edt_time.setError(null);
        }
        if (title.isEmpty() || title.length() < 6 )
        {
            edt_title.setError(getString(R.string.invalid_length));
            valid = false;
        }
        else
        {
            edt_title.setError(null);
        }
        if (address.isEmpty() || address.length() < 6 )
        {
            edt_location.setError(getString(R.string.invalid_length));
            valid = false;
        }
        else
        {
            edt_location.setError(null);
        }


        return valid;
    }
    class MyTask extends AsyncTask<String,String,String>
    {


        HashMap<String,String> param;
        ProgressDialog progressDialog;

        public MyTask(HashMap<String,String> body)
        {
            this.param=body;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG,s);
            progressDialog.dismiss();
            if(s.contains("success")) {
                Utils.openDialog(getContext(),getContext().getString(R.string.posted));
                edt_description.setText("");
                edt_location.setText("");
                edt_time.setText("");
                edt_title.setText("");
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
                try {
                    Response response =
                            client.newCall(NetworkUtils.createPostRequest(API.CreateNews, this.param)).execute();
                    String results = response.body().string();
                    Log.d("run", results);
                    if (response.isSuccessful()) {
                        Log.d("run", results);
                        return results;
                    }

                } catch (Exception e) {
                    return e.toString();
                }

                return "failed";
            }


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_send :
            {
                if(validate()) {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("time_start", edt_time.getText().toString());
                    param.put("title", edt_title.getText().toString());
                    param.put("address", edt_location.getText().toString());
                    param.put("user_id", "1");
                    param.put("description", edt_description.getText().toString());
                    new MyTask(param).execute();
                }

                break;
            }
            case R.id.edt_time :
            {
                showDatePicker();
                break;
            }
        }

    }
}

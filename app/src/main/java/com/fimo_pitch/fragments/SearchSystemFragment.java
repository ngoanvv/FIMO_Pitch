package com.fimo_pitch.fragments;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.SystemPitchAdapter;
import com.fimo_pitch.db.MyDatabaseHelper;
import com.fimo_pitch.model.SearchPitchModel;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.NetworkUtils;
import com.fimo_pitch.support.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Response;


/**
 * Created by Diep_Chelsea on 13/07/2016.
 */
public class SearchSystemFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "SearchSystemFragment";
    private static String ARG_PARAM1,ARG_PARAM2;
    private Spinner spinner_location;
    private RecyclerView recyclerView;
    private int mHour,mMinute;
    private String sHour="7";
    private String sMinute="00";
    private String location="Cầu Giấy";
    private int dateofweek=1;
    private LinearLayout menuView;
    private SystemPitchAdapter adapter;
    private ImageView buttonView2;
    private EditText edt_search;
    private ImageView buttonView4;
    private TextView tv_time;
    public static String data;
    public OkHttpClient okHttpClient;
    public MyDatabaseHelper myDatabaseHelper;
    private ArrayList<SystemPitch> rootList;
    private ArrayList<SystemPitch> listSystemPitch;
    private ArrayList<SearchPitchModel> listSearch;
    private String currentTime="07:00";
    private UserModel userModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_search_system, container, false);
        userModel = (UserModel) getActivity().getIntent().getSerializableExtra(CONSTANT.KEY_USER);
        initView(rootView);
        return rootView;
    }

    private void initView(final View rootView)
    {
//        ShowToast.showToastLong(getContext(),"Show tọaadssasfdff");
        spinner_location = (Spinner) rootView.findViewById(R.id.spn_location);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item2,
                getActivity().getResources().getStringArray(R.array.listProvince1));
        spinner_location.setAdapter(adapter);
        tv_time = (TextView) rootView.findViewById(R.id.tv_time);
        tv_time.setOnClickListener(this);

        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("day", Calendar.getInstance().get(Calendar.YEAR) + "-" +
                        (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                map.put("time_start", currentTime);
                map.put("textlocation", spinner_location.getItemAtPosition(position).toString());
                new SearchSystemPitch(map).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private class SearchSystemPitch extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        HashMap<String,String> param;
        public SearchSystemPitch(HashMap<String,String> param)
        {
            this.param = param;
        }
        @Override
        protected String doInBackground(String... params) {

            try {
                okHttpClient = new OkHttpClient();
                Response response =
                        okHttpClient.newCall(NetworkUtils.createPostRequest(API.SearchPitch, this.param)).execute();
                if (response.isSuccessful())
                {
                    return response.body().string().toString();

                }
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
            return "failed";

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("searchPitch",s);
            listSearch = new ArrayList<>();
            initRecyclerView(s);
//            try {
//                JSONObject jsonObject = new JSONObject(s);
//                JSONArray data = jsonObject.getJSONArray("data");
//                for (int i = 0; i < data.length(); i++) {
//                    JSONObject object = data.getJSONObject(i);
//                    SearchPitchModel systemPitch = new SearchPitchModel();
//                    systemPitch.setPitch_description(object.getString("description"));
//                    systemPitch.setSystem_id(object.getString("system_id"));
//                    systemPitch.setUser_id("1");
//                    systemPitch.setUser_name("Owner");
//                    systemPitch.setSystem_name(object.getString("name"));
//                    systemPitch.setAddress(object.getString("address"));
//                    systemPitch.setPitch_name(object.getString("pitch_name"));
//                    systemPitch.setPitch_id(object.getString("pitch_id"));
//                    systemPitch.setPhone(object.getString("phone"));
//                    systemPitch.setLat(object.getString("lat"));
//                    systemPitch.setLog(object.getString("log"));
//                    systemPitch.setTime_start(object.getString("time_start"));
//                    systemPitch.setTime_end(object.getString("time_end"));
//                    listSearch.add(systemPitch);
//                }
//                if(listSearch.size()>0)
//                {
//
//                }
//                else
//                {
//                    Utils.openDialog(getContext(),"Không có sân bóng khả dụng trong ngày với khu vực lựa chọn. Hãy thử với khu vực khác");
//                }
                progressDialog.dismiss();
//
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG,param.toString());
            listSystemPitch = new ArrayList<>();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
    }
    public SearchSystemFragment() {}
    public static SearchSystemFragment newInstance(String mdata, String param2) {
        SearchSystemFragment fragment = new SearchSystemFragment();
        Bundle args = new Bundle();
        fragment.data = mdata;
        args.putString(ARG_PARAM1, mdata);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private ArrayList<SystemPitch> filterByAddress(String s)
    {
        ArrayList result = new ArrayList();
        for(int i=0;i<rootList.size();i++)
        {
            if(rootList.get(i).getAddress().contains(s))
                result.add(rootList.get(i));
        }
        return result;
    }
    public void initRecyclerView(ArrayList<SystemPitch> list)
    {
        recyclerView.setHasFixedSize(true);
        adapter = new SystemPitchAdapter(getActivity(), list,userModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
        recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);

    }
    public void initRecyclerView(String data)
    {
        listSystemPitch = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray array = jsonObject.getJSONArray("data");
            if(array.length()>0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    SystemPitch systemPitch = new SystemPitch();
                    systemPitch.setDescription(object.getString("description"));
                    systemPitch.setId(object.getString("id"));
                    systemPitch.setOwnerName("Tiến TM");
                    systemPitch.setOwnerID(object.getString("user_id"));
                    systemPitch.setName(object.getString("name"));
                    systemPitch.setAddress(object.getString("address"));
                    systemPitch.setId(object.getString("id"));
                    systemPitch.setPhone(object.getString("phone"));
                    systemPitch.setLat(object.getString("lat"));
                    systemPitch.setLng(object.getString("log"));
                    listSystemPitch.add(systemPitch);
                }
                recyclerView.setHasFixedSize(true);
                adapter = new SystemPitchAdapter(getActivity(), listSystemPitch, userModel);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);

                StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
                recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
            }
            else
            {
                Utils.openDialog(getContext(),"Không có sân bóng khả dụng trong ngày với khu vực lựa chọn. Hãy thử với khu vực khác");
                recyclerView.setHasFixedSize(true);
                adapter = new SystemPitchAdapter(getActivity(), listSystemPitch, userModel);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);

                StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
                recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d(TAG,"bug");
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_time: {
                mHour = 0;
                mMinute = 0;
                TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;
                        sHour = mHour + "";
                        sMinute = mMinute + "";
                        if (mHour < 10 && mMinute < 10) {
                            tv_time.setText("0" + mHour + ":" + "0" + mMinute);
                        }
                        if (mHour < 10 && mMinute > 10) {
                            tv_time.setText("0" + mHour + ":" + "" + mMinute);
                        }
                        if (mHour > 10 && mMinute < 10) {
                            tv_time.setText("" + mHour + ":" + "0" + mMinute);
                        }
                        currentTime = tv_time.getText().toString();
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("day", Calendar.getInstance().get(Calendar.YEAR) + "-" +
                                (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        map.put("time_start", currentTime);
                        map.put("textlocation", spinner_location.getSelectedItem().toString());

                        new SearchSystemPitch(map).execute();
                    }
                }, 7, 00, true);
                dialog.setTitle("Chọn giờ bắt đầu bạn muốn");
                dialog.show();
                break;
            }
        }
    }
}


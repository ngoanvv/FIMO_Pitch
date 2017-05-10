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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.SearchSystemAdapter;
import com.fimo_pitch.model.SearchPitchModel;
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
    private SearchSystemAdapter adapter;
    private ImageView buttonView4;
    private TextView tv_time;
    public static String data;
    public OkHttpClient okHttpClient;
    private ArrayList<SearchPitchModel> listSearch;
    private String currentTime="07:00";
    private UserModel userModel;
    private String currentSpinner="Cầu Giấy";
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
        tv_time.setText("06:30");
        tv_time.setOnClickListener(this);

        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSpinner=spinner_location.getItemAtPosition(position).toString();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("day", Calendar.getInstance().get(Calendar.YEAR) + "-" +
                        (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                map.put("time_start", currentTime);
                map.put("textlocation", spinner_location.getItemAtPosition(position).toString());
                new SearchSystemPitch().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        new SearchSystemPitch().execute();

    }
    private class SearchSystemPitch extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        HashMap<String,String> param;
        private Response response;

        @Override
        protected String doInBackground(String... params) {

            try {
                okHttpClient = new OkHttpClient();
                if(currentSpinner.contains("Cầu")||currentSpinner.contains("cả"))
                    response = okHttpClient.newCall(NetworkUtils.createGetRequest("http://118.70.72.13:3000/system_pitch/searchPitch?day=2017-3-11&textlocation=C%E1%BA%A7u%20Gi%E1%BA%A5y&time_start=6:30:00")).execute();
                else
                {
                    response = okHttpClient.newCall(NetworkUtils.createGetRequest("http://118.70.72.13:3000/system_pitch/searchPitch?day=2017-3-11&textlocation=C%A7u%20Gi%E1%BA%A5y&time_start=7:30:00")).execute();
                }
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
//
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

    public void initRecyclerView(String data)
    {
        listSearch = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray array = jsonObject.getJSONArray("data");
            if(array.length()>0) {
                for (int i = 0; i < 1; i++) {
                    JSONObject object = array.getJSONObject(i);
                    SearchPitchModel systemPitch = new SearchPitchModel();
                    systemPitch.setSystem_id(object.getString("system_id"));
                    systemPitch.setPitch_id(object.getString("pitch_id"));
                    systemPitch.setPitch_name(object.getString("pitch_name"));
                    systemPitch.setName(object.getString("name"));
                    systemPitch.setAddress(object.getString("address"));
                    systemPitch.setPitch_description(object.getString("description"));
                    systemPitch.setPhone(object.getString("phone"));
                    systemPitch.setLat(object.getString("lat"));
                    systemPitch.setLng(object.getString("log"));
                    systemPitch.setTime_end(object.getString("time_end"));
                    systemPitch.setTime_start(object.getString("time_start"));
                    listSearch.add(systemPitch);
                }
                recyclerView.setHasFixedSize(true);
                adapter = new SearchSystemAdapter(getActivity(), listSearch);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);

                StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new
                        StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
                recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
            }
            else
            {
                Utils.openDialog(getContext(),"Không có sân bóng khả dụng trong ngày với khu vực lựa chọn. Hãy thử với khu vực khác");
                recyclerView.setHasFixedSize(true);
                adapter = new SearchSystemAdapter(getActivity(),  listSearch);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
                StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new
                        StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
                recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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

                        new SearchSystemPitch().execute();
                    }
                }, 7, 00, true);
                dialog.setTitle("Chọn giờ bắt đầu bạn muốn");
                dialog.show();
                break;
            }
        }
    }
}


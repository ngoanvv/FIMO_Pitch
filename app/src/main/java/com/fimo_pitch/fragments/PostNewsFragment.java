package com.fimo_pitch.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.fimo_pitch.R;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.support.ShowToast;
import com.fimo_pitch.support.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by TranManhTien on 23/08/2016.
 */
public class PostNewsFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public final int SOFTKEYBOARDHEIGHT=100;
    public static String TAG="PostNewsFragment";
    private EditText edt_time,edt_money,edt_description,edt_location;
    private Button bt_post;
    private GoogleMap map;
    // 144 xuan thuy : lat : 21.036654, lng 105.781218
    private SupportMapFragment mapFragment;
    private LatLng xuanthuy = new LatLng(21.036654,105.78218);
    private RoundedImageView img_send,img_cancel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_postnews, container, false);
        mapFragment = new SupportMapFragment() {
            @Override
            public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                if(mapFragment!=null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            map = googleMap;
                            moveCamera(xuanthuy, 12);
                            map.addMarker(new MarkerOptions().position(xuanthuy));
                            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    showCircle(latLng, 2000);
                                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                    List<Address> addresses = null;
                                    try {
                                        addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude, 1);
                                        String cityName = addresses.get(0).getAddressLine(0);
                                        String stateName = addresses.get(0).getAddressLine(1);
                                        Log.d(TAG,addresses.toString()+"");
                                        edt_location.setText(stateName);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }
                    });
                }

            }
        };
        getChildFragmentManager().beginTransaction().add(R.id.mapfragment, mapFragment).commit();
        initView(view);
        return view;
    }
    public void initView(View view)
    {
        edt_description = (EditText) view.findViewById(R.id.edt_description);
        edt_time = (EditText) view.findViewById(R.id.edt_time);
        edt_money = (EditText) view.findViewById(R.id.edt_money);
        edt_location = (EditText) view.findViewById(R.id.edt_location);
        img_send = (RoundedImageView) view.findViewById(R.id.img_send);
        img_cancel = (RoundedImageView) view.findViewById(R.id.img_cancel);

        edt_time.setOnClickListener(this);
        img_cancel.setOnClickListener(this);
        img_send.setOnClickListener(this);

    }
    public void moveCamera(LatLng latLng,int zoom) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom  );
        map.animateCamera(cameraUpdate);
        map.addMarker(new MarkerOptions().position(latLng));
    }
    public void showCircle(LatLng latLng, double radius)
    {
        map.clear();
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng)
                .center(latLng)
                .clickable(true)
                .strokeColor(Color.parseColor("#F6CECE"))
                .fillColor(Color.parseColor("#F6CECE"))
                .radius(radius);
        map.addCircle(circleOptions);
        moveCamera(latLng,12);

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
        String description,address,stadium,time,money;
        description = edt_description.getText().toString().replaceAll("\\s+", " ");;
        time        = edt_time.getText().toString().replaceAll("\\s+", " ");
        money       = edt_money.getText().toString().replaceAll("\\s+", " ");
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
        if (money.isEmpty() || money.length() < 6 )
        {
            edt_money.setError(getString(R.string.invalid_length));
            valid = false;
        }
        else
        {
            edt_money.setError(null);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_send :
            {
                Utils.openDialog(getContext(),getContext().getString(R.string.posted));
                edt_description.setText("");
                edt_location.setText("");
                edt_time.setText("");
                edt_money.setText("");
                break;
            }
            case R.id.edt_time :
            {
                showDatePicker();
                break;
            }
            case R.id.img_cancel :
            {
                edt_description.setText("");
                edt_location.setText("");
                edt_time.setText("");
                edt_money.setText("");
                break;
            }
        }

    }
}

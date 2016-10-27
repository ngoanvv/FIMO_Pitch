package com.fimo_pitch.support;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.fimo_pitch.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Diep_Chelsea on 15/07/2016.
 */
public class Utils {
        public static void openDialog(Context context, String content)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(content);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                }
            });
            builder.create().show();

        }
        public static  void showDialog(Context context,String content,DialogInterface.OnClickListener yesClick,
                                       DialogInterface.OnClickListener cancelClick)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(content);
            builder.setPositiveButton(context.getString(R.string.ok),yesClick);
            builder.setNegativeButton(context.getString(R.string.cancel),cancelClick);
            builder.create().show();
        }
    public static  void showGpsSettingsAlert(final  Context context){
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context);

        alertDialog.setTitle("GPS is not enabled");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    public static void getCurrentLocation(GoogleMap map,TrackGPS gps,Context context,String tag)
    {

    }

    public static void showInternetSettingsAlert(final Context context){
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialog.setTitle("Network settings");
        alertDialog.setMessage("Network is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
    public static void moveCamera(LatLng latLng, int zoom, GoogleMap map) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom  );
        map.animateCamera(cameraUpdate);
    }
    public static void showCircle(LatLng latLng, double radius,GoogleMap map)
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
        map.addMarker(new MarkerOptions().position(latLng));
        moveCamera(latLng,13,map);

    }
    public boolean isGPSEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try
        {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(network_enabled == false) Utils.showInternetSettingsAlert(context);
            else
            {
                if(gps_enabled==false) Utils.showGpsSettingsAlert(context);
                else
                {
                    return true;
                }
            }



        }
        catch (Exception ex)
        {
            return false;

        }
        return false;
    }
}

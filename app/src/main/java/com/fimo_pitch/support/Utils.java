package com.fimo_pitch.support;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.view.inputmethod.InputMethodManager;

import com.fimo_pitch.R;
import com.fimo_pitch.custom.view.MyCustomDialog;
import com.fimo_pitch.main.FirstActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Diep_Chelsea on 15/07/2016.
 */
public class Utils {
        public static void openDialog(Context context, String content)
        {

            MyCustomDialog dialog = new MyCustomDialog(context,content);
            dialog.show();
        }

    public static void makeNotification(Context context,Activity activity,String content)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("FIMO MSN")
                        .setContentText(content);
        Intent resultIntent = new Intent(context, FirstActivity.class);
        TaskStackBuilder stackBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(activity);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT  );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(10, mBuilder.build());
        }
        else
        {

        }
    }
    public static String getDayofWeek(int dateofweek)
    {
        switch (dateofweek)
        {
            case 1 :
            {
                return "Sun";
            }
            case 2 :
            {
                return "Mon";
            }
            case 3 :
            {
                return "Tue";
            }
            case 4 :
            {
                return "Wed";
            }
            case 5 :
            {
                return "Thu";
            }
            case 6 :
            {
                return "Fri";
            }
            case 7 :
            {
                return "Sat";
            }
        }
        return "Mon";

    }
    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    public static  void showGpsSettingsAlert(final  Context context, DialogInterface.OnClickListener clickListener){
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
        alertDialog.setNegativeButton("Cancel",clickListener);

        // Showing Alert Message
        alertDialog.show();
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
    public static void moveCamera(LatLng latLng, String title,int zoom, GoogleMap map) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom  );
        map.addMarker(new MarkerOptions().position(latLng).title(title)).showInfoWindow();
        map.animateCamera(cameraUpdate);
    }
    public static void showCircle(LatLng latLng, double radius,GoogleMap map)
    {
        map.clear();
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng)
                .center(latLng)
                .clickable(true)
                .strokeColor(Color.parseColor("#664FC3F7"))
                .fillColor(Color.parseColor("#664FC3F7"))
                .radius(radius);
        map.addCircle(circleOptions);
        map.addMarker(new MarkerOptions().position(latLng));
        moveCamera(latLng,"Bạn ở đây",13,map);

    }
    public static  void hideSoftKeyboard(Activity context) {
        if(context.getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        }
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
                if(gps_enabled==false) Utils.showGpsSettingsAlert(context, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
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

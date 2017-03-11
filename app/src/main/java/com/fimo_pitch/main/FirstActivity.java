package com.fimo_pitch.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fimo_pitch.R;
import com.fimo_pitch.support.TrackGPS;
import com.fimo_pitch.support.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.firebase.FirebaseApp;

import okhttp3.OkHttpClient;

public class FirstActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {
    SharedPreferences sharedPreferences;
    Boolean seen;
    private OkHttpClient client;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = "FirstActivity";
    private int permissionCode = 1111;
    private Location location;
    private Location mLastLocation;
    private TrackGPS gps;
    private LocationRequest mLocationRequest;
    private double currentlatitude;
    private double currentlongitude;
    private double latitude,longitude;
    private int countPause=0;
    private int countResume=0;
    private Button skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(FirstActivity.this);
        setContentView(R.layout.activity_first);
        skip = (Button) findViewById(R.id.bt_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isConnected(FirstActivity.this)) {
                    sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (sharedPreferences != null) {
                                seen = sharedPreferences.getBoolean("seen", false);
                                if (seen == true) {
                                    Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(FirstActivity.this, IntroductionActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    }, 1000);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                    builder.setMessage(getString(R.string.no_connection));
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    builder.create().show();
                }

            }
        });

        client = new OkHttpClient();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        createLocationRequest();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(FirstActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},permissionCode);
        }
    }

    @Override
    protected void onPause() {
        countPause++;
        Log.d("first","on pause");
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setUpMapIfNeeded();
        countResume++;
        if(countResume>0) {
            Log.d("first", "on resume");
            String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (locationProviders == null || locationProviders.equals("")) {
                Log.d("gps", "disable");
                Utils.showGpsSettingsAlert(FirstActivity.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (Utils.isConnected(FirstActivity.this)) {
                            sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (sharedPreferences != null) {
                                        seen = sharedPreferences.getBoolean("seen", false);
                                        if (seen == true) {
                                            Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Intent intent = new Intent(FirstActivity.this, IntroductionActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }
                            }, 1000);

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                            builder.setMessage(getString(R.string.no_connection));
                            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            builder.create().show();
                        }
                    }
                });
            } else {
                Log.d("gps", "enable");
                createLocationRequest();
                if (Utils.isConnected(FirstActivity.this)) {
                    sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (sharedPreferences != null) {
                                seen = sharedPreferences.getBoolean("seen", false);
                                if (seen == true) {
                                    Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(FirstActivity.this, IntroductionActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    }, 1000);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                    builder.setMessage(getString(R.string.no_connection));
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    builder.create().show();
                }
            }
            if(countResume>1) {
                if (Utils.isConnected(FirstActivity.this)) {
                    sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (sharedPreferences != null) {
                                seen = sharedPreferences.getBoolean("seen", false);
                                if (seen == true) {
                                    Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(FirstActivity.this, IntroductionActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    }, 1000);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                    builder.setMessage(getString(R.string.no_connection));
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    builder.create().show();
                }
            }
        }
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onConnected(Bundle bundle) {
        Location mCurrentLocation;

        Log.i("TAG", "OnConnected");
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(FirstActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(FirstActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                // Note that this can be NULL if last location isn't already known.
                if (mCurrentLocation != null) {
                    // Print current location if not null
                    Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
                    currentlatitude = mCurrentLocation.getLatitude();
                    currentlongitude = mCurrentLocation.getLongitude();
                    SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
                    sharedPreferences.edit().putString("lat",currentlatitude+"").commit();
                    sharedPreferences.edit().putString("lng",currentlongitude+"").commit();
                } else {
                    startLocationUpdates();
                }
            }
        } else {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mCurrentLocation != null) {
                Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
                currentlatitude = mCurrentLocation.getLatitude();
                currentlongitude = mCurrentLocation.getLongitude();
                SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
                sharedPreferences.edit().putString("lat",currentlatitude+"").commit();
                sharedPreferences.edit().putString("lng",currentlongitude+"").commit();

            }
            startLocationUpdates();
        }
    }
    private void createLocationRequest() {
        Log.i("TAG", "CreateLocationRequest");
        mLocationRequest = new LocationRequest();
        long UPDATE_INTERVAL = 10 * 1000;
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        long FASTEST_INTERVAL = 10000;
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

    }
    private void startLocationUpdates() {


        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(FirstActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(FirstActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                        mLocationRequest, this);

            }
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }
    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
            Log.d("TAG","permission granted");
            gps = new TrackGPS(FirstActivity.this,FirstActivity.this);
            if(gps.canGetLocation()){
                longitude = gps.getLongitude();
                latitude = gps .getLatitude();
                Log.d("gps","lattt: " + currentlatitude +" lng :"+currentlongitude);
                SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
                sharedPreferences.edit().putString("lat",currentlatitude+"").commit();
                sharedPreferences.edit().putString("lng",currentlongitude+"").commit();

            }
            else
            {
                SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
                sharedPreferences.edit().remove("lat").remove("lng").commit();
//                Utils.openDialog(FirstActivity.this,"Không định vị được vị trí của bạn");
            }
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.i("TAG", "OnLocationChanged");
        Log.i("TAG", "Current Location==>" + location);
        currentlatitude = location.getLatitude();
        currentlongitude = location.getLongitude();
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        sharedPreferences.edit().putString("lat",currentlatitude+"").commit();
        sharedPreferences.edit().putString("lng",currentlongitude+"").commit();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(FirstActivity.this, connectionResult.RESOLUTION_REQUIRED);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("TAG", "Location services connection failed with code==>" + connectionResult.getErrorCode());
            Log.e("TAG", "Location services connection failed Because of==> " + connectionResult.getErrorMessage());
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("TAG", "onConnectionSuspended");
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(FirstActivity.this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(FirstActivity.this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
        mGoogleApiClient.connect();
    }

}

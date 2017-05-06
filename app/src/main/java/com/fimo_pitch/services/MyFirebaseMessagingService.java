package com.fimo_pitch.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.main.FirstActivity;
import com.fimo_pitch.main.MainActivity;
import com.fimo_pitch.model.UserModel;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private UserModel userModel;
    private SharedPreferences sharedPreferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0 && remoteMessage.getNotification() != null ) {
            makeNotification(getApplicationContext(),remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }
    }
    private UserModel getUserModel()
    {
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        if (sharedPreferences != null)
        {
            String email = sharedPreferences.getString("email", "null");
            String password = sharedPreferences.getString("password", "null");
            String userType = sharedPreferences.getString("userType", "null");
            String phone = sharedPreferences.getString("phone", "null");
            String name = sharedPreferences.getString("password", "null");
            String id = sharedPreferences.getString("id", "null");

            Log.d(TAG,email+" - "+password);
            if (email.equals("null") || password.equals("null")) {
                return new UserModel();
            }
            else
            {
                userModel = new UserModel();
                userModel.setName(name);
                userModel.setEmail(email);
                userModel.setPassword(password);
                userModel.setUserType(userType);
                userModel.setPhone(phone);
                userModel.setId(id);
                return  userModel;
            }
        }
        else return new UserModel();
    }
    private void makeNotification(Context context,String title,String content)
    {
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setSound(uri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentText(content);
        Intent resultIntent = new Intent(context, MainActivity.class);
        userModel = getUserModel();
        resultIntent.putExtra(CONSTANT.KEY_USER, userModel);
        TaskStackBuilder stackBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(FirstActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT  );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(10, mBuilder.build());
//            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//            v.vibrate(ZAQ500);
        }
        else
        {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(10, mBuilder.build());
//            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//            v.vibrate(500);
        }
    }

}

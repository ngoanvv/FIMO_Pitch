package com.fimo_pitch.support;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by DiepNV on 11/19/2015.
 */
public class ShowToask {

    public static void showToaskLong(Context context, String messenger) {
        Toast.makeText(context, messenger, Toast.LENGTH_LONG).show();
    }

    public static void showToaskShort(Context context, String messenger) {
        Toast.makeText(context, messenger, Toast.LENGTH_SHORT).show();
    }

    public static void showCustomToask(Context context, String msg, int duration) {
        Toast toast = Toast.makeText(context, msg, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View toastView = toast.getView();
        toast.show();
    }
}

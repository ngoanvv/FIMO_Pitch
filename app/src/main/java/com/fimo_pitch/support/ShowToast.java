package com.fimo_pitch.support;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by DiepNV on 11/19/2015.
 */
public class ShowToast {

    public static void showToastLong(Context context, String messenger) {
        Toast.makeText(context, messenger, Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(Context context, String messenger) {
        Toast.makeText(context, messenger, Toast.LENGTH_SHORT).show();
    }

}

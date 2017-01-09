package com.fimo_pitch.custom.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.fimo_pitch.R;


/**
 * Created by Diep_Chelsea on 09/05/2016.
 */
public class CustomDialog extends DialogFragment implements View.OnClickListener {
    String title;
    Context mContext;
    public CustomDialog(Context context,String content ) {
        this.title = content;
        this.mContext = context;
    }



    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context mContext = getActivity();
        Dialog dialog = new Dialog(mContext);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_choose_image, null);
        TextView msg = (TextView) view.findViewById(R.id.id_message);
        msg.setText(title);
        view.findViewById(R.id.id_dialog_ok).setOnClickListener(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_dialog_ok:
                handleOkEvent();
                break;

            default:
                break;
        }
    }


    private void handleOkEvent() {
        dismiss();
    }

}

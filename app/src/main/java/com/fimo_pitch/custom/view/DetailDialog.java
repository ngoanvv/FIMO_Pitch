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
import com.fimo_pitch.model.SystemPitch;


/**
 * Created by Diep_Chelsea on 09/05/2016.
 */
public class DetailDialog extends DialogFragment implements View.OnClickListener {
    int pos;
    Context mContext;
    SystemPitch mSystemPitch;
    public DetailDialog(Context context, SystemPitch systemPitch,int x) {
        this.pos = x;
        this.mContext = context;
        this.mSystemPitch = systemPitch;
    }



    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context mContext = getActivity();
        Dialog dialog = new Dialog(mContext);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_detail, null);
        TextView name = (TextView) view.findViewById(R.id.id_sName);
        TextView address = (TextView) view.findViewById(R.id.id_address);
        TextView des = (TextView) view.findViewById(R.id.id_des);

        name.setText(mSystemPitch.getName());
        address.setText(mSystemPitch.getAddress());
        des.setText(mSystemPitch.getDescription());

        view.findViewById(R.id.id_dialog_ok).setOnClickListener(this);
        view.findViewById(R.id.id_more).setOnClickListener(this);
        view.findViewById(R.id.id_route).setOnClickListener(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_dialog_ok: {
                handleOkEvent();
                break;
            }
            case R.id.id_more: {
                handleMoreEvent();
                break;
            }
            case R.id.id_route: {
                handleRouteEvent();
                break;
            }
            default:
                break;
        }
    }

    private void handleMoreEvent() {
        dismiss();
        mEvent.onConfirmed(true);
    }
    private void handleRouteEvent()
    {
        dismiss();
        mEvent.onConfirmed(false);
    }

    private void handleOkEvent() {
        dismiss();
    }
    private OnMoreDetailEvent mEvent;

    public void setOnArrivalDeliverListener(OnMoreDetailEvent event) {
        this.mEvent = event;
    }



    public interface OnMoreDetailEvent {
        void onConfirmed(boolean confirm);
    }
}

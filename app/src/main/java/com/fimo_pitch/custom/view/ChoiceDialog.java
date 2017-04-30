package com.fimo_pitch.custom.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fimo_pitch.R;


/**
 * Created by Diep_Chelsea on 09/05/2016.
 */
public class ChoiceDialog extends Dialog implements View.OnClickListener {
    String title;
    Context mContext;
    public ChoiceDialog(Context context, String content) {
        super(context);
        this.title = content;
        this.mContext = context;
    }
    OnClickListener clickYes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_choice_image);
        TextView msg = (TextView) findViewById(R.id.id_message);
        msg.setText(title);
        Button yes = (Button) findViewById(R.id.id_dialog_ok);
        yes.setOnClickListener(this);
        Button no = (Button) findViewById(R.id.id_cancel);
        no.setOnClickListener(this);
    }

    HandleEvent mHandleEvent;
    public void setmHandleEvent(HandleEvent e)
    {
        this.mHandleEvent = e;
    }
    public interface HandleEvent
    {
        void ClickOk();
        void ClickCancel();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_dialog_ok:
                mHandleEvent.ClickOk();
                break;
            case R.id.id_cancel:
                mHandleEvent.ClickCancel();
                dismiss();
                break;
            default:
                break;
        }
    }

}

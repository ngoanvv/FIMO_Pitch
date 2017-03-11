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
public class MyCustomDialog extends Dialog implements View.OnClickListener {
    String title;
    Context mContext;
    public MyCustomDialog(Context context, String content ) {
        super(context);
        this.title = content;
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_choose_image);
        TextView msg = (TextView) findViewById(R.id.id_message);
        msg.setText(title);
        Button yes = (Button) findViewById(R.id.id_dialog_ok);
        yes.setOnClickListener(this);
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

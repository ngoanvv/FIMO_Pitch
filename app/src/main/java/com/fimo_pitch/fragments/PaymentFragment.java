package com.fimo_pitch.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fimo_pitch.R;
import com.fimo_pitch.adapter.NewsFragmentAdapter;
import com.fimo_pitch.main.PaymentActivity;
import com.fimo_pitch.model.News;
import com.fimo_pitch.support.ShowToast;
import com.fimo_pitch.support.Utils;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

/**
 * Created by Diep_Chelsea on 13/07/2016.
 */
public class PaymentFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "PaymentFragment";
    public ImageView img_payment;
    public String data;
    public ExpandableRelativeLayout expandableLayout;
    public RelativeLayout info_layout;
    public ImageView img_info;
    public TextView bt_pay;
    public EditText edt_name,edt_address,edt_phone,edt_email;

    public PayClick mPayClick;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_payment, container, false);
        Utils.hideSoftKeyboard(getActivity());
        initView(rootView);
        return rootView;
    }


    private void initView(View view) {
        img_payment = (ImageView) view.findViewById(R.id.img_payment);
//        img_payment.setBlurImageByRes(R.drawable.ic_pitch);
        BitmapDrawable drawable = (BitmapDrawable) img_payment.getBackground();
        if(drawable !=null) {
            Bitmap bitmap = drawable.getBitmap();
//            Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
//            img_payment.setImageBitmap(blurred);
        }
        expandableLayout = (ExpandableRelativeLayout) view.findViewById(R.id.expandable_layout);
        info_layout = (RelativeLayout) view.findViewById(R.id.info_layout) ;
        img_info = (ImageView) view.findViewById(R.id.img_info);
        bt_pay = (TextView) view.findViewById(R.id.bt_pay);
        edt_address = (EditText) view.findViewById(R.id.edt_address);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_name = (EditText) view.findViewById(R.id.edt_name);
        edt_phone = (EditText) view.findViewById(R.id.edt_phone);

        edt_name.setText("Tiến Tiền tỷ");
        edt_address.setText("Ngõ 165 Xuân Thủy, Cầu Giấy");
        edt_phone.setText("0989177619");
        edt_email.setText("tientienti@gmail.com");

        bt_pay.setOnClickListener(this);
        info_layout.setOnClickListener(this);

    }
    @SuppressLint("NewApi")
    private Bitmap blurRenderScript(Bitmap smallBitmap, int radius) {
        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(getContext());

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;

    }

    private Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }


    public PaymentFragment(String s) {
        data=s;

    }

    public interface PayClick
    {
        public void onPayClick();
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.info_layout :
            {
                if(expandableLayout.isExpanded())
                {
                    expandableLayout.collapse();
                    img_info.setImageResource(R.drawable.ic_right);
                }
                else
                {
                    expandableLayout.expand();
                    img_info.setImageResource(R.drawable.ic_down);
                }
                break;
            }
            case R.id.bt_pay:
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_payment,new PaymentSuccessFragment()).commit();
            }
        }
    }
}


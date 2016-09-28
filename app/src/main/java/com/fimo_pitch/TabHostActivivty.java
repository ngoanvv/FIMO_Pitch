package com.fimo_pitch;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.main.AboutActivity;
import com.fimo_pitch.main.MainActivity;
import com.fimo_pitch.main.SettingActivity;

public class TabHostActivivty extends TabActivity {
    public static String TAG ="TabHostActivivty";
    private TabHost tabHost;
    private RoundedImageView roundedImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tab_host);
        initTabhost();

    }


    public void initTabhost() {

        tabHost = getTabHost();

        TabHost.TabSpec mainActivity = tabHost.newTabSpec("home");
        TabHost.TabSpec settingActivity = tabHost.newTabSpec("settings");
        TabHost.TabSpec aboutActivity = tabHost.newTabSpec("about");

        mainActivity.setContent(new Intent(this, MainActivity.class)).setIndicator("",getResources().getDrawable(R.drawable.tab_home));
        settingActivity.setContent(new Intent(this, SettingActivity.class)).setIndicator("",getResources().getDrawable(R.drawable.tab_settings));
        aboutActivity.setContent(new Intent(this, AboutActivity.class)).setIndicator("",getResources().getDrawable(R.drawable.tab_info));

        Display display = getWindowManager().getDefaultDisplay();


        tabHost.addTab(mainActivity);
        tabHost.addTab(settingActivity);
        tabHost.addTab(aboutActivity);
        // resize tabhost widget

        int width = display.getWidth();

        tabHost.getTabWidget().getChildAt(0).setLayoutParams(new LinearLayout.LayoutParams(width / 5, width / 7));
        tabHost.getTabWidget().getChildAt(1).setLayoutParams(new LinearLayout.LayoutParams(width / 5, width / 7));
        tabHost.getTabWidget().getChildAt(2).setLayoutParams(new LinearLayout.LayoutParams(width / 5, width / 7));

        tabHost.getTabWidget().getChildAt(0).setPadding(10,10,10,10);
        tabHost.getTabWidget().getChildAt(1).setPadding(10,10,10,10);
        tabHost.getTabWidget().getChildAt(2).setPadding(10,10,10,10);

        tabHost.setCurrentTab(2);

    }
}

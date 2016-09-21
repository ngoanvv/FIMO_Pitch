package com.fimo_pitch;

import android.annotation.TargetApi;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.main.MainActivity;
import com.fimo_pitch.main.NotificationActivity;
import com.fimo_pitch.main.SearchActivity;
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


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initTabhost()
    {

        tabHost = getTabHost();

        TabHost.TabSpec mainActivity = tabHost.newTabSpec("Home");
        TabHost.TabSpec searchActivity = tabHost.newTabSpec("Search");
        TabHost.TabSpec notificationActivity = tabHost.newTabSpec("Notification");
        TabHost.TabSpec settingActivity = tabHost.newTabSpec("Settings");

        mainActivity.setContent(new Intent(this, MainActivity.class)).setIndicator("Home");
        searchActivity.setContent(new Intent(this, SearchActivity.class)).setIndicator("Search");
        notificationActivity.setContent(new Intent(this, NotificationActivity.class)).setIndicator("Notification");
        settingActivity.setContent(new Intent(this, SettingActivity.class)).setIndicator("Settings");
        Display display = getWindowManager().getDefaultDisplay();

        tabHost.addTab(mainActivity);
        tabHost.addTab(searchActivity);
        tabHost.addTab(notificationActivity);
        tabHost.addTab(settingActivity);
        // resize tabhost widget
        int width = display.getWidth();
        tabHost.getTabWidget().getChildAt(0).setLayoutParams(new LinearLayout.LayoutParams(width/4,width/6));
        tabHost.getTabWidget().getChildAt(1).setLayoutParams(new LinearLayout.LayoutParams(width/4,width/6));
        tabHost.getTabWidget().getChildAt(2).setLayoutParams(new LinearLayout.LayoutParams(width/4,width/6));
        tabHost.getTabWidget().getChildAt(3).setLayoutParams(new LinearLayout.LayoutParams(width/4,width/6));

        mainActivity.setIndicator("Home",getDrawable(R.drawable.ic_home));
    }


}

package com.fimo_pitch;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.fimo_pitch.R;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.ui.MainActivity;
import com.fimo_pitch.ui.NotificationActivity;
import com.fimo_pitch.ui.SearchActivity;
import com.fimo_pitch.ui.SettingActivity;

public class TabHostActivivty extends TabActivity {
    public static String TAG ="TabHostActivivty";
    private TabHost tabHost;
    private RoundedImageView roundedImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_host);
        initTabhost();
        Log.d(TAG,"oncreate");
    }
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
    }

}

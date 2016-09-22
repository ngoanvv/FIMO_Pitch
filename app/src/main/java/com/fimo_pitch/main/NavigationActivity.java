/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fimo_pitch.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.fimo_pitch.R;
import com.fimo_pitch.fragments.MatchsFragment;
import com.fimo_pitch.fragments.NewsFragment;
import com.fimo_pitch.fragments.NotifcationFragment;
import com.fimo_pitch.fragments.PostNewsFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Provides UI for the main screen.
 */
public class NavigationActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private TabLayout tabs;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        initView();
        initNavMenu();

    }

    public void initView()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        frameLayout = (FrameLayout) findViewById(R.id.container);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }
    
    public void replaceFragment(Fragment fragment,String tag)
    {
        tabs.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,fragment,tag).commit();

    }
    
    // init Nav menu
    public void initNavMenu()
    {
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        // This method will trigger on item Click of navigation menu
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            menuItem.setChecked(true);
                            switch (menuItem.getItemId()) {
                                case R.id.menu_home :
                                {
                                    frameLayout.setVisibility(View.GONE);
                                    tabs.setVisibility(View.VISIBLE);
                                    mDrawerLayout.closeDrawers();
                                    break;
                                }
                                case R.id.menu_notification :
                                {
                                    replaceFragment(new NotifcationFragment(),NotifcationFragment.class.getName());
                                    mDrawerLayout.closeDrawers();
                                    break;
                                }
                                case R.id.menu_search :
                                {
                                    mDrawerLayout.closeDrawers();
                                    break;
                                }
                                case R.id.menu_manage :
                                {
                                    mDrawerLayout.closeDrawers();
                                    break;
                                }
                                case R.id.menu_settings :
                                {
                                    mDrawerLayout.closeDrawers();
                                    break;
                                }
                                case R.id.menu_logout :
                                {
                                    mDrawerLayout.closeDrawers();
                                    break;
                                }

                            }
                            return true;
                        }
                    });
        }
    }
    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment( MatchsFragment.newInstance("1","2"), "Tìm nhanh");
        adapter.addFragment(NewsFragment.newInstance("",""), "Tin tức");
        adapter.addFragment(PostNewsFragment.newInstance("",""), "Đăng tin");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.fimo_pitch.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.fimo_pitch.R;
import com.fimo_pitch.fragments.PitchsFragment;
import com.fimo_pitch.fragments.NewsFragment;
import com.fimo_pitch.fragments.PostNewsFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {
	private ViewPager mViewPager;
	private static String TAG="MainActivity";
	private PitchsFragment pitchsFragment;
	private PostNewsFragment postNewsFragment;
	private NewsFragment newsFragment;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ActionBar actionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setHomeButtonEnabled(true);
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
							.setText(mSectionsPagerAdapter.getPageTitle(i))
							.setTabListener(this));
		}


	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0://Review fragment
					if (pitchsFragment == null)
					pitchsFragment = pitchsFragment.newInstance("1", "2");
					return pitchsFragment;
				case 1:
					if (newsFragment == null)
						newsFragment = newsFragment.newInstance("1", "2");
					return newsFragment;
				case 2://Benchmark

					if (postNewsFragment == null)
						postNewsFragment = postNewsFragment.newInstance("1", "2");
					return postNewsFragment;
				default: {
					pitchsFragment = pitchsFragment.newInstance("1", "2");
					return pitchsFragment;
				}
			}


		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
				case 0: {
					return "Pitchs";
				}
				case 1:
					return "News";
				case 2: {
					return "Something";
				}
			}
			return null;
		}
	}
	public static class PlaceholderFragment extends Fragment {
		private static final String ARG_SECTION_NUMBER = "section_number";
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_main, container, false);
			return rootView;
		}
	}

}
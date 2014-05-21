package com.boppel.tutorial;

import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.wielangenoch.R;

public class TutorialActivity extends ActionBarActivity {
	
	private static final int NUM_OF_PAGES = 5;
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
		
		
		initialisePaging();
	}
	
	
	// Schmeiss die Fragments in eine Liste, instanziere den Adapter und den Pager, und ab gehts!
	private void initialisePaging() {
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, TutorialPage1.class.getName()));
		fragments.add(Fragment.instantiate(this, TutorialPage2.class.getName()));
		fragments.add(Fragment.instantiate(this, TutorialPage3.class.getName()));
		fragments.add(Fragment.instantiate(this, TutorialPage4.class.getName()));
		fragments.add(Fragment.instantiate(this, TutorialPage5.class.getName()));
		
		this.mPagerAdapter  = new ScreenSlidePagerAdapter(super.getSupportFragmentManager(), fragments);
		mViewPager = (ViewPager)super.findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
	}
	
	/*
	 * Hier in der R.menu.tutorial die Buttons die in der Actionbar gebraucht werden,
	 * definieren!
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tutorial, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed(){
		if(mViewPager.getCurrentItem() == 0){
			super.onBackPressed();
		}
		else {
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
		}
	}

	
	public static class ScreenSlidePageFragment extends Fragment {

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tutorial, container, false);
	        return rootView;
		}
	}
	
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		
		private List<Fragment> fragments;
		
		public ScreenSlidePagerAdapter (android.support.v4.app.FragmentManager fm, List<Fragment> fragments){
			super(fm);
			this.fragments = fragments;
		}
		
		@Override
		public Fragment getItem(int position){
			return this.fragments.get(position);
		}
		
		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}

}

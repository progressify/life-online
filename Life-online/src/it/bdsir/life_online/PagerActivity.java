package it.bdsir.life_online;

import com.google.android.gms.common.GooglePlayServicesUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

public class PagerActivity extends FragmentActivity { 
	private ViewPager mViewPager;
	
//	public PagerActivity(ViewPager mViewPager) {
//		super();
//		this.mViewPager = mViewPager;
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pager);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagerTabStrip);
		pagerTabStrip.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(getResources().getString(R.string.size_pager_text)));
		pagerTabStrip.setTextColor(Color.WHITE);
		TitleAdapter titleAdapter = new TitleAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(titleAdapter);
		mViewPager.setCurrentItem(0);	
		//titleAdapter.getItem(position);
		mViewPager.setOffscreenPageLimit(4);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_pager, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.menu_settings:
	    	Log.e("LO",	"Cliccato Menu impostazioni");
	    	GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}

package it.bdsir.life_online;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class InitialPagerActivity extends FragmentActivity {
	ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pager);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		//PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagerTabStrip);
		TitleAdapterInitial titleAdapter = new TitleAdapterInitial(getSupportFragmentManager());
        mViewPager.setAdapter(titleAdapter);
        mViewPager.setCurrentItem(0);	
	}
}
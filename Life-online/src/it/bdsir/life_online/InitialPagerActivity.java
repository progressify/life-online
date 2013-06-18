package it.bdsir.life_online;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;

public class InitialPagerActivity extends FragmentActivity {
	ViewPager mViewPager;
	Singleton sing = Singleton.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pager);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagerTabStrip);
		pagerTabStrip.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(getResources().getString(R.string.size_pager_text)));
		pagerTabStrip.setTextColor(Color.WHITE);
		TitleAdapterInitial titleAdapter = new TitleAdapterInitial(getSupportFragmentManager());
        mViewPager.setAdapter(titleAdapter);
        mViewPager.setCurrentItem(0);	
        sing.del();
	}
}
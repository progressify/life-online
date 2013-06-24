package it.bdsir.life_online;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class TitleAdapter extends FragmentPagerAdapter{
	private String titles[] = new String[]{"Enti","Soccorso","Segnalazione","Dove sei", "Informazioni personali"}; 
	private Fragment frags[] = new Fragment[titles.length]; 

	public TitleAdapter(FragmentManager fm) {
		super(fm);
		frags[0]= new MainActivitySummary();
		frags[1] = new SoccorsoActivity();
		frags[2] = new ActivityDistressNotPersonal();
		
//		frags[3] = new ActivityServiceInfo();
		frags[4] = new ActivityServiceInfo();
		frags[3] = new ActivityMyPosition();
	}
	
	public CharSequence getPageTitle (int position){
		Log.v("TitleAdapter - getPageTitle=", titles[position]);
		return titles[position];
	}

	@Override
	public Fragment getItem(int position) {
		Log.v("TitleAdapter - getItem=", String.valueOf(position));
		return frags[position];
	}

	@Override
	public int getCount() {
		return frags.length;
	}

}
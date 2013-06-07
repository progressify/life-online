package it.bdsir.life_online;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class TitleAdapterInitial extends FragmentPagerAdapter{
	private String titles[] = new String[]{"Enti","Login","Registrazione",}; 
	private Fragment frags[] = new Fragment[titles.length]; 

	public TitleAdapterInitial(FragmentManager fm) {
		super(fm);
		frags[0]= new MainActivitySummary();
		frags[1]= new LoginActivity();
		frags[2] = new RegistrationActivity();
	}

	@Override
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
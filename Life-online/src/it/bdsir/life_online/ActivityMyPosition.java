package it.bdsir.life_online;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityMyPosition extends Fragment {

	private View myView;
	private GoogleMap map;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getmyMap();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		myView = inflater.inflate(R.layout.activity_my_position, container, false);
		return myView;
	}

	@Override
	public void onResume(){
		getmyMap();
		super.onResume();
	}
	
	@Override
	public void onPause() {
		map = null;
		super.onPause();
	}
	
	private GoogleMap getmyMap(){
		if(map == null)
			map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		return map;
	}
}

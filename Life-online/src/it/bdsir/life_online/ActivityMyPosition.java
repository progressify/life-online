package it.bdsir.life_online;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityMyPosition extends Fragment {

	private View myView;
	private static GoogleMap map;
	private static MarkerOptions marker=null;

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

	public static void addMarkerOnMaps(Double lat, Double lng, Context cont){
		StringBuilder sb=null;
		LatLng coordinate=new LatLng(lat, lng);
		Geocoder gc = new Geocoder(cont, Locale.getDefault());
		try {
			List<Address> addresses = gc.getFromLocation(lat, lng, 1);
			sb = new StringBuilder();
			if (addresses.size() > 0) {
				Address address = addresses.get(0);
				for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
					sb.append(address.getAddressLine(i)).append("\n");
					sb.append(address.getLocality()).append("\n");
					sb.append(address.getPostalCode()).append("\n");
					sb.append(address.getCountryName());
				}
			}
		} catch (Exception e) {	}
		if (marker!=null){ 
			marker.position(coordinate);
		} else {
			marker = new MarkerOptions().position(coordinate).title("Sei Qui\n"+sb);
			map.addMarker(marker);
		}
		CameraUpdate center=CameraUpdateFactory.newLatLng(coordinate);
		CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
		map.moveCamera(center);
		map.animateCamera(zoom);
	}

	private GoogleMap getmyMap(){
		if(map == null)
			map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		return map;
	}
}

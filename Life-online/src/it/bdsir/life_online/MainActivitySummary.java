package it.bdsir.life_online;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivitySummary extends Fragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View myView = inflater.inflate(R.layout.activity_main_activity_summary, container, false);

		//String[] cols = new String[]{"Polizia di Stato", "Vigili del fuoco", "Carabinieri", "Emergenza sanitaria","Capitaneria di porto","Corpo forestale","Telefono Azzurro"};
		String[] cols=getResources().getStringArray(R.array.array_enti);
		ListView list = (ListView) myView.findViewById(R.id.list);
		ListAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cols);
		list.setAdapter(adapter);

		return myView;
	}	
}

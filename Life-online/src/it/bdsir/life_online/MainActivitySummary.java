package it.bdsir.life_online;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivitySummary extends Fragment implements OnItemClickListener{

	private View myView;
	private boolean flag;
	private ListView list;
	private ViewPager vp;
	private String[] cols;
	private Singleton sing =Singleton.getInstance();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.activity_main_activity_summary, container, false);

		vp= (ViewPager) getActivity().findViewById(R.id.viewpager);
		cols=getResources().getStringArray(R.array.array_enti);
		flag=getData();
		list = (ListView) myView.findViewById(R.id.list);
		ListAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cols);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		return myView;
	}	

	private boolean getData() {
		//richiama il file delle preferenze
		SharedPreferences prefs = getActivity().getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
		Map<String, ?> loginPrefs = prefs.getAll();
		if(loginPrefs.size()!=0) {
			return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		switch ((int)id) {
		case 0: //polizia di stato
			switcher("113",cols[0]);
			break;
		case 1: //vigili del fuoco
			switcher("115",cols[1]);
			break;
		case 2: //carabinieri
			switcher("112",cols[2]);
			break;
		case 3: //emergenza sanitaria
			switcher("118",cols[3]);
			break;
		case 4: //capitaneria di porto
			switcher("1530",cols[4]);
			break;
		case 5: //corpo forestale
			switcher("000",cols[5]);
			break;
		case 6: //telefono azzurro
			switcher("000",cols[6]);
			break;
		}
	}

	private void switcher(String number,String ente) {
		if(flag){
			//è loggato
			vp.setCurrentItem(1);
			sing.setEnte(ente);
			sing.setNumero(number);
			Toast.makeText(getActivity(), sing.getEnte()+" "+getResources().getString(R.string.toast_main_seleziona) ,Toast.LENGTH_SHORT).show();
		} else {
			//non loggato
			startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number)));
		}
	}
}
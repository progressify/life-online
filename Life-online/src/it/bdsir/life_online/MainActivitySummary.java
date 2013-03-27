package it.bdsir.life_online;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivitySummary extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity_summary);
		String[] cols = new String[]{"Polizia di Stato", "Vigili del fuoco", "Carabinieri", "Emergenza sanitaria","Capitaneria di porto","Corpo forestale","Telefono Azzurro"}; 
		ListView list = (ListView)this.findViewById(R.id.list);
		ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
		cols);
		list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_summary, menu);
		return true;
	}

}

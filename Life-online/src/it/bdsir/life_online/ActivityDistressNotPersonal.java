package it.bdsir.life_online;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
public class ActivityDistressNotPersonal extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_activity_distress_not_personal);
                
                Spinner s = (Spinner) findViewById(R.id.spinner);
                Spinner ss = (Spinner) findViewById(R.id.spinner2);
                Spinner sss = (Spinner) findViewById(R.id.spinner3);
                final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.num_persone, android.R.layout.simple_spinner_item);
                final ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.cause, android.R.layout.simple_spinner_item);
                final ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.sintomi, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(adapter);
                ss.setAdapter(adapter2);
                sss.setAdapter(adapter3);
                s.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) { Toast.makeText(getApplicationContext(), adapter.getItem(arg2).toString(),
                Toast.LENGTH_LONG).show(); }
                

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                        
                }
                });
                
        
                ss.setOnItemSelectedListener(new OnItemSelectedListener() {
                	@Override
                	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) { Toast.makeText(getApplicationContext(), adapter2.getItem(arg2).toString(),
                			Toast.LENGTH_LONG).show(); }

                	@Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                            
                    }
                    });
                
                sss.setOnItemSelectedListener(new OnItemSelectedListener() {
                	@Override
                	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) { Toast.makeText(getApplicationContext(), adapter3.getItem(arg2).toString(),
                			Toast.LENGTH_LONG).show(); }

                	@Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                            
                    }
                    });
                
        
        }
        
       
       

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(
                                R.menu.activity_activity_distress_not_personal, menu);
                return true;
        }

}
	
	

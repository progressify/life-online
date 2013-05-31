package it.bdsir.life_online;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class ActivityServiceInfo extends Activity implements OnClickListener{

	private EditText editDataN; 
	private Dialog dialog;
	private Calendar calen=Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_service_info);
		editDataN=(EditText)findViewById(R.id.editText3);
		editDataN.setInputType(InputType.TYPE_NULL);
		editDataN.setFocusable(false);
		editDataN.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_activity_service_info, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		dialog = new Dialog(this);
		dialog.setContentView(R.layout.date_layout);
		dialog.setCancelable(true);
		//dialog.setTitle(getResources().getString(R.string.popup_preventivi_data));
		DatePicker dp = (DatePicker)dialog.findViewById(R.id.datePicker1);
		Button buttonSet=(Button)dialog.findViewById(R.id.buttonSet);
		buttonSet.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (v.getId()== R.id.buttonSet){
					onDateSet();
				}
			}
		});
		int year = calen.get(Calendar.YEAR);
		int month = calen.get(Calendar.MONTH);
		int day = calen.get(Calendar.DAY_OF_MONTH);
		dp.init(year, month, day, new DatePicker.OnDateChangedListener() {
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				calen.set(year, monthOfYear, dayOfMonth);
			}
		});
		dialog.show();
	}
	
	private void onDateSet(){
		editDataN.setText(calen.get(Calendar.DAY_OF_MONTH)+"-"+(calen.get(Calendar.MONTH)+1)+"-"+calen.get(Calendar.YEAR));
		dialog.dismiss();
	}
}

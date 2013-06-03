package it.bdsir.life_online;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

public class ActivityServiceInfo extends Activity implements OnClickListener{

	private EditText editDataN; 
	private Calendar calen=Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_service_info);
		editDataN=(EditText)findViewById(R.id.edit_data_nascita);
		editDataN.setInputType(InputType.TYPE_NULL);
		editDataN.setFocusable(false);
		editDataN.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_activity_service_info, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.edit_data_nascita){
			int year = calen.get(Calendar.YEAR);
			int month = calen.get(Calendar.MONTH);
			int day = calen.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog dialogs = new DatePickerDialog(this,new OnDateSetListener() {
				public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
					calen.set(year, monthOfYear, dayOfMonth);
					editDataN.setText(calen.get(Calendar.DAY_OF_MONTH)+"-"+(calen.get(Calendar.MONTH)+1)+"-"+calen.get(Calendar.YEAR));
				}
			}, year, month, day);
			dialogs.setTitle("Imposta Data");
			dialogs.show();
		}
	}
}

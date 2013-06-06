package it.bdsir.life_online;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

public class ActivityServiceInfo extends Fragment implements OnClickListener{

	private EditText editDataN; 
	private Calendar calen=Calendar.getInstance();
	private View myView;
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.activity_service_info, container, false);

		editDataN=(EditText) myView.findViewById(R.id.edit_data_nascita);
		editDataN.setInputType(InputType.TYPE_NULL);
		editDataN.setFocusable(false);
		editDataN.setOnClickListener(this);

		return myView;
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.edit_data_nascita){
			int year = calen.get(Calendar.YEAR);
			int month = calen.get(Calendar.MONTH);
			int day = calen.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog dialogs = new DatePickerDialog(getActivity(),new OnDateSetListener() {
				public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
					calen.set(year, monthOfYear, dayOfMonth);
					editDataN.setText(calen.get(Calendar.DAY_OF_MONTH)+"-"+(calen.get(Calendar.MONTH)+1)+"-"+calen.get(Calendar.YEAR));
				}
			}, year, month, day);
			dialogs.setTitle(getResources().getString(R.string.popup_imposta_data));
			dialogs.show();
		}
	}
}

package it.bdsir.life_online;

import java.util.Calendar;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.SharedPreferences;
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

	private EditText editDataN, editNome, editCogn, editInfo, editUser, editCodFis; 
	private Calendar calen=Calendar.getInstance();
	private View myView;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.activity_service_info, container, false);

		editUser=(EditText) myView.findViewById(R.id.edit_username);
		editCodFis=(EditText) myView.findViewById(R.id.edit_cod_fiscale);
		editCogn=(EditText) myView.findViewById(R.id.edit_cognome);
		editNome=(EditText) myView.findViewById(R.id.edit_nome);
		editInfo=(EditText) myView.findViewById(R.id.edit_info_agg);
		editDataN=(EditText) myView.findViewById(R.id.edit_data_nascita);
		editDataN.setInputType(InputType.TYPE_NULL);
		editDataN.setFocusable(false);
		editDataN.setOnClickListener(this);
		getData();

		return myView;
	}

	private boolean getData() {
		//richiama il file delle preferenze
		SharedPreferences prefs = getActivity().getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
		Map<String, ?> loginPrefs = prefs.getAll();
		//se non esistono preferenze preimpostate allora i campi saranno vuoti (la prima volta), altrimenti li riempie
		if(loginPrefs.size()!=0) {
			String user = prefs.getString(getResources().getString(R.string.PREFERENCES_USR), "");
			//Log.e("LO", "user: "+user);
			editUser.setText(user);
			String codfis =prefs.getString(getResources().getString(R.string.PREFERENCES_COD_FIS), "");
			//Log.e("LO", "codfis: "+codfis);
			editCodFis.setText(codfis);
			String cogn =prefs.getString(getResources().getString(R.string.PREFERENCES_COGNOME), "");
			//Log.e("LO", "cogn: "+cogn);
			editCogn.setText(cogn);
			String nome =prefs.getString(getResources().getString(R.string.PREFERENCES_NOME), "");
			//Log.e("LO", "nome: "+nome);
			editNome.setText(nome);
			String segnipart =prefs.getString(getResources().getString(R.string.PREFERENCES_SEGNI_PART), "");
			//Log.e("LO", "segni: "+segnipart);
			editInfo.setText(segnipart);
			String datan =prefs.getString(getResources().getString(R.string.PREFERENCES_DATAN), "");
			//Log.e("LO", "datan: "+segnipart);
			editDataN.setText(datan);
			return true;
		}
		return false;
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
		if (v.getId()==R.id.buttonOk){
			//aporta le modifiche alle informazioni personali
		}
	}
}

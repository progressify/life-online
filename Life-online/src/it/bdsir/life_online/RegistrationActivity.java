package it.bdsir.life_online;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends Fragment implements OnClickListener {

	private View myView;
	private String ERROR_LOG="LO";
	private Button okButton;
	private EditText editDataN,editUsername,editPassword, editCodiceFis, editNome,editCogn,editInfoAgg;
	private Calendar calen=Calendar.getInstance();
	private ProgressDialog pd;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.activity_registration, container, false);

		pd=new ProgressDialog(getActivity());
		pd.setCancelable(false);
		pd.setMessage(getResources().getString(R.string.progress_dialog_reg));
		okButton=(Button)myView.findViewById(R.id.buttonOk);
		okButton.setOnClickListener(this);
		editDataN=(EditText) myView.findViewById(R.id.edit_reg_data_nascita);
		editDataN.setInputType(InputType.TYPE_NULL);
		editDataN.setFocusable(false);
		editDataN.setOnClickListener(this);
		

		return myView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.buttonOk){
			pd.show();
			HttpGetTask task;
			ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				task = new HttpGetTask();
				task.execute(editUsername.getText().toString(),editPassword.getText().toString());
			} else {
				//chiudo la dialog e avviso che non c'� connessione
				pd.dismiss();
				Toast.makeText(getActivity(), R.string.toast_connection_unavailable ,Toast.LENGTH_LONG).show();
				return;
			}
		}
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

	private class HttpGetTask extends AsyncTask<String,String,String>  {
		JSONArray jArray;
		InputStream is = null;
		StringBuilder sb=null;
		HttpResponse response=null;
		HttpGet get;

		protected void onPreExecute() {
			Log.e(ERROR_LOG,"on pre execute");
		}

		@Override
		protected String doInBackground(String... params) {
			String result = null;
			HttpClient client=null;
			// interrogazione del web service
			try {
				String url="http://lifeonline.altervista.org/app/logger.php?usr="+params[0]+"&psw="+params[1];
				Log.e(ERROR_LOG,"URL: "+url);
				client = new DefaultHttpClient();
				final HttpParams httpParams = client.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
				HttpConnectionParams.setSoTimeout(httpParams, 20000);
				get = new HttpGet(url);
				response = client.execute(get);
				if (response!=null) try {
					HttpEntity entity = response.getEntity();
					is = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
					sb = new StringBuilder();
					sb.append(reader.readLine() + "\n");
					String line = "0";
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					is.close();
					result = sb.toString();
				} catch (Exception e) {
					Log.e(ERROR_LOG, "Error converting result " + e.toString());
				}
			} catch (HttpResponseException e) {
				// gestisce le risposte diverse da HTTP code 200
				Log.e(ERROR_LOG,"HTTP Response Exception : "+e.toString());
			} catch (Exception e) {
				Log.e(ERROR_LOG,"Error : "+e.toString());
			} finally {
				if (client != null) client.getConnectionManager().shutdown();
			}
			return result;
		}

		protected void onPostExecute(String result) {
			Log.e(ERROR_LOG, "trovato: "+result);
			//parsing data
			if (result!=null) try{
				jArray = new JSONArray(result);
				Log.e(ERROR_LOG, "Lunghezza array trovato: "+jArray.length());
				JSONObject json_data=null;
				//se il login è andato a buon
				if(jArray.length()==1){
					json_data = jArray.getJSONObject(0);
					Log.e(ERROR_LOG, "trovato id: "+json_data.getString("id"));							String id=json_data.getString("id");
					Log.e(ERROR_LOG, "trovato user: "+json_data.getString("username"));					String username=json_data.getString("username");
					Log.e(ERROR_LOG, "trovato pass: "+json_data.getString("password"));					String password=json_data.getString("password");
					Log.e(ERROR_LOG, "trovato nome: "+json_data.getString("nome"));						String nome=json_data.getString("nome");
					Log.e(ERROR_LOG, "trovato user: "+json_data.getString("codice_fiscale"));			String cod_fis=json_data.getString("codice_fiscale");
					Log.e(ERROR_LOG, "trovato user: "+json_data.getString("cognome"));					String cogn=json_data.getString("cognome");
					Log.e(ERROR_LOG, "trovato user: "+json_data.getString("segni_particolari"));		String segni_part=json_data.getString("segni_particolari");
					//chiudo la progressDialog dopo fatto il login e salvo tutto nelle shared preferences
					pd.dismiss();
					//saveData(id, username, password, nome, cod_fis, cogn, segni_part);
					//apre la HomeActivity, saluta e termina l'activity di login
					Toast.makeText(getActivity(), getResources().getString(R.string.toast_login_success) ,Toast.LENGTH_LONG).show();
					startActivity(new Intent(getActivity(), PagerActivity.class));
					getActivity().finish();
				}
				else{
					//se non è andato a buon fine il login restituisce un messaggio di errore e termina l'esecuzione del metodo
					pd.dismiss();
					Toast.makeText(getActivity(), getResources().getString(R.string.toast_login_failed_invalid_usrpsw) ,Toast.LENGTH_LONG).show();
				}
			}
			catch(JSONException e1){
				pd.dismiss();
				Log.e(ERROR_LOG, "Nessun dato trovato: "+e1);
				//se non è andato a buon fine il login restituisce un messaggio di errore e termina l'esecuzione del metodo
				Toast.makeText(getActivity(), getResources().getString(R.string.toast_login_failed_invalid_usrpsw) ,Toast.LENGTH_LONG).show();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			else{
				pd.dismiss();
				Log.e(ERROR_LOG, "Errore di rete");
				Toast.makeText(getActivity(), getResources().getString(R.string.toast_connection_unavailable) ,Toast.LENGTH_LONG).show();
			}
		}//fine asynctask
	}
}

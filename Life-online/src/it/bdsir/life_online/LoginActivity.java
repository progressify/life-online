package it.bdsir.life_online;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Fragment implements OnClickListener{

	private ProgressDialog pd;
	private String ERROR_LOG="LO";
	private Button buttonLogin;
	private EditText editUsr,editPsw;
	private View myView;
	private ViewPager vp;
	private Singleton sing = Singleton.getInstance();

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.activity_login, container, false);

		pd=new ProgressDialog(getActivity());
		pd.setCancelable(false);
		pd.setMessage(getResources().getString(R.string.progress_dialog_login));
		vp= (ViewPager) getActivity().findViewById(R.id.viewpager);
		buttonLogin = (Button) myView.findViewById(R.id.button_login);
		buttonLogin.setOnClickListener(this);

		if(getData()){
			pd.show();
			HttpGetTask task;
			ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				task = new HttpGetTask();
				task.execute(editUsr.getText().toString(),editPsw.getText().toString());
			} else {
				//chiudo la dialog e avviso che non c'� connessione
				pd.dismiss();
				Toast.makeText(getActivity(), R.string.toast_connection_unavailable ,Toast.LENGTH_LONG).show();
				vp.setCurrentItem(0);
			}
		}
		return myView;
	}

	private boolean getData() {
		//richiama il file delle preferenze
		SharedPreferences prefs = getActivity().getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
		Map<String, ?> loginPrefs = prefs.getAll();
		//se non esistono preferenze preimpostate allora i campi saranno vuoti (la prima volta), altrimenti li riempie
		if(loginPrefs.size()!=0) {
			String user = prefs.getString(getResources().getString(R.string.PREFERENCES_USR), "");
			editUsr = (EditText) myView.findViewById(R.id.edit_username);
			editUsr.setText(user);
			String psw =prefs.getString(getResources().getString(R.string.PREFERENCES_PSW), "");
			editPsw=(EditText) myView.findViewById(R.id.edit_password);
			editPsw.setText(psw);
			return true;
		}
		return false;
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
					Log.e(ERROR_LOG, "trovato cod: "+json_data.getString("codice_fiscale"));			String cod_fis=json_data.getString("codice_fiscale");
					Log.e(ERROR_LOG, "trovato cogn: "+json_data.getString("cognome"));					String cogn=json_data.getString("cognome");
					Log.e(ERROR_LOG, "trovato segn: "+json_data.getString("segni_particolari"));		String segni_part=json_data.getString("segni_particolari");
					Log.e(ERROR_LOG, "trovato datan: "+json_data.getString("data_nascita"));			String datanasc=json_data.getString("data_nascita");
					//chiudo la progressDialog dopo fatto il login e salvo tutto nelle shared preferences
					pd.dismiss();
					saveData(id, username, password, nome, cod_fis, cogn, segni_part,datanasc);
					//apre la HomeActivity, saluta e termina l'activity di login
					Toast.makeText(getActivity(), getResources().getString(R.string.toast_login_success) ,Toast.LENGTH_SHORT).show();
					startActivity(new Intent(getActivity(), PagerActivity.class));
					getActivity().finish();
				}
				else{
					//se non è andato a buon fine il login restituisce un messaggio di errore e termina l'esecuzione del metodo
					pd.dismiss();
					Toast.makeText(getActivity(), getResources().getString(R.string.toast_login_failed_invalid_usrpsw) ,Toast.LENGTH_SHORT).show();
				}
			}
			catch(JSONException e1){
				pd.dismiss();
				Log.e(ERROR_LOG, "Nessun dato trovato: "+e1);
				//se non è andato a buon fine il login restituisce un messaggio di errore e termina l'esecuzione del metodo
				Toast.makeText(getActivity(), getResources().getString(R.string.toast_login_failed_invalid_usrpsw) ,Toast.LENGTH_SHORT).show();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			else{
				pd.dismiss();
				Log.e(ERROR_LOG, "Errore di rete");
				Toast.makeText(getActivity(), getResources().getString(R.string.toast_connection_unavailable) ,Toast.LENGTH_LONG).show();
			}
		}//fine asynctask

		private void saveData(String... params) {
			//id, username, password, nome, cod_fis, cogn, segni_part
			//richiama il file delle preferenze
			SharedPreferences prefs = getActivity().getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
			//memorizza tutto nelle preferences
			SharedPreferences.Editor prefsEditor = prefs.edit();
			prefsEditor.putString(getResources().getString(R.string.PREFERENCES_ID), params[0]);
			sing.setId(params[0]);
			prefsEditor.putString(getResources().getString(R.string.PREFERENCES_USR), params[1]);
			prefsEditor.putString(getResources().getString(R.string.PREFERENCES_PSW), params[2]);
			prefsEditor.putString(getResources().getString(R.string.PREFERENCES_NOME), params[3]);
			prefsEditor.putString(getResources().getString(R.string.PREFERENCES_COD_FIS), params[4]);
			prefsEditor.putString(getResources().getString(R.string.PREFERENCES_COGNOME), params[5]);
			prefsEditor.putString(getResources().getString(R.string.PREFERENCES_SEGNI_PART), params[6]);
			prefsEditor.putString(getResources().getString(R.string.PREFERENCES_DATAN), params[7]);
			prefsEditor.commit();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.button_login){
			HttpGetTask task;
			pd.show();
			editUsr=(EditText) myView.findViewById(R.id.edit_username);
			editPsw=(EditText) myView.findViewById(R.id.edit_password);
			if(editUsr.getText().toString().equals("")){
				pd.dismiss();
				Toast.makeText(getActivity(), R.string.toast_login_failed_user ,Toast.LENGTH_SHORT).show();
				return;
			}
			if(editPsw.getText().toString().equals("")){
				pd.dismiss();
				Toast.makeText(getActivity(), R.string.toast_login_failed_psw ,Toast.LENGTH_SHORT).show();
				return;
			}
			//fa il login
			ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				task = new HttpGetTask();
				task.execute(editUsr.getText().toString(),editPsw.getText().toString());
			} else {
				//chiudo la dialog e avviso che non c'� connessione
				pd.dismiss();
				Toast.makeText(getActivity(), R.string.toast_connection_unavailable ,Toast.LENGTH_LONG).show();
				vp.setCurrentItem(0);
			}
		}
	}
}

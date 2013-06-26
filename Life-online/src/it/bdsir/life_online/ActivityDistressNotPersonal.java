package it.bdsir.life_online;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityDistressNotPersonal extends Fragment implements OnClickListener{

	private String ERROR_LOG="LO";
	private ProgressDialog pd;
	private Button button_invia;
	private double lat,lon;
	private Location location;
	private LocationListener loc_listener;
	private LocationManager locationManager;
	private Singleton sing =Singleton.getInstance();
	private View myView;
	private Spinner sintomi;
	private EditText edit_sintomi_agg;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.activity_distress_not_personal, container, false);

		button_invia=(Button) myView.findViewById(R.id.button_invia);
		button_invia.setOnClickListener(this);
		edit_sintomi_agg = (EditText) myView.findViewById(R.id.edit_sintomi_agg);
		pd=new ProgressDialog(getActivity());
		pd.setCancelable(false);
		pd.setMessage(getResources().getString(R.string.progress_dialog_invio_segnalazione)); 
		Spinner numeroPersone = (Spinner) myView.findViewById(R.id.spinner_numero_feriti);
		numeroPersone.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				TextView text_sintomi = (TextView) myView.findViewById(R.id.text_sintomi);
				text_sintomi.setVisibility(4);
				sintomi.setVisibility(4);
				if(id!=0){
					text_sintomi.setVisibility(1);
					sintomi.setVisibility(1);
				} 
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {	}
		});
		Spinner cause = (Spinner) myView.findViewById(R.id.spinner_causa_malessere);
		sintomi = (Spinner) myView.findViewById(R.id.spinner_sintomi);
		sintomi.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				TextView text_sintomi_agg = (TextView) myView.findViewById(R.id.text_sintomi_agg);
				text_sintomi_agg.setVisibility(4);
				edit_sintomi_agg.setVisibility(4);
				if(id==4){
					text_sintomi_agg.setVisibility(1);
					edit_sintomi_agg.setVisibility(1);
				} 
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {	}
		});
		final ArrayAdapter<CharSequence> numPersoneBox = ArrayAdapter.createFromResource(getActivity(), R.array.num_persone, android.R.layout.simple_spinner_item);
		final ArrayAdapter<CharSequence> causeBox = ArrayAdapter.createFromResource(getActivity(), R.array.cause, android.R.layout.simple_spinner_item);
		final ArrayAdapter<CharSequence> sintomiBox = ArrayAdapter.createFromResource(getActivity(), R.array.sintomi, android.R.layout.simple_spinner_item);
		numPersoneBox.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		causeBox.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sintomiBox.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		numeroPersone.setAdapter(numPersoneBox);
		cause.setAdapter(causeBox);
		sintomi.setAdapter(sintomiBox);

		return myView;
	}

	public void onResume() {
		super.onResume();
		_getLocation();
	}

	public void onPause() {
		locationManager.removeUpdates(loc_listener);
		super.onPause();
	}

	public void onStop() {
		locationManager.removeUpdates(loc_listener);
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		HttpGetTask task=null;
		if (v.getId() == R.id.button_invia){
			pd.show();
			//controllo se il fix è avvenuto
			if(sing.getLatitudine() !="-1" && sing.isGpsEnabled()){
				ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
				if (networkInfo != null && networkInfo.isConnected()) {
					task = new HttpGetTask();
					Spinner sp= (Spinner) myView.findViewById(R.id.spinner_numero_feriti);
					String num_feriti=sp.getSelectedItem().toString();
					Log.e(ERROR_LOG, "numero feriti: "+num_feriti);
					String ente=sing.getEnte().replace(" ", "%20");
					Log.e(ERROR_LOG,sing.getId());
					Spinner sp_sintomi=(Spinner) myView.findViewById(R.id.spinner_causa_malessere);
					Spinner sp_sint=(Spinner) myView.findViewById(R.id.spinner_sintomi);
					EditText sp_sint_agg=(EditText) myView.findViewById(R.id.edit_sintomi_agg);
					String sitomi_aggiuntivi=sp_sintomi.getSelectedItem().toString() +";"+ sp_sint.getSelectedItem().toString()+";"+sp_sint_agg.getText().toString();
					Log.e(ERROR_LOG, sitomi_aggiuntivi);
					String info=sitomi_aggiuntivi.replace(" ", "%20");
					task.execute(sing.getId(),ente,sing.getLatitudine()+";"+sing.getLongitudine(),num_feriti,"terzi",info);
				} else {
					//chiudo la dialog e avviso che non c'è connessione
					pd.dismiss();
					Toast.makeText(getActivity(), R.string.toast_connection_unavailable ,Toast.LENGTH_LONG).show();
				}
			} else {
				//fix non avvenuto o gps disattivato
				pd.dismiss();
				if (sing.isGpsEnabled())
					Toast.makeText(getActivity(), R.string.toast_gps_unavailable ,Toast.LENGTH_LONG).show();
				else {
					Toast.makeText(getActivity(), R.string.toast_gps_disabled ,Toast.LENGTH_LONG).show();
					showSettingsGPSAlert();
				}
			}
		}
	}

	private void _getLocation() {
		// Get the location manager
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		checkGPS();
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); 
		String bestProvider = locationManager.getBestProvider(criteria, true);
		location = locationManager.getLastKnownLocation(bestProvider);
		loc_listener = new LocationListener() {
			public void onLocationChanged(Location l) {
				try {
					lat = location.getLatitude();
					lon = location.getLongitude();
					sing.setLatitudine(""+lat);
					sing.setLongitudine(""+lon);
					ActivityMyPosition.addMarkerOnMaps(lat, lon,getActivity());
				} catch (NullPointerException e) {
					lat = -1.0;
					lon = -1.0;
				}
				locationManager.removeUpdates(this);
				Toast.makeText(getActivity(), lat+";"+lon ,Toast.LENGTH_LONG).show();
			}
			public void onProviderEnabled(String p) {}

			public void onProviderDisabled(String p) {}

			public void onStatusChanged(String p, int status, Bundle extras) {
				Toast.makeText(getActivity(), lat+";"+lon ,Toast.LENGTH_SHORT).show();
			}
		};
		locationManager.requestLocationUpdates(bestProvider, 0, 0, loc_listener);
		location = locationManager.getLastKnownLocation(bestProvider);
		try {
			lat = location.getLatitude();
			lon = location.getLongitude();
		} catch (NullPointerException e) {
			lat = -1.0;
			lon = -1.0;
		}
		Log.e(ERROR_LOG, lat+";"+lon);
		//Toast.makeText(getActivity(), lat+";"+lon ,Toast.LENGTH_SHORT).show();
	}

	private void showSettingsGPSAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		// titolo dialog
		alertDialog.setTitle(getResources().getString(R.string.popup_titolo_gps));
		// messaggio dialog
		alertDialog.setMessage(getResources().getString(R.string.popup_messaggio_gps));
		// quando premo ok
		alertDialog.setPositiveButton(getResources().getString(R.string.popup_bottone_si_gps), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
				checkGPS();
			}
		});
		// quando premo no
		alertDialog.setNegativeButton(getResources().getString(R.string.popup_bottone_no_gps), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				sing.setGpsEnabled(false);
			}
		});
		alertDialog.setCancelable(false);
		alertDialog.show();
	}

	private void checkGPS() {
		boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!isGPSEnabled)
			showSettingsGPSAlert();
		else
			sing.setGpsEnabled(true);
	}

	private class HttpGetTask extends AsyncTask<String,String,String>  {
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
				String url="http://lifeonline.altervista.org/app/segnalazione.php?id_segnalante="+params[0]+"&ente_riferimento="+params[1]+"&coordinate_gps="+params[2]+"&persone_coinvolte="+params[3]+"&personale="+params[4]+"&informazioni_aggiuntive="+params[5];
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

		@Override
		protected void onPostExecute(String result) {
			pd.dismiss();
			Toast.makeText(getActivity(), R.string.toast_distress_segnalazione ,Toast.LENGTH_SHORT).show();
		}
	}
}

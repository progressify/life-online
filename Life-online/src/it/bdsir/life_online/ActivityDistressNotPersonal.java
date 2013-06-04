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

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
public class ActivityDistressNotPersonal extends Fragment implements OnClickListener{

	private String ERROR_LOG="LO";
	private ProgressDialog pd;
	private Button button_invia;
	private double lat,lon;
	private Location location;
	View myView;



	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.activity_activity_distress_not_personal, container, false);

		button_invia=(Button) myView.findViewById(R.id.button_invia);
		button_invia.setOnClickListener(this);
		pd=new ProgressDialog(getActivity());
		pd.setCancelable(false);
		pd.setMessage(getResources().getString(R.string.progress_dialog_invio_segnalazione)); 
		Spinner numeroPersone = (Spinner) myView.findViewById(R.id.spinner_numero_feriti);
		Spinner cause = (Spinner) myView.findViewById(R.id.spinner_causa_malessere);
		Spinner sintomi = (Spinner) myView.findViewById(R.id.spinner_sintomi);
		final ArrayAdapter<CharSequence> numPersoneBox = ArrayAdapter.createFromResource(getActivity(), R.array.num_persone, android.R.layout.simple_spinner_item);
		final ArrayAdapter<CharSequence> causeBox = ArrayAdapter.createFromResource(getActivity(), R.array.cause, android.R.layout.simple_spinner_item);
		final ArrayAdapter<CharSequence> sintomiBox = ArrayAdapter.createFromResource(getActivity(), R.array.sintomi, android.R.layout.simple_spinner_item);
		numPersoneBox.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		causeBox.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sintomiBox.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		numeroPersone.setAdapter(numPersoneBox);
		cause.setAdapter(causeBox);
		sintomi.setAdapter(sintomiBox);
		_getLocation();

		return myView;
	}



	@Override
	public void onClick(View v) {
		HttpGetTask task=null;
		if (v.getId() == R.id.button_invia){
			pd.show();
			ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				task = new HttpGetTask();
				Spinner sp= (Spinner) myView.findViewById(R.id.spinner_numero_feriti);
				String num_feriti=sp.getSelectedItem().toString();
				Log.e(ERROR_LOG, "numero feriti: "+num_feriti);
				task.execute("23423","pompieri","134234234,34545646",num_feriti,"terzi");
			} else {
				//chiudo la dialog e avviso che non c'è connessione
				pd.dismiss();
				Toast.makeText(getActivity(), R.string.toast_connection_unavailable ,Toast.LENGTH_LONG).show();
				return;
			}
		}
	}

	private void _getLocation() {
		// Get the location manager
		final LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); 
		String bestProvider = locationManager.getBestProvider(criteria, true);
		location = locationManager.getLastKnownLocation(bestProvider);
		LocationListener loc_listener = new LocationListener() {
			public void onLocationChanged(Location l) {
				try {
					lat = location.getLatitude();
					lon = location.getLongitude();
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
		Toast.makeText(getActivity(), lat+";"+lon ,Toast.LENGTH_SHORT).show();
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
				String url="http://lifeonline.altervista.org/app/segnalazione.php?id_segnalante"+params[0]+"&ente_riferimento="+params[1]+"&coordinate_gps="+params[2]+"&persone_coinvolte="+params[3]+"&personale="+params[4];
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
			Toast.makeText(getActivity(), "Connessione riuscita" ,Toast.LENGTH_LONG).show();

		}
	}


}

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
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SoccorsoActivity extends Fragment implements OnClickListener{

	private String ERROR_LOG="LO";
	private Button buttonCall,buttonHelp,buttonHelp2;
	private ViewPager vp;
	private ProgressDialog pd;
	private LocationManager locationManager;
	private Singleton sing =Singleton.getInstance();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View myView = inflater.inflate(R.layout.activity_soccorso, container, false);

		pd=new ProgressDialog(getActivity());
		pd.setCancelable(false);
		pd.setMessage(getResources().getString(R.string.progress_dialog_invio_segnalazione)); 
		vp=(ViewPager) getActivity().findViewById(R.id.viewpager);
		buttonHelp = (Button) myView.findViewById(R.id.button_help);
		buttonHelp2 = (Button) myView.findViewById(R.id.button_help_2);
		buttonCall = (Button) myView.findViewById(R.id.button_call);
		buttonHelp.setOnClickListener(this);
		buttonHelp2.setOnClickListener(this);
		buttonCall.setOnClickListener(this);

		return myView; 
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.button_help){
			if(sing.getEnte().equals("")==false){
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
				// titolo dialog
				alertDialog.setTitle(getResources().getString(R.string.popup_titolo_personale));
				// messaggio dialog
				alertDialog.setMessage(getResources().getString(R.string.popup_messaggio_personale));
				// quando premo ok
				alertDialog.setPositiveButton(getResources().getString(R.string.popup_bottone_si_personale), new DialogInterface.OnClickListener() {
					HttpGetTask task=null;
					public void onClick(DialogInterface dialog,int which) {
						pd.show();
						if(sing.getLatitudine() !="-1" && sing.isGpsEnabled()){
							ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
							NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
							if (networkInfo != null && networkInfo.isConnected()) {
								task = new HttpGetTask();
								String ente=sing.getEnte().replace(" ", "%20");
								task.execute(sing.getId(),ente,sing.getLatitudine()+";"+sing.getLongitudine(),"0","personale","n/n");
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
				});
				// quando premo no
				alertDialog.setNegativeButton(getResources().getString(R.string.popup_bottone_no_gps), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						vp.setCurrentItem(0);
					}
				});
				alertDialog.show();
			} else {
				Toast.makeText(getActivity(), R.string.toast_soccorso_seleziona_ente ,Toast.LENGTH_SHORT).show();
				vp.setCurrentItem(0);
			}
		}
		if(v.getId()==R. id.button_help_2){
			if(sing.getEnte().equals("")==false){
				vp.setCurrentItem(2);
				Toast.makeText(getActivity(), R.string.toast_soccorso_non_personale ,Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), R.string.toast_soccorso_seleziona_ente ,Toast.LENGTH_SHORT).show();
				vp.setCurrentItem(0);
			}
		}
		if(v.getId()==R.id.button_call){
			if(sing.getNumero().equals("")==false){
				//bisogna cambiare il numero a seconda dell'ente selezionato
				String toDial = "tel: "+sing.getNumero();
				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(toDial)));
			} else {
				Toast.makeText(getActivity(), R.string.toast_soccorso_seleziona_ente ,Toast.LENGTH_SHORT).show();
				vp.setCurrentItem(0);
			}
		}
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

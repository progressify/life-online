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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private ProgressDialog pd;
	private String ERROR_LOG="LO";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		
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

		@Override
		protected void onPostExecute(String result) {
			pd.dismiss();
		}
	}
}

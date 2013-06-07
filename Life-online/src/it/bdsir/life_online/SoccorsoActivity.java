package it.bdsir.life_online;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SoccorsoActivity extends Fragment implements OnClickListener{

	private Button buttonCall,buttonHelp,buttonHelp2;
	private ViewPager vp;
	private Singleton sing =Singleton.getInstance();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View myView = inflater.inflate(R.layout.activity_soccorso, container, false);

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
				alertDialog.setTitle(getResources().getString(R.string.popup_titolo_gps));
				// messaggio dialog
				alertDialog.setMessage(getResources().getString(R.string.popup_messaggio_gps));
				// quando premo ok
				alertDialog.setPositiveButton(getResources().getString(R.string.popup_bottone_si_gps), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
						//qui devo creare un asynctask per inviare la segnalazione personale
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

}

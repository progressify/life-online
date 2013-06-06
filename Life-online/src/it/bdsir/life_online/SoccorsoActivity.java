package it.bdsir.life_online;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SoccorsoActivity extends Fragment implements OnClickListener{

	Button buttonCall,buttonHelp,buttonHelp2;
	ViewPager vp;
	
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
			
		}
		if(v.getId()==R. id.button_help_2){
			vp.setCurrentItem(2);
		}
		if(v.getId()==R.id.button_call){
			
		}
	}

}

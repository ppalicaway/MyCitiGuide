package com.cellcity.citiguide.screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;

public class FlightScreen extends CitiGuideActivity implements OnKeyListener {
	public static FlightScreen instance;
	
	// share data
	private String rId;
	private String id;
	private String headerTxt;

	public static String queryString = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Translucent);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.flight);
		
		instance = this;
		
		initActivity(instance, "");

		settingLayout();
	}

	 
	private void settingLayout(){
		//#### read share data ####
		SharedPreferences preferences = getSharedPreferences(Constants.DEFAUL_SHARE_DATA, 0);
		rId = preferences.getString("rId", "1");
		id = preferences.getString("id", "1");
		headerTxt = preferences.getString("name", "");
		//#########################
		
		
		TextView titleView = (TextView)findViewById(R.id.templateTopTitleTView);
		titleView.setText(headerTxt);
		
		final TextView airlineCodeT = (TextView)findViewById(R.id.airlineCodeET);
		final TextView flightNumT = (TextView)findViewById(R.id.flightNoET);
		final DatePicker dPicker = (DatePicker)findViewById(R.id.DatePicker01);
	
		airlineCodeT.setOnKeyListener(this);
		flightNumT.setOnKeyListener(this);
		
		Button buttonToday = (Button)findViewById(R.id.buttonSearch);
		buttonToday.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				disableKeyboard();
				
				SharedPreferences preferences = getSharedPreferences(Constants.DEFAUL_SHARE_DATA, 0);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("name", headerTxt);
				
				String airlineCode = airlineCodeT.getText().toString();
				String flightNo = flightNumT.getText().toString();
				String date = dPicker.getYear()+"-"+(dPicker.getMonth()+1)+"-"+dPicker.getDayOfMonth();
				
				FlightResultScreen.URL = Constants.URL_FLIGHT + "&airline="
						+ airlineCode + "&flight=" + flightNo + "&ddate="
						+ date;
				Intent intent = new Intent(instance, FlightResultScreen.class);
				startActivityForResult(intent, 0);
				if (editor.commit()) {
					setResult(RESULT_OK);
				}
			}
		});
		
		Util.showKeyboard(instance);
	}
	
	public void disableKeyboard(){
		EditText editText = (EditText)findViewById(R.id.airlineCodeET);
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}


	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode){
		case KeyEvent.KEYCODE_ENTER:
			disableKeyboard();
			return true;
		}
		return false;
	}

}
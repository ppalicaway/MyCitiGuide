package com.cellcity.citiguide.screen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cellcity.citiguide.util.Constants;

public class WeatherScreen extends CitiGuideActivity  {
	public static WeatherScreen instance;
	private ProgressDialog progressDialog = null;
	private Runnable initR;
	
	private Bitmap bitmap;
	private int iconW, iconH;	
	
	private Spinner fromSpinner, toSpinner;
	private ArrayAdapter<Object> fromAdapter, toAdapter;
	private EditText editText;
	
	// share data
	private String rId;
	private String id;
	private String headerTxt;

	public static String queryString = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setTheme(R.)
		setTheme(R.style.Theme_Translucent);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.weather);
		
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
		
//		titleView.setText(Html.fromHtml("<b>text3:</b>  Text with a "
//				+ "<font color=\"red\">link</font> "
//				+ "created in the Java source code using HTML."));
		
		final Button buttonToday = (Button)findViewById(R.id.buttonToday);
		buttonToday.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ListingWeatherScreen.URL = Constants.URL_WEATHER_TODAY;
				
				SharedPreferences preferences = getSharedPreferences(Constants.DEFAUL_SHARE_DATA, 0);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("name", buttonToday.getText().toString());
			
				Intent intent = new Intent(instance, ListingWeatherScreen.class);
				instance.startActivityForResult(intent, 0);
				if (editor.commit()) {
					setResult(RESULT_OK);
				}
			}
		});
		
		final Button buttonForecast = (Button)findViewById(R.id.buttonForecast);
		buttonForecast.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					ListingWeatherScreen.URL = Constants.URL_WEATHER_FORECAST;
				
				SharedPreferences preferences = getSharedPreferences(Constants.DEFAUL_SHARE_DATA, 0);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("name", buttonForecast.getText().toString());
		
				Intent intent = new Intent(instance, ListingWeatherScreen.class);
				instance.startActivityForResult(intent, 0);
				if (editor.commit()) {
					setResult(RESULT_OK);
				}
			}
		});
		
	}

}
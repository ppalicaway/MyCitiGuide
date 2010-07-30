package com.cellcity.citiguide.screen;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cellcity.citiguide.info.MerchantInfo;
import com.cellcity.citiguide.info.MerchantInfo1;
import com.cellcity.citiguide.map.GPSLocationListener;
import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;
import com.cellcity.citiguide.info.CategoryInfo;

public class DirectionInputScreen extends CitiGuideActivity  {
	public static DirectionInputScreen instance;
	
	// share data
	private String rId;
	private String id;
	private String headerTxt;

	public static MerchantInfo1 merchantInfo = null;
	private EditText yourPositionET;
	private double lat, lng;
	private LocationManager myLocationManager;
	private GPSLocationListener locationListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Translucent);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.direction);
		
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
		
		/**
		 * GET ADDRESS NAME
		 */
		String addressName = "";
		myLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationListener = new GPSLocationListener();
		//myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100, locationListener);
		if(myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 300, locationListener);
		}
		else {
			myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 300, locationListener);
		}
		//Get the current location in start-up
		lat = Constants.BANGKOK_LAT;
		lng = Constants.BANGKOK_LONG;
		try {
			//System.out.println("lat lng : " + lat + " " + lng);
			if(myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				lat = myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
				lng = myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
			}
			else {
				lat = myLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
				lng = myLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
			}
			System.out.println("lat lng : " + lat + " " + lng);
		} catch (Exception e) {
			System.out.println("No Address");
		}
		//Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
		//try {
		//	List<Address> addresses = geoCoder.getFromLocation(lat, lng, 1);
		//	String add = "";
		//	if (addresses.size() > 0) {
		//		for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
		//			add += addresses.get(0).getAddressLine(i) + " ";
		//	}
		//	addressName = add;
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}
		try {
			addressName = Util.getGPSAddress(instance, lat, lng);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		if(addressName.trim().equalsIgnoreCase("")){
			addressName = "Can not get address from GPS. Please insert your location.";
		}
		
		yourPositionET = (EditText)findViewById(R.id.yourPositionET);
		yourPositionET.setVisibility(EditText.GONE);
		final TextView yourPositionTV = (TextView)findViewById(R.id.yourPositionTV);
		yourPositionTV.setVisibility(TextView.VISIBLE);
		yourPositionTV.setText(addressName);
		final TextView toPositionTV = (TextView)findViewById(R.id.toPositionTV);
		toPositionTV.setText(merchantInfo.getPostalAddress());
		
		final Button buttonOK = (Button)findViewById(R.id.buttonOK);
		final Button buttonCancel = (Button)findViewById(R.id.buttonCancel);
		final Button buttonChange = (Button)findViewById(R.id.buttonChange);
		
		buttonOK.setVisibility(Button.GONE);
		buttonCancel.setVisibility(Button.GONE);
		
		buttonOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				disableKeyboard();
				buttonChange.setVisibility(Button.VISIBLE);
				buttonOK.setVisibility(Button.GONE);
				buttonCancel.setVisibility(Button.GONE);
				
				yourPositionET.setVisibility(EditText.GONE);
				yourPositionTV.setVisibility(TextView.VISIBLE);
				yourPositionTV.setText(yourPositionET.getText().toString());
			}
		});
		
		buttonCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				disableKeyboard();
				buttonChange.setVisibility(Button.VISIBLE);
				buttonOK.setVisibility(Button.GONE);
				buttonCancel.setVisibility(Button.GONE);
				
				yourPositionET.setVisibility(EditText.GONE);
				yourPositionTV.setVisibility(TextView.VISIBLE);
			}
		});
	
		buttonChange.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				buttonChange.setVisibility(Button.GONE);
				buttonOK.setVisibility(Button.VISIBLE);
				buttonCancel.setVisibility(Button.VISIBLE);
				
				yourPositionET.setVisibility(EditText.VISIBLE);
				yourPositionTV.setVisibility(TextView.GONE);
				Util.showKeyboard(instance);
			}
		});
		
		Button buttonSearch = (Button)findViewById(R.id.buttonSearch);
		buttonSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				disableKeyboard();
				
				String sourceAddr = lat+","+lng;
				if(yourPositionET.getText().toString().length() > 0){
					sourceAddr = yourPositionET.getText().toString();
				}
                Intent navigation = new Intent(Intent.ACTION_VIEW, 
                		Uri.parse("http://maps.google.com/maps?saddr="+sourceAddr+
                				"&daddr="+Double.parseDouble(merchantInfo.getLatitude())+","+
                				Double.parseDouble(merchantInfo.getLongitude())));
                startActivity(navigation); 
			}
		});
	}
	
	public void disableKeyboard(){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(yourPositionET.getWindowToken(), 0);
	}
	
	@Override
	protected void onPause() {
		myLocationManager.removeUpdates(locationListener);
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		if(myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 300, locationListener);
		}
		else {
			myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 300, locationListener);
		}
		super.onResume();
	}
}
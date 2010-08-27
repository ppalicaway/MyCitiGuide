package com.cellcity.citiguide.screen;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;

public class NewNearbyScreen extends CitiGuideActivity implements OnClickListener {

	public static NewNearbyScreen instance;
	private double latitude, longitude;
	private static String nearbyText = "";
	private EditText nearbyET;
	private TextView nearbyTV;
	private CheckBox cbDining;
	private CheckBox cbPubs;
	private CheckBox cbShopping;
	private static String keyWord = "Dining,Pubs,Shopping";
	private static boolean isDining = true;
	private static boolean isPubs = true;
	private static boolean isShopping = true;
	private String myLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.newnearby);
		
		instance = this;
		
		initActivity(instance, getText(R.string.nearby).toString());
		init();
	}
	
	private void init() {
		getAddress();
		settingLayout();
		
		cbDining = (CheckBox)findViewById(R.id.checkboxdining);
		cbPubs = (CheckBox)findViewById(R.id.checkboxpubs);
		cbShopping = (CheckBox)findViewById(R.id.checkboxshopping);
		
		cbDining.setChecked(true);
		cbPubs.setChecked(true);
		cbShopping.setChecked(true);
		
		cbDining.setOnClickListener(this);
		cbPubs.setOnClickListener(this);
		cbShopping.setOnClickListener(this);
	}

	private void settingLayout() {
		Button map = (Button)findViewById(R.id.mapButton);
		Button ar = (Button)findViewById(R.id.arButton);
		map.setVisibility(Button.GONE);
		ar.setVisibility(Button.GONE);
		
		
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.widget35);
		linearLayout.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		
		TextView titleTView = (TextView)findViewById(R.id.templateTopTitleTView);
		titleTView.setText(getString(R.string.nearby));
		
		Button homeButton = (Button)findViewById(R.id.homeButton);
		homeButton.setOnClickListener(new MenuListener());
		
		Button bCancel = (Button)findViewById(R.id.buttonCancel);
		bCancel.setOnClickListener(this);
		Button bSearch = (Button)findViewById(R.id.buttonSearch);
		bSearch.setOnClickListener(this);
		Button bChange = (Button)findViewById(R.id.buttonChange);
		bChange.setOnClickListener(this);
		
		nearbyTV = (TextView)findViewById(R.id.nearbyDesc);
		nearbyET = (EditText)findViewById(R.id.searchEText);
		if(nearbyText.equalsIgnoreCase("")) {
			nearbyTV.setVisibility(TextView.GONE);
			nearbyET.setVisibility(EditText.VISIBLE);
			bSearch.setVisibility(Button.VISIBLE);
			bCancel.setVisibility(Button.VISIBLE);
			bChange.setVisibility(Button.GONE);
		}
		else {
			nearbyTV.setVisibility(TextView.VISIBLE);
			nearbyET.setVisibility(EditText.GONE);
			nearbyTV.setText(nearbyText);
			bSearch.setVisibility(Button.VISIBLE);
			bCancel.setVisibility(Button.GONE);
			bChange.setVisibility(Button.VISIBLE);
		}
	}

	private void getAddress() {
		String addressName = "";
		latitude = Constants.SINGAPORE_LATITUDE;
		longitude = Constants.SINGAPORE_LONGITUDE;
		
		// Get Lat Long using Android API
		try {
			if(MainCitiGuideScreen.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				latitude = MainCitiGuideScreen.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
				longitude = MainCitiGuideScreen.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
			}
			else {
				latitude = MainCitiGuideScreen.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
				longitude = MainCitiGuideScreen.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// If Android API fails, Use nearest Cell Network Location.
		try {
			if((latitude == Constants.SINGAPORE_LATITUDE && longitude == Constants.SINGAPORE_LONGITUDE) ||
			   (latitude == 0.0 && longitude == 0.0)) {
				double[] latLong = Util.queryLatLong(instance);
				latitude = latLong[0];
				longitude = latLong[1];
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		// Get Address using Lat and Long
		try {
			addressName = Util.getGPSAddress(instance, latitude, longitude);
			if(addressName.trim().equalsIgnoreCase("")){
				addressName = getString(R.string.can_not_get_gps);
				myLocation = addressName;
			}else{
				nearbyText = addressName;
				myLocation = addressName;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.buttonSearch:
				// Get keyWord Value
				String temp = "";
				if(!keyWord.equalsIgnoreCase("Dining,Pubs,Shopping")) {
					if(keyWord.contains("Dining")) {
						temp += "Dining,";
					}
					if(keyWord.contains("Pubs")) {
						temp += "Pubs,";
					}
					System.out.println(keyWord);
					if(keyWord.contains("Shopping")) {
						temp += "Shopping";
					}
					try {
						if(temp.charAt(temp.length()-1) == ',') {
							temp = temp.substring(0, temp.length()-1);
						}
					}
					catch(Exception e) {
						e.printStackTrace();
					}				
					keyWord = temp;
				}
				if(!keyWord.equalsIgnoreCase("")) {
					if(nearbyET.getText().toString().length() == 0) {
						MerchantListingScreen.URL = Constants.NEARBY_LOCATION + keyWord +
													"&latitude=" + latitude +
													"&longitude=" + longitude +
													"&pageNum=";
					}
					else {
						List<Address> addressLatLong = Util.getLatLongOfAddress(instance, nearbyET.getText().toString() + ", Singapore");
						
						if(addressLatLong != null) {
							if((addressLatLong.get(0).getLatitude() <= 1.5 && addressLatLong.get(0).getLatitude() >= 1.1) && 
							   (addressLatLong.get(0).getLongitude() <= 104.0 && addressLatLong.get(0).getLongitude() >= 103.6)) {
								MerchantListingScreen.URL = Constants.NEARBY_LOCATION + keyWord +
													"&latitude=" + addressLatLong.get(0).getLatitude() +
													"&longitude=" + addressLatLong.get(0).getLongitude() +
													"&pageNum=";
							}
							else {
								String address = nearbyET.getText().toString().replaceAll(" ", "%20");
								MerchantListingScreen.URL = Constants.NEARBY_LOCATION + keyWord +
													"&address=" + address + "&pageNum=";
							}
						}
						else {
							String address = nearbyET.getText().toString().replaceAll(" ", "%20");
							MerchantListingScreen.URL = Constants.NEARBY_LOCATION + keyWord +
														"&address=" + address + "&pageNum=";
						}
					}
					
					SharedPreferences shared = getSharedPreferences(Constants.DEFAUL_SHARE_DATA, 0);
					SharedPreferences.Editor edit = shared.edit();
					edit.putString("name", getString(R.string.nearby));
					edit.commit();
					
					Intent shopping = new Intent(instance, MerchantListingScreen.class);
					startActivityForResult(shopping,0);
				}
				else {
					Util.showAlert(instance, "f.y.i. Singapore", "Please select at least one category.", "OK", false);
				}
				
				break;
			case R.id.buttonChange:
				nearbyText = "";
				settingLayout();
				break;
			case R.id.buttonCancel:
				nearbyET.setText("");
				getAddress();
				settingLayout();
				break;
			case R.id.checkboxdining:
				if(!isDining && cbDining.isChecked()) {
					keyWord += "Dining";
					isDining = true;
				}
				else {
					keyWord = keyWord.replaceAll("Dining", "");
					isDining = false;
				}
				break;
			case R.id.checkboxpubs:
				if(!isPubs && cbPubs.isChecked()) {
					keyWord += "Pubs";
					isPubs = true;
				}
				else {
					keyWord = keyWord.replaceAll("Pubs", "");
					isPubs = false;
				}
				break;
			case R.id.checkboxshopping:
				if(!isShopping && cbShopping.isChecked()) {
					keyWord += "Shopping";
					isShopping = true;
				}
				else {
					keyWord = keyWord.replaceAll("Shopping", "");
					isShopping = false;
				}
				break;
		}
	}
	
	@Override
	protected void onResume() {
		try {
			if(MainCitiGuideScreen.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				latitude = MainCitiGuideScreen.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
				longitude = MainCitiGuideScreen.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
			}
			else {
				latitude = MainCitiGuideScreen.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
				longitude = MainCitiGuideScreen.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			if((latitude == Constants.SINGAPORE_LATITUDE && longitude == Constants.SINGAPORE_LONGITUDE) ||
			   (latitude == 0.0 && longitude == 0.0)) {
				double[] latLong = Util.queryLatLong(instance);
				latitude = latLong[0];
				longitude = latLong[1];
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		MainCitiGuideScreen.locationManager.removeUpdates(MainCitiGuideScreen.locationListener);
		super.onPause();
	}
}

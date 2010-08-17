package com.cellcity.citiguide.screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.cellcity.citiguide.map.GPSLocationListener;
import com.cellcity.citiguide.util.Constants;

public  class SearchScreen extends CitiGuideActivity implements OnClickListener {
	public static SearchScreen instance;
	
	private EditText searchText;
	
	private LocationManager locationManager;
	private GPSLocationListener locationListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.search);
		
		if(instance != null)
			instance.finish();
		instance = this;
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationListener = new GPSLocationListener();
		
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 300, locationListener);
			lat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
			longi = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
		}
		else {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 300, locationListener);
			lat = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
			longi = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
		}
		
		initActivity(instance, getText(R.string.search).toString());
		init();
	}
	
	public void init(){
		
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.widget35);
		linearLayout.setVisibility(LinearLayout.GONE);
		
		searchText = (EditText)findViewById(R.id.searchEText);
		
		// force show keyboard
		//Util.showKeyboard(instance);
		
		// == init button ==
		Button bCancel = (Button)findViewById(R.id.buttonCancel);
		bCancel.setOnClickListener(this);
		Button bSearch = (Button)findViewById(R.id.buttonSearch);
		bSearch.setOnClickListener(this);
		
		Button footerSearch = (Button)findViewById(R.id.search_Button);
		footerSearch.setOnClickListener(new MenuListener());
		Button footerNearby = (Button)findViewById(R.id.nearbyButton);
		footerNearby.setOnClickListener(new MenuListener());
		Button footerShare = (Button)findViewById(R.id.shareButton);
		footerShare.setOnClickListener(new MenuListener());
		Button footerMap = (Button)findViewById(R.id.map_Button);
		footerMap.setOnClickListener(new MenuListener());
		Button homeButton = (Button)findViewById(R.id.homeButton);
		homeButton.setOnClickListener(new MenuListener());
		
		HorizontalScrollView scrollView = (HorizontalScrollView)findViewById(R.id.scrollView);
		scrollView.setVisibility(HorizontalScrollView.GONE);
	}

	@Override
	public void onClick(View v) {
		// close soft keyboard 
		disableKeyboard();
		switch(v.getId()){
		case R.id.buttonCancel:
			instance.finish();
			break;
		case R.id.buttonSearch:
			SharedPreferences preferences = getSharedPreferences(Constants.DEFAUL_SHARE_DATA, 0);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("name", getString(R.string.search));
			
			String keyword = searchText.getText().toString();
			keyword = keyword.replaceAll(" ", "%20");
			
			MerchantListingScreen.querySearch = keyword;
			Intent intent = new Intent(instance, MerchantListingScreen.class);
			startActivityForResult(intent, 0);
			if (editor.commit()) {
				setResult(RESULT_OK);
			}
			break;
		}
	}
	
	public void disableKeyboard(){
		EditText editText = (EditText)findViewById(R.id.searchEText);
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
	
	@Override
	protected void onResume() {
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 300, locationListener);
		}
		else {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 300, locationListener);
		}
		ARScreen.isMerchantList = false;
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		locationManager.removeUpdates(locationListener);
		super.onPause();
	}
}
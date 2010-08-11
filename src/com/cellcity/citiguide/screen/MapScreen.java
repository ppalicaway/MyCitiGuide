package com.cellcity.citiguide.screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cellcity.citiguide.map.GPSLocationListener;
import com.cellcity.citiguide.screen.CitiGuideListActivity.MenuListener;
import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;
import com.google.android.maps.MapActivity;

public class MapScreen extends MapActivity {
	public static MapScreen instance;
    private LocationManager lm;
    private GPSLocationListener locationListener;
    
	//public static int page = 1;
	//public static int totalPage = 1;
	//private int startItem, endItem, totalItems;

	// share data
	//private String id;
	//private String sId;
	private String headerTxt;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setTheme(R.style.Theme_Translucent);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.map);
		
//		if(instance != null)
//			instance.finish();
		instance = this;
		init();
	}
	
	private void init(){
		//ImageView im = (ImageView)findViewById(R.id.headerIView);
		//if(Util.getScreenWidth(instance) > Util.getScreenHeight(instance))
		//	im.setImageResource(R.drawable.head_main_page_land);
		//else
		//	im.setImageResource(R.drawable.head_main_page);
		
		Button map = (Button)findViewById(R.id.mapButton);
		Button ar = (Button)findViewById(R.id.arButton);
		map.setVisibility(Button.GONE);
		ar.setVisibility(Button.GONE);
		
		Button homeButton = (Button)findViewById(R.id.homeButton);
		homeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Util.closeAllInstance();			
				Intent main = new Intent(instance, MainCitiGuideScreen.class);
				startActivity(main);
			}
		});
		
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.widget35);
		linearLayout.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		
		//########## [ read share data ] ###########
		SharedPreferences preferences = getSharedPreferences(Constants.DEFAUL_SHARE_DATA, 0);
		headerTxt = preferences.getString("name", "");
		//##########################################

		// ---use the LocationManager class to obtain GPS locations---
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if(locationListener == null)
			locationListener = new GPSLocationListener();
		
		if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, locationListener);
		}
		else {
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 100, locationListener);
		}
	}

	/**
	 * Must let Google know that a route will not be displayed
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	
	/**
	 * =================================
	 * Create menu
	 * =================================
	 */
	// new menu
	//private Menu mMenu;
	//public boolean onCreateOptionsMenu(Menu menu) {
	//	this.mMenu = menu;
	//	MenuInflater inflater = getMenuInflater();
	//	inflater.inflate(R.menu.main, menu);
	//	return true;
	//}
	//public boolean	onPrepareOptionsMenu(Menu menu){
		
	//	return super.onPrepareOptionsMenu(menu);
		
	//}
	
	//public boolean onMenuItemSelected(int featureId, MenuItem item) {
	//	switch (item.getItemId()) {
	//	case R.id.menu_search:
	//		this.finish();
	//		Intent search = new Intent(this, SearchScreen.class);
	//		startActivity(search);
	//		return true;
//		case R.id.menu_nearby:
//			act.finish();
//			Intent nearby = new Intent(act, NearbyScreen.class);
//			startActivity(nearby);
//			return true;
	//	case R.id.menu_share:
	//		new AlertDialog.Builder(this)
//            .setIcon(R.drawable.alert_dialog_icon)
    //        .setTitle(R.string.sharTitle)
    //       .setPositiveButton(R.string.sms, new DialogInterface.OnClickListener() {
    //            public void onClick(DialogInterface dialog, int whichButton) {
	//				Util.sendMessage(instance, "", "");
    //           }
    //        })
    //        .setNegativeButton(R.string.email, new DialogInterface.OnClickListener() {
    //            public void onClick(DialogInterface dialog, int whichButton) {
	//				Util.sendEmail(instance, "", "", "", "");
    //            }
    //        })
    //        .create().show();
	//		return true;
	//	case R.id.menu_map:
	//		Controller.displayMapScreen(this);
	//		break;
	//	}
		
	//	return super.onMenuItemSelected(featureId, item);
	//}
	
	@Override
	protected void onResume() {
		if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, locationListener);
		}
		else {
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 100, locationListener);
		}
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		lm.removeUpdates(locationListener);
		super.onPause();
	}
}

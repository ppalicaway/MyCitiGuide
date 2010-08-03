package com.cellcity.citiguide.screen;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.cellcity.citiguide.ar.ARLayout;
import com.cellcity.citiguide.ar.CustomCameraView;
import com.cellcity.citiguide.ar.FourSqareVenue;
import com.cellcity.citiguide.ar.GetJSON;
import com.cellcity.citiguide.ar.ReturnRes;
import com.cellcity.citiguide.ar.SeekBarLayout;
import com.cellcity.citiguide.info.MerchantInfo1;
import com.cellcity.citiguide.util.Util;

public class ARScreen extends CitiGuideActivity {

	public static ARScreen instance;
	/** Called when the activity is first created. */
	private CustomCameraView cv;
	public static volatile Context ctx;
	private static ARLayout ar;
	private SeekBarLayout seekLayout;
    private LocationManager curLocation;
    public static Location myLocation;
    //private HashMap<String, String> merchantHash;
    private Location location;
    public static String cats = "1,2,3";
    
    public static ArrayList<MerchantInfo1> merchantList = null;
	public static ProgressDialog progressDialog = null;
	//private Runnable initR;
	public static int page = 1;
	public static float radius = (float) 1;
	public static String query;
	public static boolean isMerchantList;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		instance = this;
		initActivity(this, "");
		
		ctx = this.getApplicationContext();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		init();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		init();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void init(){
		
    	ar = new ARLayout(getApplicationContext());
        cv = new CustomCameraView(this.getApplicationContext());

        //cl = new CompassListener(this.getApplicationContext());
        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
     	int width = d.getWidth();
     	int height = d.getHeight(); 
        ar.screenHeight = height;
        ar.screenWidth = width;
        FrameLayout rl = new FrameLayout(getApplicationContext());
        rl.addView(cv,width, height);
        ar.debug = true;
        rl.addView(ar, width, height);
        seekLayout = new SeekBarLayout(this);
        rl.addView(seekLayout, width, height);
        
        setContentView(rl);
        curLocation = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        if(curLocation.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        	myLocation = curLocation.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            curLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, gpsListener);
        }
        else {
        	myLocation = curLocation.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            curLocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, gpsListener);
        }        
        
        if(myLocation == null){
			Util.showAlert(instance, getString(R.string.menu_ar),
					getString(R.string.can_not_get_location),
					getString(R.string.ok), true);
			return;
        }
		System.out.println("################# onLocationChanged 0 : lat = "+myLocation.getLatitude()); 
		System.out.println("################# onLocationChanged 0 : lng = "+myLocation.getLongitude()); 
		
		ARLayout.curLocation = myLocation;
		ARLayout.locationChanged = true;
		
		if(isMerchantList) {
			merchantList = new ArrayList<MerchantInfo1>();
			merchantList = ListingMerchantScreen.merchantLtd;
			
			for(int i = 0; i < merchantList.size(); i++) {
				System.out.println("Merchant is: " + merchantList.get(i).getMerchantName());
			}
			Thread t = new Thread(new ReturnRes());
			t.start();
		}
		else {
			// display progress dialog
			try {
				progressDialog = ProgressDialog.show(this, "", getString(R.string.retrieving_data), true);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			Thread thread = new Thread(null, new GetJSON(), "initR");
			thread.start();
		}
    }
 
	public static void addMerchant(){
		ar.clearARViews();
		Vector<FourSqareVenue> v = new Vector<FourSqareVenue>();
		FourSqareVenue curVen = null;
		float degree = 0.0f;
		float beringTo = 0.0f;
		float diff = 0.0f;
		
		float height = 0;
		for(int i = 0; i < merchantList.size(); i++){
			MerchantInfo1 mInfo = merchantList.get(i);
			if((mInfo.getLatitude() != null && !mInfo.getLatitude().equalsIgnoreCase("")) && 
			   (mInfo.getLongitude() != null && !mInfo.getLongitude().equalsIgnoreCase(""))) {
				curVen = new FourSqareVenue(instance);
				curVen.merchantInfo = mInfo;
			
				curVen.location = new Location("FourSqareApi");
				curVen.location.setLatitude(Double.parseDouble(mInfo.getLatitude())); // 35.683333, 139.766667
				curVen.location.setLongitude(Double.parseDouble(mInfo.getLongitude()));
				curVen.distance = myLocation.distanceTo(curVen.location);
				curVen.inclination = -8 + height;
				height += (4.3f);
				v.add(curVen);
			}
		}
		
		if (v != null && v.size() > 0) {
			Enumeration<FourSqareVenue> e = v.elements();
			while (e.hasMoreElements()) {
				FourSqareVenue fq = (FourSqareVenue) e.nextElement();
				System.out.println("Got Venue ####################### : " + fq.merchantInfo.getMerchantName());
				ar.addARView(fq);
			}
		}
	}
	
	public void onStart() {
		super.onStart();
	}
    
	public void onDestroy() {
		super.onDestroy();
		ARScreen.cats="1,2,3";
		ARScreen.radius = (float) 1;
		cv.closeCamera();
		ar.close();
		// cl.close();
		// cv.closeCamera();
	}
	
	/**
	 * ================================================================
	 */
	private LocationListener gpsListener = new LocationListener() {

		public void onLocationChanged(Location location) {
			myLocation = location;
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	
	protected void onResume() {
		if(curLocation.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        	curLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, gpsListener);
        	myLocation = curLocation.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        else {
        	curLocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, gpsListener);
        	myLocation = curLocation.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
		super.onResume();
	};
	
	protected void onPause() {
		curLocation.removeUpdates(gpsListener);
		cv.closeCamera();
		ar.close();
		super.onPause();
	};
}

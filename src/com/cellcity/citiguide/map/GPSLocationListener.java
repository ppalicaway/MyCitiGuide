package com.cellcity.citiguide.map;

import java.text.DecimalFormat;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.cellcity.citiguide.screen.MainCitiGuideScreen;
import com.cellcity.citiguide.screen.R;
import com.cellcity.citiguide.util.Util;
import com.google.android.maps.GeoPoint;

/**
 * ============================================================
 * inner class
 * Get current location from gps.
 * ============================================================
 * @author Administrator
 */
public class GPSLocationListener implements LocationListener {
	private double lat = 0;
	private double lng = 0;
	
	public GPSLocationListener(){
	}

	public void onLocationChanged(Location argLocation) {
		// TODO Auto-generated method stub
		lat = argLocation.getLatitude();
		lng = argLocation.getLongitude();
		
		MainCitiGuideScreen.lat = lat;
		MainCitiGuideScreen.lng = lng;
		
		if(MapLocationViewer.useCurrentGPS){
			System.out.println("onLocationChanged: ###################"); 
			
			DecimalFormat twoDForm = new DecimalFormat("#.####");
			String latStr = Double.valueOf(twoDForm.format(lat)).toString();
			String lngStr = Double.valueOf(twoDForm.format(lng)).toString();
			
			Activity act = (Activity) MainCitiGuideScreen.instance;
			
			MapLocationInfo mLocation = new MapLocationInfo(Util.getGPSAddress(act, lat, lng), "", "Latitude: " + latStr + " Longitude: " + lngStr, lat, lng, R.drawable.pin_violet, null);
			
			//MapLocationInfo mLocation = new MapLocationInfo(MainCitiGuideScreen.instance.getString(R.string.map_text), 
			//		"", "Latitude: " + latStr + " Longitude: " + lngStr, lat, lng, R.drawable.pin_violet, null);
			
			MapLocationViewer.setMapLocation(mLocation, 0, MapLocationViewer.useCurrentGPS);
			
			GeoPoint point = new GeoPoint((int) (lat * 1e6), (int) (lng * 1e6));
			MapLocationViewer.mapView.getController().setCenter(point);
		}
		
		System.out.println("onLocationChanged: lat="+argLocation.getLatitude()); 
		System.out.println("onLocationChanged: lng="+argLocation.getLongitude()); 
	}
	
	 @Override 
     public void onProviderDisabled(String provider) {
		 System.out.println("onProviderDisabled: " +provider);
     } 

     @Override 
     public void onProviderEnabled(String provider) { 
    	 System.out.println("onProviderEnabled: " +provider);
     } 

     @Override 
     public void onStatusChanged(String provider, int status, 
               Bundle extras) { 
    	 System.out.println("onStatusChanged: " +provider + " " + status);
     } 
}
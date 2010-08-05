package com.cellcity.citiguide.adapter;


import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.cellcity.citiguide.info.MerchantInfo1;
import com.cellcity.citiguide.info.MerchantInfo2;
import com.cellcity.citiguide.map.MapLocationInfo;
import com.cellcity.citiguide.map.MapLocationViewer;
import com.cellcity.citiguide.screen.DescriptionScreen;
import com.cellcity.citiguide.screen.ListingMerchantScreen;
import com.cellcity.citiguide.screen.MainCitiGuideScreen;
import com.cellcity.citiguide.screen.MapScreen;
import com.cellcity.citiguide.screen.MerchantListingScreen;
import com.cellcity.citiguide.screen.NewDescriptionScreen;
import com.cellcity.citiguide.screen.R;
import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;

public class Controller {
	public static int getResourceImageTypeId(int typeId){
		int rId = 0;
		switch(typeId){
		 case Constants.TYPE_GOURMET:
		 rId = R.drawable.icon_gourmet_plesure;
		 break;
		 case Constants.TYPE_PROMOTION:
		 rId = R.drawable.icon_promotions;
		 break;
		case Constants.TYPE_SHOPPING:
			 rId = R.drawable.icon_shop;
			 break;
		case Constants.TYPE_BARS:
			 rId = R.drawable.icon_pub_and_bars;
		break;
		case Constants.TYPE_BANK:
			 rId = R.drawable.icon_atm;
	    break;
		case Constants.TYPE_MOVIE:
			 rId = R.drawable.icon_movies;
		break;
		case Constants.TYPE_HOTEL:
			 rId = R.drawable.icon_hotel;
	    break;
			
		case Constants.TYPE_FLIGHT:
			 rId = R.drawable.icon_flights;
        break;
		case Constants.TYPE_WEATHER:
			 rId = R.drawable.icon_weather;
    	break;
		}
		return rId;
	}
	
	public static int getResourcePin(int index){
		int rId = 0;
		switch(index){
		case 1:
			rId = R.drawable.pin_1;
			break;
		case 2:
			rId = R.drawable.pin_2;
			break;
		case 3:
			rId = R.drawable.pin_3;
			break;
		case 4:
			rId = R.drawable.pin_4;
			break;
		case 5:
			rId = R.drawable.pin_5;
			break;
		case 6:
			rId = R.drawable.pin_6;
			break;
		case 7:
			rId = R.drawable.pin_7;
			break;
		case 8:
			rId = R.drawable.pin_8;
			break;
		case 9:
			rId = R.drawable.pin_9;
			break;
		case 10:
			rId = R.drawable.pin_10;
			break;
		}
		return rId;
	}
	public static void displayMapScreen(Context context){
		if(context instanceof MerchantListingScreen){
			MapLocationViewer.setMapLocations(MerchantListingScreen.merchantList, 0, false);
			Intent intent = new Intent(context, MapScreen.class);
			context.startActivity(intent);
		}
		else if(context instanceof NewDescriptionScreen){
			ArrayList<MerchantInfo2> mapList = new ArrayList<MerchantInfo2>();
			mapList.add(NewDescriptionScreen.merchantInfo);
			MapLocationViewer.setMapLocations(mapList, 0, false);
			Intent intent = new Intent(context, MapScreen.class);
			context.startActivity(intent);
		}
		else{
			double lat = MainCitiGuideScreen.lat;//Constants.BANGKOK_LAT;
			double lng = MainCitiGuideScreen.lng;//Constants.BANGKOK_LONG;
			DecimalFormat twoDForm = new DecimalFormat("#.####");
			String latStr = Double.valueOf(twoDForm.format(lat)).toString();
			String lngStr = Double.valueOf(twoDForm.format(lng)).toString();
			
			Activity act = (Activity) context;
			
			MapLocationInfo mLocation = new MapLocationInfo(Util.getGPSAddress(act, lat, lng), "Latitude: " + latStr + " Longitude: " + lngStr, lat, lng, R.drawable.pin_violet, null);
			mLocation.setCurrentPost(true);
					
			MapLocationViewer.setMapLocation(mLocation, 0, true);
			
			Intent map = new Intent(context, MapScreen.class);
			context.startActivity(map);
		}
	}
}

package com.cellcity.citiguide.ar;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cellcity.citiguide.info.MerchantInfo1;
import com.cellcity.citiguide.info.MerchantInfo2;
import com.cellcity.citiguide.screen.ARScreen;
import com.cellcity.citiguide.screen.MainCitiGuideScreen;
import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;

public class GetJSON implements Runnable {
	
	private HashMap<String, String> merchantHash;
	private ArrayList<MerchantInfo2> merchants;
	
	private double lat, lng;
	
	@Override
	public void run() {
		String result = "";
		
		result = Util.getHttpData(Constants.RESTAURANT_LOCATION_AR + MainCitiGuideScreen.lat +
			      "&longitude=" + MainCitiGuideScreen.lng);
		result = Util.toJSONString(result);
		
		ARScreen.merchantList = new ArrayList<MerchantInfo2>();
		merchantHash = new HashMap<String, String>();
		
		try {
			JSONObject jsonObject1 = new JSONObject(result);
			JSONArray nameArray = jsonObject1.getJSONArray("data");
			
			try {
				for(int i = 0; i < nameArray.length(); i++) {
					JSONObject jsonObject2 = nameArray.getJSONObject(i);
					
					int id = 0;
					String image = "";
					String restaurantName = "";
					String address = "";
					float rating = 0;
					int reviews = 0;
					double latitude = 0;
					double longitude = 0;
					String distance = "";
					
					id = Integer.parseInt(jsonObject2.getString("ID"));
					image = jsonObject2.getString("Image");
					restaurantName = jsonObject2.getString("RestaurantName");
					address = jsonObject2.getString("Address");
					rating = Float.parseFloat(jsonObject2.getString("Rating"));
					reviews = Integer.parseInt(jsonObject2.getString("Reviews"));
					latitude = Double.parseDouble(jsonObject2.getString("Latitude"));
					longitude = Double.parseDouble(jsonObject2.getString("Longitude"));
					distance = jsonObject2.getString("Distance");
					
					MerchantInfo2 mInfo = new MerchantInfo2(id, image, restaurantName, address, rating, reviews, latitude, longitude);
					mInfo.setDistance(distance);
					
					if(!merchantHash.containsKey(id)) {
						merchantHash.put(Integer.toString(id), Integer.toString(id));
						ARScreen.merchantList.add(mInfo);
					}
				}
				
				Thread t = new Thread(new ReturnRes());
				t.start();
			}
			catch(Exception e) {
				try {
					if (ARScreen.progressDialog.isShowing())
						ARScreen.progressDialog.dismiss();
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		catch(Exception e) {
			try {
				if (ARScreen.progressDialog.isShowing()) {
					ARScreen.progressDialog.dismiss();
				}					
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}
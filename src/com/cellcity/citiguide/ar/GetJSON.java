package com.cellcity.citiguide.ar;

import java.util.ArrayList;
import java.util.HashMap;

import com.cellcity.citiguide.info.MerchantInfo1;
import com.cellcity.citiguide.screen.ARScreen;
import com.cellcity.citiguide.screen.MainCitiGuideScreen;
import com.cellcity.citiguide.screen.R;
import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;

public class GetJSON implements Runnable {
	
	private HashMap<String, String> merchantHash;
	private ArrayList<MerchantInfo1> merchants;
	
	private double lat, lng;
	
	@Override
	public void run() {
		try {
			// read data
			//String result = Util.getHttpData(Constants.SINGAPORE_ARSEARCH + ARScreen.cats + 
			//		 "&r=" + ARScreen.radius +
			//		 "&startRow=" + ((ARScreen.page-1)*Constants.AR_ITEMS_PER_PAGE) +
			//		 "&maxRow=" + Constants.AR_ITEMS_PER_PAGE +
			//		 "&p="+ ARScreen.myLocation.getLatitude() + "," +
			//		 ARScreen.myLocation.getLongitude() + "&f=pjson");
			
			//String query = "&startRow=" + ((ARScreen.page-1)*Constants.ITEMS_PER_PAGE) + "&maxRow=" + Constants.ITEMS_PER_PAGE + "&p="+ ARScreen.myLocation.getLatitude() + "," + ARScreen.myLocation.getLongitude() + "&f=pjson";
			
			//String result = "http://www.dc2go.net/api/go/getList.php?result=20&city=Singapore&state=&country=Singapore&start=0&category=3";
			
			//result = Util.toJSONString(result);
						
			//ARScreen.merchantList = new ArrayList<MerchantInfo>();
			//merchantHash = new HashMap<String, String>();

			// JSONObject Parsing
			//JSONObject json = new JSONObject(result);
            //JSONArray nameArray=json.names();
            //JSONArray valArray=json.toJSONArray(nameArray);
			//for (int i = 0; i < valArray.length(); i++) {
			//	if(nameArray.getString(i).equalsIgnoreCase("items")){
			//		try {
			//			JSONArray jArray1 = valArray.getJSONArray(i);
			//			for (int j = 0; j < jArray1.length(); j++) {
			//				JSONObject jArray2 = jArray1.getJSONObject(j);
			//				
			//				// content info
			//				String cId = "1";
			//				String name = "";
			//				String address = "";
			//				String desc = "";
			//				String phone = "";
			//				String price = "";
			//				String email = "";
			//				String website = "";
			//				String op = "";
			//				String lat = "";
			//				String lng = "";
			//				String distance = "";
			//				String thumbnail = "";
			//				String category = "";
			//				String bdayTreat = "";
			//				String tnc = "";
			//				
			//				cId = jArray2.getString("id");
			//				//name = jArray2.getString("name");
			//				name = jArray2.getString("title");
			//				address = jArray2.getString("address");
			//				//desc = jArray2.getString("offer");
			//				desc = jArray2.getString("desc");
			//				phone = jArray2.getString("phone");
			//				//price = jArray2.getString("price");
			//				//email = jArray2.getString("email");
			//				//website = jArray2.getString("website");
			//				website = jArray2.getString("url");
			//				//op = jArray2.getString("op");
			//				//lat = jArray2.getString("lat");
			//				//lng = jArray2.getString("longi");
			//				lat = jArray2.getString("latitude");
			//				lng = jArray2.getString("longitude");
			//				//distance = jArray2.getString("distance");
			//				//thumbnail = jArray2.getString("thumbnail");
			//				//category = jArray2.getString("category");
			//				//bdayTreat = jArray2.getString("birthday_treat");
			//				//tnc = jArray2.getString("tnc");
			//				lat = lat.replaceAll(",", "");
			//				lng = lng.replaceAll(",", "");
			//				lat = lat.replaceAll("°", "");
			//				lng = lng.replaceAll("°", "");
			//				lat = lat.replaceAll("'", "");
			//				lng = lng.replaceAll("'", "");
			//				
			//				try{
			//					lat = lat.substring(0, lat.indexOf(".", lat.indexOf(".")+1));
			//					lng = lng.substring(0, lng.indexOf(".", lng.indexOf(".")+1));
			//				}catch(Exception ex){
			//					
			//				}
			//				
			//				if(lat.indexOf(".") <= 3 && lng.indexOf(".") <= 3){
			//					System.out.println(name + " " + lat + " vs " + lng);
			//					int count = 0;
			//					while(address.startsWith(" ") || address.startsWith("\n")){
			//						address = address.substring(1);
			//						count++;
			//					}
			//					address = address.substring(count);
			//					
			//					
			//					//MerchantInfo cInfo = new MerchantInfo(Integer.parseInt(cId), name, address, desc, phone, price, email, website, op, lat, lng);
			//					//MerchantInfo cInfo = new MerchantInfo(Integer.parseInt(cId), name, address, desc, phone, price, email, website, op, lat, lng);
			//					//cInfo.setIndex((j+1));
			//					//cInfo.setThumbnail(thumbnail);
			//					//cInfo.setCategory(category);
			//					//cInfo.setBirthday_treat(bdayTreat);
			//					//cInfo.setTnc(tnc);
			//					//cInfo.setDistance(Float.parseFloat(distance));
			//					
			//					if(!merchantHash.containsKey(cId)){
			//						merchantHash.put(cId, cId);
			//						//ARScreen.merchantList.add(cInfo);
			//					}
			//					
			//				}
			//			}
			//			
			//		} catch (Exception e) {
			//			// TODO: handle exception
			//			e.printStackTrace();
			//		}
			//	}
			//}
			
			lat = MainCitiGuideScreen.lat;
			lng = MainCitiGuideScreen.lng;
			
			merchants = new ArrayList<MerchantInfo1>();
			ARScreen.merchantList = new ArrayList<MerchantInfo1>();
			String URL = Constants.URL_ARSEARCH + lat + 
						 "&current_long=" + lng + 
						 "&radius=" + ARScreen.radius;
			//String URL = "http://www.citiworldprivileges.com/mobile/?country=Singapore&city=Singapore&offer_type=Dining&keywords=bar&sort_by=distance&sort_order=desc&num_offers=10&current_lat=1.3369238666666667&current_long=103.84865956666665&radius=10";
			merchants = Util.getMerchants(URL);
			
			if(merchants != null) {
				for(int i=0; i < ARScreen.merchantList.size(); i++) {
					//ARScreen.merchantList.get(i).setDistance(Util.convertLatLongToDist(lat, lng, Double.parseDouble(ARScreen.merchantList.get(i).getLatitude()), Double.parseDouble(ARScreen.merchantList.get(i).getLongitude())));
					ARScreen.merchantList.get(i).setDistance(Util.getDistance(Double.parseDouble(ARScreen.merchantList.get(i).getLatitude()), Double.parseDouble(ARScreen.merchantList.get(i).getLongitude()), lat, lng));
				}
			
				sortByDistance();
			
				for(int i = 0; i < 15; i++) {
					ARScreen.merchantList.add(merchants.get(i));
					System.out.println(merchants.get(i).getMerchantName());
				}
				Thread t = new Thread(new ReturnRes());
				t.start();
			}
			else {
				Util.showAlert(ARScreen.instance, "", 
						ARScreen.instance.getResources().getString(R.string.no_item_found),
						ARScreen.instance.getResources().getString(R.string.ok), true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sortByDistance() {
		ArrayList<MerchantInfo1> temp = ARScreen.merchantList;
		for(int i = 0; i < temp.size(); i++){
			for(int j = i+1; j < temp.size(); j++){
				MerchantInfo1 nextInfo = temp.get(j);
				double nextDistance = temp.get(j).getDistance();		
				
				MerchantInfo1 currInfo = temp.get(i);
				double currentDistance = temp.get(i).getDistance();
				
				if(currentDistance > nextDistance){
					nextInfo.setIndex(i+1);
					currInfo.setIndex(j+1);
					ARScreen.merchantList.set(i, nextInfo);
					ARScreen.merchantList.set(j, currInfo);
				}
			}
		}
		temp = null;
	}	
}
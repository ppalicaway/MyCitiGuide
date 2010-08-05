package com.cellcity.citiguide.screen;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cellcity.citiguide.adapter.Controller;
import com.cellcity.citiguide.info.MerchantDetails;
import com.cellcity.citiguide.info.MerchantInfo2;
import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;

public class NewDescriptionScreen extends CitiGuideActivity implements OnClickListener{

	public static NewDescriptionScreen instance;
	public static MerchantInfo2 merchantInfo;
	public static MerchantDetails merchantDetails;
	public static int catID;
	private Runnable queryThread;
	private ProgressDialog progressDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newdescription);
		
		instance = this;

		initActivity(this, "");
		init();
	}

	private void init() {
		try {
			progressDialog = ProgressDialog.show(this, "", "Retrieving data...", true);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		queryThread = new Runnable() {
			
			@Override
			public void run() {
				getData();
				runOnUiThread(populateData);
			}
		};
		
		Button coupon = (Button)findViewById(R.id.coupon);
		coupon.setOnClickListener(this);
		coupon.setVisibility(Button.GONE);
		Button mapButt = (Button)findViewById(R.id.mapButt);
		mapButt.setOnClickListener(this);
		Button navigate = (Button)findViewById(R.id.navigate);
		navigate.setOnClickListener(this);
		
		Thread thread = new Thread(null, queryThread, "queryThread");
		thread.start();		
		
		Button map = (Button)findViewById(R.id.mapButton);
		Button ar = (Button)findViewById(R.id.arButton);
		map.setVisibility(Button.GONE);
		ar.setVisibility(Button.GONE);
		
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.widget35);
		linearLayout.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

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
	}
	
	private Runnable populateData = new Runnable() {
		
		@Override
		public void run() {
			TextView titleTView = (TextView)findViewById(R.id.templateTopTitleTView);
			titleTView.setText(merchantDetails.getTitle());
			
			TextView address = (TextView)findViewById(R.id.address);
			address.setText(merchantDetails.getAddress());
			
			TextView offerDesc = (TextView)findViewById(R.id.offerDesc);
			offerDesc.setText(merchantDetails.getOffer());
			
			String phone = merchantDetails.getPhone();
			if(phone != null || !phone.equalsIgnoreCase("")) {
				phone = phone.replaceAll(" ", "");
				phone = phone.replaceAll("-", "");
			}
			final String phoneNum = phone;
			
			LinearLayout telDesc = (LinearLayout)findViewById(R.id.telDesc);
			telDesc.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Util.makeCall(instance, phoneNum);
				}
			});
			
			TextView telNumber = (TextView)findViewById(R.id.telNumber);
			telNumber.setText("Tel: " + phone);
			
			String message = "Merchant Name:\n";
			message += merchantDetails.getTitle();
			message += "\nAddress:\n";
			message += merchantDetails.getAddress();
			message += "\nOffer:\n";
			message += merchantDetails.getOffer();
			
			String subject = merchantDetails.getTitle();
			
			CitiGuideListActivity.message = message;
			CitiGuideActivity.message = message;
			CitiGuideListActivity.subject = subject;
			CitiGuideActivity.subject = subject;
		}
	};

	protected void getData() {
		String result = "";
		result = Util.getHttpData(Constants.RESTAURANT_DETAIL + catID);
		
		if(result == null || result.equalsIgnoreCase("408") || result.equalsIgnoreCase("404")) {
			//TODO
		}
		else {
			result = Util.toJSONString(result);
			merchantDetails = new MerchantDetails();
			
			try {
				JSONObject jsonObject1 = new JSONObject(result);
				JSONArray names = jsonObject1.names();
				JSONArray valArray = jsonObject1.toJSONArray(names);
				JSONObject jsonObject2 = valArray.getJSONObject(0);
				
				int id = Integer.parseInt(jsonObject2.getString("id"));
				String image = jsonObject2.getString("img");
				String thumbnail = jsonObject2.getString("thumb");
				String title = jsonObject2.getString("title");
				String type = jsonObject2.getString("type");
				double rating = Double.parseDouble(jsonObject2.getString("rating"));
				int reviews = Integer.parseInt(jsonObject2.getString("reviews"));
				String address = jsonObject2.getString("address");
				String phone = jsonObject2.getString("phone");
				double latitude = Double.parseDouble(jsonObject2.getString("latitude"));
				double longitude = Double.parseDouble(jsonObject2.getString("longitude"));
				String description = jsonObject2.getString("description");
				
				merchantDetails.setId(id);
				merchantDetails.setImage(image);
				merchantDetails.setThumbnail(thumbnail);
				merchantDetails.setTitle(title);
				merchantDetails.setType(type);
				merchantDetails.setRating(rating);
				merchantDetails.setReviews(reviews);
				merchantDetails.setAddress(address);
				merchantDetails.setPhone(phone);
				merchantDetails.setLatitude(latitude);
				merchantInfo.setLatitude(latitude);
				merchantDetails.setLongitude(longitude);
				merchantInfo.setLongitude(longitude);
				merchantDetails.setDescription(description);
				
				JSONArray bankInfo = jsonObject2.getJSONArray("offers");
				
				for(int i = 0; i < bankInfo.length(); i++) {
					JSONObject jsonObject3 = bankInfo.getJSONObject(i);
					String bankname = jsonObject3.getString("bank");
					
					if(bankname.equalsIgnoreCase("Citibank")) {
						String offer = jsonObject3.getString("offer");
						merchantDetails.setOffer(offer);
					}
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.coupon:
				break;
			case R.id.mapButt:
				Controller.displayMapScreen(instance);
				break;
			case R.id.navigate:
				Intent navigation = new Intent(Intent.ACTION_VIEW, 
						Uri.parse("http://maps.google.com/maps?saddr=" + MainCitiGuideScreen.lat + "," + MainCitiGuideScreen.lng +
								  "&daddr=" + merchantDetails.getLatitude() + "," + merchantDetails.getLongitude())); 
            startActivity(navigation);
			break;
		}
	}
}
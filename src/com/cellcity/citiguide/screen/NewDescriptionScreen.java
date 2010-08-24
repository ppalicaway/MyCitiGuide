package com.cellcity.citiguide.screen;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
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
			progressDialog = ProgressDialog.show(this, "", "Retrieving data...", true, true, new DialogInterface.OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					instance.finish();
				}
			});
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
		
		TextView titleView = (TextView)findViewById(R.id.templateTopTitleTView);
		titleView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		titleView.setSingleLine(false);
		
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
		
		@SuppressWarnings("null")
		@Override
		public void run() {
			TextView titleTView = (TextView)findViewById(R.id.templateTopTitleTView);
			titleTView.setText(merchantDetails.getTitle());
			
			TextView address = (TextView)findViewById(R.id.address);
			address.setText(merchantDetails.getAddress());
			
			TextView offerDesc = (TextView)findViewById(R.id.offerDesc);
			offerDesc.setText(merchantDetails.getOffer());
			
			TextView tncDesc = (TextView)findViewById(R.id.tncDesc);
			tncDesc.setText(merchantDetails.getTnc());
			
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
			
			String subject = "Recommended Citibank Credit Card exclusive privilege.";
			
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
			Util.showAlert(instance, "f.y.i Singapore", "Please make sure Internet connection is available.", "OK", true);
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
				String title = jsonObject2.getString("outletname");
				String category = jsonObject2.getString("category");
				String subCategory = jsonObject2.getString("subcategory");
				String description = jsonObject2.getString("description");
				String thumbnail = jsonObject2.getString("thumb");
				String image = jsonObject2.getString("img");
				String address = jsonObject2.getString("address");
				String phone = jsonObject2.getString("phone");
				double latitude = Double.parseDouble(jsonObject2.getString("latitude"));
				double longitude = Double.parseDouble(jsonObject2.getString("longitude"));
				String offer = jsonObject2.getString("offer").replaceAll("\r", "");
				String tnc = jsonObject2.getString("tnc");
				
				merchantDetails.setId(id);
				merchantDetails.setTitle(title);
				merchantDetails.setCategory(category);
				merchantDetails.setSubCategory(subCategory);
				merchantDetails.setDescription(description);
				merchantDetails.setThumbnail(thumbnail);
				merchantDetails.setImage(image);
				merchantDetails.setAddress(address);
				merchantDetails.setPhone(phone);
				merchantDetails.setLatitude(latitude);
				merchantInfo.setLatitude(latitude);
				merchantDetails.setLongitude(longitude);
				merchantInfo.setLongitude(longitude);
				merchantDetails.setOffer(offer);
				merchantDetails.setTnc(tnc);
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(progressDialog.isShowing()) {
				instance.finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
package com.cellcity.citiguide.screen;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cellcity.citiguide.adapter.Controller;
import com.cellcity.citiguide.info.MerchantInfo1;
import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;


public class ListingMerchantScreen extends CitiGuideListActivity {
	public static ListingMerchantScreen instance;
	public static ArrayList<MerchantInfo1> merchantList = null;
	public static ArrayList<MerchantInfo1> merchantLtd = null;
	private ProgressDialog progressDialog = null;
	private Runnable initR;
	
	public static Bitmap bmp;

	private Bitmap bitmap;
	private int iconW, iconH;
	private ListViewAdapter m_adapter;

	public static int page = 1;
	public static int totalPage = 1;
	private int startItem, endItem, totalItems;

	// share data
	private String id;
	private String sId;
	private String headerTxt;
	public static MerchantInfo1 merchantInfo;
	public static String querySearch = null;
	
	private String URL = "";
	
	private double lat;
	private double lng;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Translucent);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listingmerchant);

		instance = this;

		initActivity(instance, "");
		
		ARScreen.isMerchantList = true;

		page = 1;

		init();
	}

	public void init() {
		settingLayout();

		// display progress dialog
		progressDialog = ProgressDialog.show(this, "", getString(R.string.retrieving_data), true);

		// start thread
		initR = new Runnable() {
			@Override
			public void run() {
				getJSON();
				runOnUiThread(returnRes);
			}
		};
		Thread thread = new Thread(null, initR, "initR");
		thread.start();
	}

	private void settingLayout() {
		SharedPreferences preferences = getSharedPreferences(Constants.DEFAUL_SHARE_DATA, 0);
		id = preferences.getString("id", "1");
		sId = preferences.getString("sId", "1");
		headerTxt = preferences.getString("name", "");
		
		determineURL(headerTxt);
		
		// ############# [ tab host ] ###############
//		ImageView tabList = (ImageView) findViewById(R.id.tabListIView);
//		tabList.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//		ImageView tabMap = (ImageView) findViewById(R.id.tabMapIView);
//		tabMap.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				SharedPreferences preferences = getSharedPreferences(
//						Constants.DEFAUL_SHARE_DATA, 0);
//				SharedPreferences.Editor editor = preferences.edit();
//				editor.putString("name", headerTxt);
//
////			    MapLocationViewer.setMapLocations(merchantList, 55, true);
////				MapScreen.showTab = true;
////				Intent intent = new Intent(instance, MapScreen.class);
////				startActivityForResult(intent, 0);
//			}
//
//		});
		// ##########################################

		TextView titleView = (TextView) findViewById(R.id.templateTopTitleTView);
		titleView.setText(headerTxt);
		
		Button mapButton = (Button)findViewById(R.id.mapButton);
		mapButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Controller.displayMapScreen(instance);
			}
		});
		
		Button arButton = (Button)findViewById(R.id.arButton);
		arButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(ARScreen.instance != null)
					ARScreen.instance.finish();
				
				Intent ar = new Intent(instance, ARScreen.class);
				startActivity(ar);
			}
		});
		
		mapButton.setVisibility(Button.GONE);
		arButton.setVisibility(Button.GONE);
		
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.widget35);
		linearLayout.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

		//Button footerSearch = (Button)findViewById(R.id.search_Button);
		//footerSearch.setOnClickListener(new MenuListener());
		//Button footerNearby = (Button)findViewById(R.id.nearbyButton);
		//footerNearby.setOnClickListener(new MenuListener());
		//Button footerShare = (Button)findViewById(R.id.shareButton);
		//footerShare.setOnClickListener(new MenuListener());
		//Button footerMap = (Button)findViewById(R.id.map_Button);
		//footerMap.setOnClickListener(new MenuListener());
		Button homeButton = (Button)findViewById(R.id.homeButton);
		homeButton.setOnClickListener(new MenuListener());
		
		//merchantList = new ArrayList<MerchantInfo>();
		merchantList = new ArrayList<MerchantInfo1>();
		merchantLtd = new ArrayList<MerchantInfo1>();
		m_adapter = new ListViewAdapter(instance, R.layout.row_list, merchantList);
		setListAdapter(m_adapter);

		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
		iconW = bitmap.getWidth();
		iconH = bitmap.getHeight();
	}

	private void determineURL(String headerTxt) {
		lat = MainCitiGuideScreen.lat;
		lng = MainCitiGuideScreen.lng;
		
		if(headerTxt.equalsIgnoreCase(getString(R.string.search))) {
			URL = Constants.URL_SEARCH + querySearch +
			      "&sort_by=distance&sort_order=desc&num_offers=" + Constants.ITEMS_PER_PAGE +
			      "&current_lat=" + lat +
			      "&current_long=" + lng +
			      "&radius=10";
		}
		else if(headerTxt.equalsIgnoreCase(getString(R.string.dining))) {
			URL = Constants.URL_DINING + lat + 
				  "&current_long=" + lng + 
				  "&radius=10";
		}
		else if(headerTxt.equalsIgnoreCase(getString(R.string.shopping))) {
			URL = Constants.URL_SHOPPING + lat + 
				  "&current_long=" + lng + 
				  "&radius=10";
		}
		else if(headerTxt.equalsIgnoreCase(getString(R.string.hotels))) {
			URL = Constants.URL_HOTELS + "&current_lat=" +
			  	  lat + "&current_long=" +
			  	  lng + "&radius=10";
		}
		else if(headerTxt.equalsIgnoreCase(getString(R.string.bank))) {
			URL = Constants.URL_BANK + "&current_lat=" +
			  	  lat + "&current_long=" +
			  	  lng + "&radius=10";
		}
		else {
			URL = Constants.URL_BAR + lat + 
				  "&current_long=" + lng + 
				  "&radius=10";
		}
	}

	private Runnable returnRes = new Runnable() {
		@Override
		public void run() {
			//if (merchantList != null && merchantList.size() > 0) {
			//	m_adapter.notifyDataSetChanged();
			//	for (int i = 0; i < merchantList.size(); i++) {
			//		m_adapter.add(merchantList.get(i));
			//	}
			//	settingNextBackButton();
			//}
			if (merchantLtd != null && merchantLtd.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < merchantLtd.size(); i++) {
					m_adapter.add(merchantLtd.get(i));
					System.out.println(merchantLtd.get(i).getDistance());
				}
				settingNextBackButton();
			}
			// data is null
			else {
				Util.showAlert(instance, headerTxt,
						getString(R.string.no_item_found),
						getString(R.string.ok), true);
			}
			progressDialog.dismiss();
		}
	};

	private void getJSON() {
		
		System.out.println(URL);
				
		try {
			
			//if(merchantList == null || merchantList.isEmpty()) {
				//URL = "http://www.citiworldprivileges.com/mobile/?country=Singapore&city=Singapore&offer_type=Dining&sort_by=distance&sort_order=asc&num_offers=10&current_lat=1.33665909&current_long=103.8482263&radius=10&page_no=4";
				merchantList = Util.getMerchants(URL);
			//}
			
			for(int i=0; i < merchantList.size(); i++) {
				double lat1;
				double lng1;
				
				try {
					lat1 = Double.parseDouble(merchantList.get(i).getLatitude());
					lng1 = Double.parseDouble(merchantList.get(i).getLongitude());
				}
				catch (Exception e) {
					e.printStackTrace();
					lat1 = Constants.SINGAPORE_LATITUDE;
					lng1 = Constants.SINGAPORE_LONGITUDE;
				}
				merchantList.get(i).setDistance(Util.convertLatLongToDist(lat, lng, lat1, lng1));
			}
			
			//Server API will sort this one already.
			//sortByDistance();
			
			totalItems = merchantList.size();
			
			startItem = (page * 10) - 9;
			endItem = page * 10;
			
			if(endItem > totalItems) {
				endItem = totalItems;
			}
			
			merchantLtd = new ArrayList<MerchantInfo1>();
			merchantLtd = Util.getMerchantList(merchantList, page, merchantList.size());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		String message = "";
		
		int size = 10;
		
		if(merchantLtd.size() < 10) {
			size = merchantLtd.size();
		}
		
		for(int ctr = 0; ctr < size; ctr++) {
			message += "Merchant Name:\n";
			message += merchantLtd.get(ctr).getMerchantName();
			message += "\n";
			message += "Merchant Address:\n";
			message += merchantLtd.get(ctr).getPostalAddress();
			message += "\n\n";
		}
		
		CitiGuideListActivity.message = message;
		CitiGuideActivity.message = message;
		CitiGuideListActivity.subject = headerTxt;
		CitiGuideActivity.subject = headerTxt;
		
		//try {
			// read data
			//String result = Util.getHttpData(Constants.URL_MERCHANT_LIST + id
			//		+ "&sc=" + sId + "&maxRow=" + Constants.ITEMS_PER_PAGE
			//		+ "&startRow=" + ((page - 1) * Constants.ITEMS_PER_PAGE)
			//		+ querySearch);
			//result = Util.toJSONString(result);

			//merchantList = new ArrayList<MerchantInfo>();
			//merchantList = new ArrayList<MerchantInfo1>();

			// JSONObject Parsing
			//JSONObject json = new JSONObject(result);
			//JSONArray nameArray = json.names();
			//JSONArray valArray = json.toJSONArray(nameArray);
			//for (int i = 0; i < valArray.length(); i++) {
			//	if (!valArray.getString(i).equalsIgnoreCase("")) {
			//		try {
			//			JSONArray jArray1 = valArray.getJSONArray(i);
			//			for (int j = 0; j < jArray1.length(); j++) {
			//				JSONObject jArray2 = jArray1.getJSONObject(j);

							// content info
							//String name = "";
							//String address = "";
							//String phone = "";
							//String rate = "";
							//String desc = "";
							//String email = "";
							//String price = "";
							//String website = "";
							//String open = "";
							//String lat = "";
							//String lng = "";

							//name = jArray2.getString("name");
							//address = jArray2.getString("address");
							//desc = jArray2.getString("description");
							//phone = jArray2.getString("phone");
							//price = jArray2.getString("price");
							//email = jArray2.getString("email");
							//website = jArray2.getString("website");
							//open = jArray2.getString("op");
							//lat = jArray2.getString("lat");
							//lng = jArray2.getString("longi");
							//lat = lat.replaceAll(",", "");
							//lng = lng.replaceAll(",", "");

							//MerchantInfo cInfo = new MerchantInfo(Integer
							//		.parseInt(id), sId, name, address, phone,
							//		"0", desc, email, price, website, open,
							//		lat, lng);
							//cInfo.setIndex((j + 1));
							//merchantList.add(cInfo);
						//}

					//} catch (Exception e) {
						// TODO: handle exception
					//}
				//}

				//if (nameArray.getString(i).equalsIgnoreCase("total")) {
				//	totalItems = Integer.parseInt(valArray.getString(i));
				//	totalPage = totalItems / Constants.ITEMS_PER_PAGE;
				//	if (totalItems % Constants.ITEMS_PER_PAGE != 0)
				//		totalPage += 1;
				//}
				//if (nameArray.getString(i).equalsIgnoreCase("start")) {
				//	startItem = Integer.parseInt(valArray.getString(i));
				//}
				//if (nameArray.getString(i).equalsIgnoreCase("end")) {
				//	endItem = Integer.parseInt(valArray.getString(i));
				//}
			//}

		//} catch (Exception e) {
		//	e.printStackTrace();
		//}
	}

	private void sortByDistance() {
		ArrayList<MerchantInfo1> temp = merchantList;
		for(int i = 0; i < temp.size(); i++){
			for(int j = i+1; j < temp.size(); j++){
				MerchantInfo1 nextInfo = temp.get(j);
				double nextDistance = temp.get(j).getDistance();		
				
				MerchantInfo1 currInfo = temp.get(i);
				double currentDistance = temp.get(i).getDistance();
				
				if(currentDistance > nextDistance){
					nextInfo.setIndex(i+1);
					currInfo.setIndex(j+1);
					merchantList.set(i, nextInfo);
					merchantList.set(j, currInfo);
				}
			}
		}
		temp = null;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id_) {
		MerchantInfo1 cInfo = merchantLtd.get(position);
		nextScreen(cInfo);
	}

	//public void nextScreen(MerchantInfo cInfo) {
	public void nextScreen(MerchantInfo1 cInfo) {
		DescriptionScreen.merchantInfo = cInfo;
		Intent intent = new Intent(instance, DescriptionScreen.class);
		startActivityForResult(intent, 0);
	}

	public void settingNextBackButton() {
		TextView result = (TextView) findViewById(R.id.tabText);
		result.setText("Results (" + startItem + "-" + endItem + ") of "
				+ totalItems);

		ImageView tabBack = (ImageView) findViewById(R.id.tabBack);
		tabBack.setImageResource(R.drawable.button_cust_tab_back);
		tabBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (page <= totalPage) {
					page--;
					init();
				}
			}
		});
		ImageView tabNext = (ImageView) findViewById(R.id.tabNext);
		tabNext.setImageResource(R.drawable.button_cust_tab_next);
		tabNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (page < totalPage) {
					page++;
					init();
				}
			}
		});

		System.out.println(page + " " + totalPage);

		if (totalPage == 1) {
			tabBack.setVisibility(ImageView.GONE);
			tabNext.setVisibility(ImageView.GONE);
			LinearLayout ll = (LinearLayout)findViewById(R.id.tabhostButtonLayout);
			ll.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		}
		if (page == 1 && totalPage > 1) {
			tabNext.setVisibility(ImageView.VISIBLE);
			tabBack.setVisibility(ImageView.INVISIBLE);
		}
		if (page == totalPage && totalPage > 1) {
			tabBack.setVisibility(ImageView.VISIBLE);
			tabNext.setVisibility(ImageView.INVISIBLE);
		}
		if (page > 1 && page < totalPage) {
			tabBack.setVisibility(ImageView.VISIBLE);
			tabNext.setVisibility(ImageView.VISIBLE);
		}
	}

	/**
	 * ============================================= inner class create list
	 * item. =============================================
	 * 
	 * @author Administrator
	 * 
	 */
	private class ListViewAdapter extends ArrayAdapter<MerchantInfo1> {
		private ArrayList<MerchantInfo1> items;

		public ListViewAdapter(Context context, int textViewResourceId,
			ArrayList<MerchantInfo1> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@SuppressWarnings("static-access")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_merchant_list, null);

			}
			//final MerchantInfo info = items.get(position);
			final MerchantInfo1 info = items.get(position);
			if (info != null) {
				final ImageView imView = (ImageView) v.findViewById(R.id.rowlistIconIView);
				//final RoundedImage imView = (RoundedImage) v.findViewById(R.id.rowlistIconIView);

				try {
					Bitmap bitmap = Util.getBitmap(Constants.URL_IMAGE + info.getThumbImageName());
					if(bitmap == null) {
						bitmap = new BitmapFactory().decodeResource(MainCitiGuideScreen.instance.getResources(), R.drawable.listicon);
					}
					bitmap = Util.resizeImage(bitmap, 60, 60);
					bmp = Util.getRoundedCornerBitmap(bitmap);
					//bitmap = Util.createRoundImage(bitmap);
					imView.setImageBitmap(bmp);
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
				//} else {
				//	imView.setImageResource(R.drawable.icon_call);
				//}
				//imView.setOnClickListener(new OnClickListener() {

				//	@Override
				//	public void onClick(View v) {
						// TODO Auto-generated method stub
				//		if (!info.getTel().equalsIgnoreCase(""))
				//			Util.makeCall(instance, info.getTel());
				//	}
				//});

				TextView tTView = (TextView) v.findViewById(R.id.topTView);
				TextView mTView = (TextView) v.findViewById(R.id.middleTView);
				TextView bTView = (TextView) v.findViewById(R.id.bottomTView);
				if (tTView != null) {
					tTView.setText(info.getMerchantName());
				}
				if (mTView != null) {
					mTView.setText(info.getPostalAddress());
				}
				if (bTView != null) {
					bTView.setText("");
				}

//				ImageView pinView = (ImageView) v
//						.findViewById(R.id.rowlistPinView);
//				pinView.setImageResource(Controller.getResourcePin(info
//						.getIndex()));
			}
			return v;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		ARScreen.isMerchantList = true;
		super.onResume();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			
			Runnable runnable =	new Runnable() {
				@Override
				public void run() {
					if(progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
				}
			};
			
			Thread thread = new Thread(null, runnable, "runnable");
			thread.start();
			
			instance.finish();
			return true;
		}
		else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
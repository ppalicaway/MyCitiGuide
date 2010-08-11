package com.cellcity.citiguide.screen;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cellcity.citiguide.adapter.BitmapLoader;
import com.cellcity.citiguide.info.TwitterInfo;
import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;

public  class TwitterScreen extends CitiGuideListActivity {
	public static TwitterScreen instance;
	private ProgressDialog progressDialog = null;
	private Runnable initR;
	private ArrayList<TwitterInfo> m_orders = null;
	private ListViewAdapter m_adapter;
	private Runnable viewOrders;
	
	private ArrayList<TwitterInfo> twList;
	
	// share data
	private String rId;
	private String id;
	private String headerTxt;
	//private MerchantInfo mInfo;
	
	private int total, page, results_per_page;
	public static HashMap imageHash;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.listing);
		instance = this;
		
		initActivity(instance, getText(R.string.search).toString());
		init();
	}
	
	public void init(){
		settingLayout();
		
		try {
			progressDialog = ProgressDialog.show(this, "",
				getString(R.string.retrieving_data), true);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
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
	
	public void settingLayout(){
		imageHash = new HashMap<String, ImageView>();
		
		// #### read share data ####
		SharedPreferences preferences = getSharedPreferences(
				Constants.DEFAUL_SHARE_DATA, 0);
		rId = preferences.getString("rId", "1");
		id = preferences.getString("id", "1");
		headerTxt = preferences.getString("name", "");
		// #########################
		
		headerTxt = getString(R.string.twitter);
		TextView titleView = (TextView)findViewById(R.id.templateTopTitleTView);
		titleView.setText(headerTxt);
		
		m_orders = new ArrayList<TwitterInfo>();
		m_adapter = new ListViewAdapter(this, R.layout.row_twitter_list, m_orders);
		setListAdapter(m_adapter);
	}
	
	
	/**
	 * ========================================================
	 */
	private Runnable returnRes = new Runnable() {
		@Override
		public void run() {
			if (twList != null && twList.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < twList.size(); i++) {
					m_adapter.add(twList.get(i));
				}
			}
			// data is null
			else {
				Util.showAlert(instance, headerTxt,
						getString(R.string.no_item_found),
						getString(R.string.ok), true);
			}
			
			try {
				if (progressDialog.isShowing())
					progressDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

	private void getJSON() {
		try {
			// read data
			//String queryText = mInfo.getTitle();
			//queryText = queryText.replaceAll(" ", "+");
			//String url = "http://search.twitter.com/search.json?callback=?&q="+queryText;//+"Billy+Bombers+American+Diner";//
			//String result = Util.getHttpData(url);
			//result = Util.toJSONString(result);
			
//			System.out.println("result = " + result);

			twList = new ArrayList<TwitterInfo>();

			// JSONObject Parsing
			//JSONObject json = new JSONObject(result);
			//JSONArray nameArray = json.names();
			//JSONArray valArray = json.toJSONArray(nameArray);
			//for (int i = 0; i < valArray.length(); i++) {
			//	if (nameArray.getString(i).equalsIgnoreCase("results")) {
			//		try {
			//			JSONArray jArray1 = valArray.getJSONArray(i);
			//			for (int j = 0; j < jArray1.length(); j++) {
			//				JSONObject jArray2 = jArray1.getJSONObject(j);

			//				// content info
			//				String profile_image_url = "";
			//				String created_at = "";
			//				String from_user = "";
			//				String text = "";

			//				profile_image_url = jArray2.getString("profile_image_url");
			//				created_at = jArray2.getString("created_at");
			//				from_user = jArray2.getString("from_user");
			//				text = jArray2.getString("text");
			//				created_at = created_at.substring(0, created_at.lastIndexOf(" "));
							
			//				twList.add(new TwitterInfo(j+"", profile_image_url, created_at, from_user, text));
			//			}

			//		} catch (Exception e) {
			//			// TODO: handle exception
			//			e.printStackTrace();
			//		}
			//	}

			//	if (nameArray.getString(i).equalsIgnoreCase("total")) {
			//		total = Integer.parseInt(valArray.getString(i));
			//	}
			//	if (nameArray.getString(i).equalsIgnoreCase("results_per_page")) {
			//		results_per_page = Integer.parseInt(valArray.getString(i));
			//	}
			//	if (nameArray.getString(i).equalsIgnoreCase("page")) {
			//		page = Integer.parseInt(valArray.getString(i));
			//	}
			//}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ============================================= inner class create list
	 * item. =============================================
	 * 
	 * @author Administrator
	 * 
	 */
	public class ListViewAdapter extends ArrayAdapter<TwitterInfo> {
		private ArrayList<TwitterInfo> items;
		private Typeface face;
		public ListViewAdapter(Context context, int textViewResourceId,
				ArrayList<TwitterInfo> items) {
			super(context, textViewResourceId, items);
			this.items = items;
			this.face = Typeface.createFromAsset(getAssets(),"fonts/Arial.ttf");
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_twitter_list, null);
			}
			final TwitterInfo info = items.get(position);

			if (info != null) {
				ImageView imageView = (ImageView)v.findViewById(R.id.rowlistIconIView);
				//=============
				String id = info.getIndex();
				String path = info.getProfile_image_url();
				if(!imageHash.containsKey(id)){
					imageView.setImageResource(R.drawable.icon);
					imageHash.put(id, imageView);
					BitmapLoader bLoader = new BitmapLoader(instance, id, path);
					bLoader.start();
				}else{
					imageView = (ImageView)imageHash.get(id);
				}
				
				TextView tTView = (TextView) v.findViewById(R.id.topTView);
//				tTView.setTypeface(face);
				TextView mTView = (TextView) v.findViewById(R.id.middleTView);
//				mTView.setTypeface(face);
				TextView bTView = (TextView) v.findViewById(R.id.bottomTView);
				
				tTView.setText(info.getFrom_user());
				mTView.setText(info.getText());
				bTView.setText(info.getCreated_at());	
			}

			
			return v;
		}
		
		public void setImage(Bitmap bitmap_){
//			iconBitmap = bitmap_;
//			bitmap.invalidate();
		}
	}

}


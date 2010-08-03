package com.cellcity.citiguide.screen;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.cellcity.citiguide.info.ContentInfo;
import com.cellcity.citiguide.info.MerchantInfo1;
import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;

public class ListingScreen extends CitiGuideListActivity {
	public static ListingScreen instance;
	private ProgressDialog progressDialog = null;
	private Runnable initR;

	private Bitmap bitmap;
	private int iconW, iconH;
	private ArrayList<MerchantInfo1> m_orders = null;
	private ListViewAdapter m_adapter;

	// share data
	private String rId;
	private String id;
	private String headerTxt;
	
	private String URL;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Translucent);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listing);

		instance = this;
		
		Button map = (Button)findViewById(R.id.mapButton);
		map.setVisibility(Button.INVISIBLE);
		Button ar = (Button)findViewById(R.id.arButton);
		ar.setVisibility(Button.INVISIBLE);
		
		initActivity(instance, "");

		ListView listView = (ListView)findViewById(android.R.id.list);
		listView.setDividerHeight(0);
		
		settingLayout();
		init();
	}

	public void init() {
		// display progress dialog
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
				m_orders = Util.getMerchants(URL);
				runOnUiThread(returnRes);
			}
		};
		Thread thread = new Thread(null, initR, "initR");
		thread.start();
	}

	private void settingLayout() {
		// #### read share data ####
		SharedPreferences preferences = getSharedPreferences(
				Constants.DEFAUL_SHARE_DATA, 0);
		rId = preferences.getString("rId", "1");
		id = preferences.getString("id", "1");
		headerTxt = preferences.getString("name", "");
		
		determineURL(headerTxt);
		// #########################

		TextView titleView = (TextView) findViewById(R.id.templateTopTitleTView);
		titleView.setText(headerTxt);

		m_orders = new ArrayList<MerchantInfo1>();
		m_adapter = new ListViewAdapter(this, R.layout.row_list, m_orders);
		setListAdapter(m_adapter);
		
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
		iconW = bitmap.getWidth();
		iconH = bitmap.getHeight();
	}

	private void determineURL(String headerTxt) {
		if(headerTxt.equalsIgnoreCase(getString(R.string.dining))) {
			URL = Constants.URL_DINING;
		}
		else if(headerTxt.equalsIgnoreCase(getString(R.string.shopping))) {
			URL = Constants.URL_SHOPPING;
		}
	}

	private Runnable returnRes = new Runnable() {
		@Override
		public void run() {
			if (m_orders != null && m_orders.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < m_orders.size(); i++) {
					m_adapter.add(m_orders.get(i));
				}
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
		try {
			// read data
			String result = Util.getHttpData(Constants.URL_CATEGORY + id);
			result = Util.toJSONString(result);
			// if(Util.validateHttpCode(result)){
			// Util.showAlert(instance, getString(R.string.conn_err_title),
			// getString(R.string.conn_err_text), getString(R.string.ok), true);
			// return;
			// }

			// JSONObject Parsing
			JSONObject json = new JSONObject(result);
			JSONArray nameArray = json.names();
			JSONArray valArray = json.toJSONArray(nameArray);
			for (int i = 0; i < valArray.length(); i++) {
				if (!valArray.getString(i).equalsIgnoreCase("")) {
					try {
						JSONArray jArray1 = valArray.getJSONArray(i);
						for (int j = 0; j < jArray1.length(); j++) {
							JSONObject jArray2 = jArray1.getJSONObject(j);
							String name = jArray2.getString("name");
							String subcatId = jArray2
									.getString("subcategory_id");
						}

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}

			// ==========//

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		MerchantInfo1 cInfo = m_orders.get(position);
		nextScreen(cInfo);
	}

	/**
	 * ============================================= inner class create list
	 * item. =============================================
	 * 
	 * @author Administrator
	 * 
	 */
	private class ListViewAdapter extends ArrayAdapter<MerchantInfo1> {
		//private ArrayList<ContentInfo> items;
		private ArrayList<MerchantInfo1> items;

		public ListViewAdapter(Context context, int textViewResourceId,
			ArrayList<MerchantInfo1> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_list, null);
			}
			
			//v.
			//LinearLayout ll = (LinearLayout)findViewById(R.id.widget34);
			//ListView lv = (ListView)findViewById(android.R.id.list);
			//lv.getChildAt(1).setBackgroundResource(R.drawable.list_1);
			//lv.getChildAt(lv.getChildCount() - 1).setBackgroundResource(R.drawable.list_3);
			
			//if(position == 1) {
			//	ll.setBackgroundResource(R.drawable.list_1);
			//}			
			
			final MerchantInfo1 info = items.get(position);
			if (info != null) {
//				
				TextView tTView = (TextView) v.findViewById(R.id.topTView);
				TextView mTView = (TextView) v.findViewById(R.id.middleTView);
				TextView bTView = (TextView) v.findViewById(R.id.bottomTView);
				if (tTView != null) {
					tTView.setTypeface(Typeface.DEFAULT);
					tTView.setText(info.getMerchantName());
					mTView.setVisibility(TextView.GONE);
					bTView.setVisibility(TextView.GONE);
				}
			}
			
			return v;
		}
	}

	private void nextScreen(MerchantInfo1 info) {
		SharedPreferences preferences = getSharedPreferences(
				Constants.DEFAUL_SHARE_DATA, 0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("id", id);
		editor.putString("name", headerTxt);

		ListingMerchantScreen.querySearch = "";
		Intent intent = new Intent(instance, ListingMerchantScreen.class);
		startActivityForResult(intent, 0);

		if (editor.commit()) {
			setResult(RESULT_OK);
		}
	}
}
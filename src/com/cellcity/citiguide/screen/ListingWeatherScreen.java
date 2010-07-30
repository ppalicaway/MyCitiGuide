package com.cellcity.citiguide.screen;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cellcity.citiguide.info.WeatherInfo;
import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;

public class ListingWeatherScreen extends CitiGuideListActivity  {
	public static ListingWeatherScreen instance;
	private ProgressDialog progressDialog = null;
	private Runnable initR;
	
	private Bitmap bitmap;
	private int iconW, iconH;
	private ArrayList<WeatherInfo> m_orders = null;
	private ListViewAdapter m_adapter;
	
	// share data
	private String rId;
	private String id;
	private String headerTxt;
	
	public static String URL;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Translucent);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.listing);
		
		instance = this;
		
		initActivity(instance, "");

		settingLayout();
		init();
	}

	
	public void init() {
		// display progress dialog
		progressDialog = ProgressDialog.show(this, "",
				getString(R.string.retrieving_data), true);
		
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
	
	private void settingLayout(){
		//#### read share data ####
		SharedPreferences preferences = getSharedPreferences(Constants.DEFAUL_SHARE_DATA, 0);
		rId = preferences.getString("rId", "1");
		id = preferences.getString("id", "1");
		headerTxt = preferences.getString("name", "");
		//#########################
		
		
		TextView titleView = (TextView)findViewById(R.id.templateTopTitleTView);
		titleView.setText(headerTxt);
		
		m_orders = new ArrayList<WeatherInfo>();
		m_adapter = new ListViewAdapter(this, R.layout.row_list, m_orders);
		setListAdapter(m_adapter);
		
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
		iconW = bitmap.getWidth();
		iconH = bitmap.getHeight();
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
			else{
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
			String result = Util.getHttpData(URL);
			result = Util.toJSONString(result);	
//			if(Util.validateHttpCode(result)){
//				Util.showAlert(instance, getString(R.string.conn_err_title), 
//						getString(R.string.conn_err_text), getString(R.string.ok), true);
//				return;
//			}
						
			m_orders = new ArrayList<WeatherInfo>();

			// JSONObject Parsing
			JSONObject json = new JSONObject(result);
            JSONArray nameArray=json.names();
            JSONArray valArray=json.toJSONArray(nameArray);
			for (int i = 0; i < valArray.length(); i++) {
				if(nameArray.getString(i).equalsIgnoreCase("items")){
					try {
						JSONArray jArray1 = valArray.getJSONArray(i);
						for (int j = 0; j < jArray1.length(); j++) {
							JSONObject jArray2 = jArray1.getJSONObject(j);
							
							if(URL.equalsIgnoreCase(Constants.URL_WEATHER_TODAY)){
								String icon = jArray2.getString("icon");
								String location = jArray2.getString("location");
								String description = jArray2.getString("description");
								String temperature = jArray2.getString("temperature");
								
								WeatherInfo wInfo = new WeatherInfo(icon, location, description, temperature);
								m_orders.add(wInfo);
							}
							else if(URL.equalsIgnoreCase(Constants.URL_WEATHER_FORECAST)){
								String icon = jArray2.getString("icon");
								String weekday = jArray2.getString("weekday");
								String date = jArray2.getString("date");
								String high_temp = jArray2.getString("high_temp");
								String low_temp = jArray2.getString("low_temp");
								String uv = jArray2.getString("uv");
								String uv_index = jArray2.getString("uv_index");
								String description = jArray2.getString("description");
								
								WeatherInfo wInfo = new WeatherInfo(icon,
										weekday, date, high_temp, low_temp, uv,
										uv_index, description);
								m_orders.add(wInfo);
							}

						}

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}

			//==========//

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * =============================================
	 * inner class 
	 * create list item.
	 * =============================================
	 * @author Administrator
	 *
	 */
	private class ListViewAdapter extends ArrayAdapter<WeatherInfo>{
		private ArrayList<WeatherInfo> items;
		private Bitmap bitmap;
		private Context context;
		public ListViewAdapter(Context context, int textViewResourceId,
				ArrayList<WeatherInfo> items) {
			super(context, textViewResourceId, items);
			this.items = items;
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_weather, null);
			}
			final WeatherInfo info = items.get(position);
			if (info != null) {
				ImageView imView = (ImageView) v.findViewById(R.id.rowlistIconIView);
				bitmap = Util.loadImage(context, "weather_"+info.getIcon(), false);
				imView.setImageBitmap(bitmap);
				
				TextView tTView = (TextView) v.findViewById(R.id.topTView);
				TextView mTView = (TextView) v.findViewById(R.id.middleTView);
				TextView bTView = (TextView) v.findViewById(R.id.bottomTView);

				if(URL.equalsIgnoreCase(Constants.URL_WEATHER_TODAY)){
					tTView.setText(info.getLocation());
					mTView.setText(Html.fromHtml("Temp : " + info.getTemperature()+" &deg;C"));
					bTView.setText("Desc : " + info.getDescription());
				}
				else if(URL.equalsIgnoreCase(Constants.URL_WEATHER_FORECAST)){
					tTView.setText(info.getWeekday());
					mTView.setText(Html.fromHtml("Temp : " + info.getHigh_temp()+" &deg;C(H), " + info.getLow_temp()+" &deg;C(L)"));
					bTView.setText("Desc : " + info.getDescription());
				}
			}
			return v;
		}
	}
	
}
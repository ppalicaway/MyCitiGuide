package com.cellcity.citiguide.screen;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cellcity.citiguide.info.MovieInfo;
import com.cellcity.citiguide.info.TheatreInfo;
import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;

public class MovieScreen extends CitiGuideListActivity {
	public static MovieScreen instance;

	private ProgressDialog progressDialog = null;
	private Runnable initR;	

	private Bitmap bitmap;
	private int iconW, iconH;
	private ArrayList<MovieInfo> m_orders = null;
	private ListViewAdapter m_adapter;

	// share data
	private String rId;
	private String id;
	private String headerTxt;

	public static String queryString = "";
	public static MovieInfo movieInfo = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Translucent);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listing);

		instance = this;

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
				getJSON();
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
		// #########################

		TextView titleView = (TextView) findViewById(R.id.templateTopTitleTView);
		titleView.setText(headerTxt);
		
		Button map = (Button)findViewById(R.id.mapButton);
		Button ar = (Button)findViewById(R.id.arButton);
		map.setVisibility(Button.GONE);
		ar.setVisibility(Button.GONE);
		
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.widget35);
		linearLayout.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		
		Button home = (Button)findViewById(R.id.homeButton);
		home.setOnClickListener(new MenuListener());
		Button search = (Button)findViewById(R.id.search_Button);
		search.setOnClickListener(new MenuListener());
		Button nearby = (Button)findViewById(R.id.nearbyButton);
		nearby.setOnClickListener(new MenuListener());
		Button share = (Button)findViewById(R.id.shareButton);
		share.setOnClickListener(new MenuListener());
		Button mapB = (Button)findViewById(R.id.map_Button);
		mapB.setOnClickListener(new MenuListener());

		m_orders = new ArrayList<MovieInfo>();
		m_adapter = new ListViewAdapter(this, R.layout.row_list, m_orders);
		setListAdapter(m_adapter);

		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
		iconW = bitmap.getWidth();
		iconH = bitmap.getHeight();
	}

	private void getJSON() {
		try {
			// read data
			String result = Util.getHttpData(Constants.URL_MOVIE);
			System.out.println("result ==========="+result);
			result = Util.toJSONString(result);
			
			m_orders = new ArrayList<MovieInfo>();

			// JSONObject Parsing
			JSONObject json = new JSONObject(result);
			JSONArray nameArray = json.names();
			JSONArray valArray = json.toJSONArray(nameArray);
			String message = "";
			for (int i = 0; i < valArray.length(); i++) {
				if(nameArray.getString(i).equalsIgnoreCase("items")){
					try {
						JSONArray totalArray = valArray.getJSONArray(i);
						for (int j = 0; j < totalArray.length(); j++) {
							
							JSONObject elemArray = totalArray.getJSONObject(j);
							
							String movieName = elemArray.getString("name");
							CitiGuideListActivity.message = CitiGuideListActivity.message + movieName + "\n";
							CitiGuideActivity.message = CitiGuideActivity.message + movieName + "\n";
							String tName = "";
							String tAddress = "";
							String showtime = "";
							
							ArrayList<TheatreInfo> theatreList = new ArrayList<TheatreInfo>();
							
							//System.out.println("movieName   = " + movieName);
							message += movieName;
							message += "\n";
							
							JSONArray elemNameArray = elemArray.names();
							JSONArray elemValArray = elemArray.toJSONArray(elemNameArray);
							for (int k = 0; k < elemValArray.length(); k++) {
								if(elemNameArray.getString(k).equalsIgnoreCase("theatres")){
									
									JSONArray theatreArray = elemValArray.getJSONArray(k);
									for(int l = 0; l < theatreArray.length(); l++){
										JSONObject theaterElemArray = theatreArray.getJSONObject(l);
										
										tName = theaterElemArray.getString("name");
										tAddress = theaterElemArray.getString("address");
										showtime = theaterElemArray.getString("showtimes");
										
//										System.out.println("	tName      = " + tName);
//										System.out.println("	tAddress   = " + tAddress);
//										System.out.println("	tTime      = " + showtime);
										theatreList.add(new TheatreInfo(tName, tAddress, showtime));
									}
								}
							}							

//							MovieInfo mInfo = new MovieInfo(movieName, tName, tAddress, showtime);									
							MovieInfo mInfo = new MovieInfo(movieName, theatreList);
							m_orders.add(mInfo);
						}
						
						CitiGuideListActivity.message = message;
						CitiGuideActivity.message = message;
												
						System.out.println(message);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
			
			//System.out.println(CitiGuideListActivity.message);
			//System.out.println(CitiGuideActivity.message);

			// ==========//
			
			// test
			//for(int i = 0; i < m_orders.size(); i++){
			//	MovieInfo mInfo = (MovieInfo)m_orders.get(i);
			//	System.out.println("name = " + mInfo.getMovieName());
				
				
			//	for(int j = 0; j < mInfo.getTheatreList().size(); j++){
			//		TheatreInfo tInfo = (TheatreInfo)mInfo.getTheatreList().get(j);
			//		System.out.println("gettName()" + tInfo.gettName());
			//		System.out.println("gettAddress()" + tInfo.gettAddress());
			//		System.out.println("gettTime()" + tInfo.gettTime());
			//	}
			//}

		} catch (Exception e) {
			e.printStackTrace();
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
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		movieInfo = m_orders.get(position);
		nextScreen(movieInfo);
	}
	
	private void nextScreen(MovieInfo minfo) {
		SharedPreferences preferences = getSharedPreferences(Constants.DEFAUL_SHARE_DATA, 0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("id", id);
//		editor.putString("sId", minfo.getsId());
		editor.putString("name", headerTxt);

//		MovieResultScreen.querySearch = "";
		Intent intent = new Intent(instance, MovieResultScreen.class);
		startActivityForResult(intent, 0);

		if (editor.commit()) {
			setResult(RESULT_OK);
		}
	}

	/**
	 * ============================================= inner class create list
	 * item. =============================================
	 * 
	 * @author Administrator
	 * 
	 */
	private class ListViewAdapter extends ArrayAdapter<MovieInfo> {
		private ArrayList<MovieInfo> items;

		public ListViewAdapter(Context context, int textViewResourceId,
				ArrayList<MovieInfo> items) {
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
			final MovieInfo info = items.get(position);
			if (info != null) {
				ImageView imView = (ImageView) v
						.findViewById(R.id.rowlistIconIView);
//				imView.setImageResource(Integer.parseInt(rId));

				// imView.setOnClickListener(new OnClickListener() {
				//					
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// nextScreen(info);
				// }
				// });

				TextView tTView = (TextView) v.findViewById(R.id.topTView);
				TextView mTView = (TextView) v.findViewById(R.id.middleTView);
				TextView bTView = (TextView) v.findViewById(R.id.bottomTView);
				if (tTView != null) {
					tTView.setTypeface(Typeface.DEFAULT);
					tTView.setText(info.getMovieName());
					mTView.setVisibility(TextView.GONE);
					bTView.setVisibility(TextView.GONE);
				}
			}
			return v;
		}
	}
}

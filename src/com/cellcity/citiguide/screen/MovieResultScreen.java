package com.cellcity.citiguide.screen;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

public class MovieResultScreen extends CitiGuideListActivity{
	public static MovieResultScreen instance;
	private Runnable initR;	
	
	private ProgressDialog progressDialog=null;
	private ArrayList<TheatreInfo> m_orders = null;
	private ListViewAdapter m_adapter;
	//share data
	private String headerTxt;
	
	private MovieInfo movieInfo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Translucent);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listing);

		instance = this;
		
		ListView listView = (ListView)findViewById(android.R.id.list);
		listView.setDividerHeight(0);
		
		ImageView topMovie = (ImageView)findViewById(R.id.topmovie);
		topMovie.setVisibility(ImageView.GONE);

		initActivity(instance, "");

		settingLayout();
		init();
	}

	private void settingLayout(){
		// #### read share data ####
		SharedPreferences preferences = getSharedPreferences(Constants.DEFAUL_SHARE_DATA, 0);
		
		headerTxt = preferences.getString("name", "");
		// #########################
		
		// init movie info
		movieInfo = MovieScreen.movieInfo;

		TextView titleView = (TextView) findViewById(R.id.templateTopTitleTView);
		titleView.setText(movieInfo.getMovieName());
		
		CitiGuideListActivity.subject = movieInfo.getMovieName();
		CitiGuideActivity.subject = movieInfo.getMovieName();
		
		Button map = (Button)findViewById(R.id.mapButton);
		Button ar = (Button)findViewById(R.id.arButton);
		map.setVisibility(Button.GONE);
		ar.setVisibility(Button.GONE);
		
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.widget35);
		linearLayout.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		
		Button home = (Button)findViewById(R.id.homeButton);
		home.setOnClickListener(new MenuListener());
		//Button search = (Button)findViewById(R.id.search_Button);
		//search.setOnClickListener(new MenuListener());
		//Button nearby = (Button)findViewById(R.id.nearbyButton);
		//nearby.setOnClickListener(new MenuListener());
		//Button share = (Button)findViewById(R.id.shareButton);
		//share.setOnClickListener(new MenuListener());
		//Button mapB = (Button)findViewById(R.id.map_Button);
		//mapB.setOnClickListener(new MenuListener());

		m_orders = new ArrayList<TheatreInfo>();
		m_adapter = new ListViewAdapter(this, R.layout.row_movie_list, m_orders);
		setListAdapter(m_adapter);
	}
	public void init() {
		// display progress dialog
		progressDialog = ProgressDialog.show(this, "",
				getString(R.string.retrieving_data), true);

		// start thread
		initR = new Runnable() {
			@Override
			public void run() {
			//	getJSON();
				m_orders = movieInfo.getTheatreList();
				runOnUiThread(returnRes);
			}
		};
		Thread thread = new Thread(null, initR, "initR");
		thread.start();
	}
	private Runnable returnRes = new Runnable() {
		@Override
		public void run() {
			String message = "";
			String subject = "";
			if (m_orders != null && m_orders.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < m_orders.size(); i++) {
					m_adapter.add(m_orders.get(i));
					message += "Theatre Name:\n";
					message += m_orders.get(i).gettName();
					message += "\n";
					message += "Theatre Address:\n";
					message += m_orders.get(i).gettAddress();
					message += "\n";
					message += "Showtimes:\n";
					message += m_orders.get(i).gettTime();
					message += "\n\n";
				}
			}
			// data is null
			else {
				Util.showAlert(instance, headerTxt,
						getString(R.string.no_item_found),
						getString(R.string.ok), true);
			}
			CitiGuideListActivity.message = message;
			CitiGuideActivity.message = message;
			progressDialog.dismiss();
		}
	};
	
	/**
	 * ============================================= 
	 * inner class create list item. 
	 * =============================================
	 * 
	 * @author Administrator
	 * 
	 */
	private class ListViewAdapter extends ArrayAdapter<TheatreInfo> {
		private ArrayList<TheatreInfo> items;

		public ListViewAdapter(Context context, int textViewResourceId,
				ArrayList<TheatreInfo> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_movie_list, null);
			}
			final TheatreInfo tInfo = items.get(position);
			if (tInfo != null) {

				TextView tNameTView = (TextView) v.findViewById(R.id.theatreName);
				tNameTView.setText(tInfo.gettName());
				TextView tAddrTView = (TextView) v.findViewById(R.id.theatreAddress);
				tAddrTView.setText(tInfo.gettAddress());
				TextView bTView = (TextView) v.findViewById(R.id.theatreShowtime);
				bTView.setText(tInfo.gettTime());
			}
			return v;
		}
	}

}

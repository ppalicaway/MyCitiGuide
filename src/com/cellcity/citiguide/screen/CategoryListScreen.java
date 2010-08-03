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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cellcity.citiguide.info.CategoryInfo;
import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;


public class CategoryListScreen extends CitiGuideListActivity {
	public static CategoryListScreen instance;
	public static ArrayList<CategoryInfo> categoryList = null;
	private ProgressDialog progressDialog = null;
	private Runnable initR;

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
	public static CategoryInfo categoryInfo;
	public static String querySearch = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.Theme_Translucent);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listingmerchant);

		instance = this;

		initActivity(instance, "");

		page = 1;
		totalPage = 1;

		init();
	}

	public void init() {
		settingLayout();

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
		// ########## [ read share data ] ###########
		SharedPreferences preferences = getSharedPreferences(
				Constants.DEFAUL_SHARE_DATA, 0);
		id = preferences.getString("id", "1");
		sId = preferences.getString("sId", "1");
		headerTxt = preferences.getString("name", "");
		// ##########################################

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

		categoryList = new ArrayList<CategoryInfo>();
		m_adapter = new ListViewAdapter(instance, R.layout.row_list,
				categoryList);
		setListAdapter(m_adapter);

		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
		iconW = bitmap.getWidth();
		iconH = bitmap.getHeight();
	}

	private Runnable returnRes = new Runnable() {
		@Override
		public void run() {
			if (categoryList != null && categoryList.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < categoryList.size(); i++) {
					m_adapter.add(categoryList.get(i));
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
		try {
			// read data
			String jsonUrl = Constants.URL_SHOPPING + "&start="+((page - 1) * Constants.ITEMS_PER_PAGE)+
					"&category="+id;
			String result = Util.getHttpData(jsonUrl);
			result = Util.toJSONString(result);
			categoryList = new ArrayList<CategoryInfo>();

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

							// content info
							String id="";
							String title="";
							String address="";
							String phone="";
							String latitude="";
							String longitude="";
							String desc="";
							String url="";

							id = jArray2.getString("id");
							title=jArray2.getString("title");
							address = jArray2.getString("address");							
							phone = jArray2.getString("phone");
							latitude=jArray2.getString("latitude");
							longitude=jArray2.getString("longitude");
							desc = jArray2.getString("desc");
							url=jArray2.getString("url");
							
							CategoryInfo cInfo = new CategoryInfo(id,title, address,phone,latitude,longitude,desc,url);
							cInfo.setIndex((j + 1));
							categoryList.add(cInfo);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}

				if (nameArray.getString(i).equalsIgnoreCase("total")) {
					totalItems = Integer.parseInt(valArray.getString(i));
					totalPage = totalItems / Constants.ITEMS_PER_PAGE;
					if (totalItems % Constants.ITEMS_PER_PAGE != 0)
						totalPage += 1;
				}
				
				startItem = ((page - 1) * 20) + 1;
				endItem = startItem + categoryList.size()-1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id_) {
		CategoryInfo cInfo = categoryList.get(position);
		nextScreen(cInfo);

	}

	public void nextScreen(CategoryInfo cInfo) {
		 categoryInfo = cInfo;
		 Intent intent = new Intent(instance, DescriptionCategoryScreen.class);
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
			tabBack.setVisibility(ImageView.INVISIBLE);
			tabNext.setVisibility(ImageView.INVISIBLE);
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
	private class ListViewAdapter extends ArrayAdapter<CategoryInfo> {
		private ArrayList<CategoryInfo> items;

		public ListViewAdapter(Context context, int textViewResourceId,
				ArrayList<CategoryInfo> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_merchant_list, null);

			}
			final CategoryInfo info = items.get(position);
			if (info != null) {
				final ImageView imView = (ImageView) v
						.findViewById(R.id.rowlistIconIView);

				if (info.getPhone().equalsIgnoreCase("")) {
					imView.setImageResource(R.drawable.icon_not_call);
				} else {
					imView.setImageResource(R.drawable.icon_call);
				}
				imView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!info.getPhone().equalsIgnoreCase(""))
							Util.makeCall(instance, info.getPhone());
					}
				});

				TextView tTView = (TextView) v.findViewById(R.id.topTView);
				TextView mTView = (TextView) v.findViewById(R.id.middleTView);
				TextView bTView = (TextView) v.findViewById(R.id.bottomTView);
				if (tTView != null) {
					tTView.setText(info.getTitle());
				}
				if (mTView != null) {
					mTView.setText(info.getAddress());
				}
				if (bTView != null) {
					bTView.setText(getText(R.string.telTitle) + info.getPhone());
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
	protected void onResume() {
		ARScreen.isMerchantList = false;
		super.onResume();
	}
}
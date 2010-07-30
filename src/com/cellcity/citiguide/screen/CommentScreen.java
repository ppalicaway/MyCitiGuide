package com.cellcity.citiguide.screen;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cellcity.citiguide.info.CommentInfo;
import com.cellcity.citiguide.info.MerchantInfo;
import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;

public  class CommentScreen extends CitiGuideListActivity implements OnClickListener {
	public static CommentScreen instance;
	private ProgressDialog progressDialog = null;
	private Runnable initR;
	private ArrayList<CommentInfo> m_orders = null;
	private ListViewAdapter m_adapter;
	private Runnable viewOrders;
	
	private EditText searchText;
	private ArrayList<CommentInfo> commentList;
	
	// share data
	private String rId;
	private String id;
	private String headerTxt;
	private MerchantInfo mInfo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.comment);
		
		instance = this;
		
		initActivity(instance, getText(R.string.search).toString());
		init();
	}
	
	public void init(){
		settingLayout();
		
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
	
	public void settingLayout(){
				
		// #### read share data ####
		SharedPreferences preferences = getSharedPreferences(
				Constants.DEFAUL_SHARE_DATA, 0);
		rId = preferences.getString("rId", "1");
		id = preferences.getString("id", "1");
		headerTxt = preferences.getString("name", "");
		// #########################
		
		headerTxt = getString(R.string.comment);
		TextView titleView = (TextView)findViewById(R.id.templateTopTitleTView);
		titleView.setVisibility(TextView.GONE);
		
		m_orders = new ArrayList<CommentInfo>();
		m_adapter = new ListViewAdapter(this, R.layout.row_list, m_orders);
		setListAdapter(m_adapter);
		
		// force show keyboard
//		Util.showKeyboard(instance);
		
		// == init button ==
		Button bCancel = (Button)findViewById(R.id.buttonCancel);
		bCancel.setOnClickListener(this);
		Button bCommet = (Button)findViewById(R.id.buttonComment);
		bCommet.setOnClickListener(this);
		
		searchText = (EditText)findViewById(R.id.searchEText);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// close soft keyboard 
		disableKeyboard();
		switch(v.getId()){
		case R.id.buttonCancel:
			instance.finish();
			break;
		case R.id.buttonComment:
			String query = searchText.getText().toString();
			SharedPreferences preferences = getSharedPreferences(Constants.DEFAUL_SHARE_DATA, 0);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("name", getString(R.string.comment));
			
			String url = "http://www.dc2go.net/api/go/setComment.php?jsoncallback=?&ci=singapore&na=" +
					"undefined&ca=undefined&did=false&cn=yahoo" +
					"&id="+mInfo.getId()+"&cm="+query;
			
			Util.getHttpData(url);
			Intent intent = new Intent(instance, CommentScreen.class);
			startActivityForResult(intent, 0);
			if (editor.commit()) {
				setResult(RESULT_OK);
			}
			instance.finish();
			break;
		}
	}
	
	public void disableKeyboard(){
		EditText editText = (EditText)findViewById(R.id.searchEText);
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
	
	
	/**
	 * ========================================================
	 */
	private Runnable returnRes = new Runnable() {
		@Override
		public void run() {
			if (commentList != null && commentList.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < commentList.size(); i++) {
					m_adapter.add(commentList.get(i));
				}
			}
			// data is null
			else {
				Util.showAlert(instance, headerTxt,
						getString(R.string.no_item_found),
						getString(R.string.ok), false);
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
			String result = Util.getHttpData("http://www.dc2go.net/api/go/getComment.php?id="+mInfo.getId()
					+"&ci=singapore&jsoncallback=");
			result = Util.toJSONString(result);

			commentList = new ArrayList<CommentInfo>();

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
							String comment = "";
							String date = "";

							comment = jArray2.getString("comment");
							date = jArray2.getString("regi_date");

							commentList.add(new CommentInfo(comment, date));
						}

					} catch (Exception e) {
						// TODO: handle exception
					}
				}

//				if (nameArray.getString(i).equalsIgnoreCase("total")) {
//					totalItems = Integer.parseInt(valArray.getString(i));
//					totalPage = totalItems / Constants.ITEMS_PER_PAGE;
//					if (totalItems % Constants.ITEMS_PER_PAGE != 0)
//						totalPage += 1;
//				}
//				if (nameArray.getString(i).equalsIgnoreCase("start")) {
//					startItem = Integer.parseInt(valArray.getString(i));
//				}
//				if (nameArray.getString(i).equalsIgnoreCase("end")) {
//					endItem = Integer.parseInt(valArray.getString(i));
//				}
			}

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
	private class ListViewAdapter extends ArrayAdapter<CommentInfo> {
		private ArrayList<CommentInfo> items;

		public ListViewAdapter(Context context, int textViewResourceId,
				ArrayList<CommentInfo> items) {
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
			final CommentInfo info = items.get(position);
			if (info != null) {

				TextView tTView = (TextView) v.findViewById(R.id.topTView);
				TextView mTView = (TextView) v.findViewById(R.id.middleTView);
				TextView bTView = (TextView) v.findViewById(R.id.bottomTView);
				if (tTView != null) {
					tTView.setSingleLine(false);
					tTView.setText(info.getComment());
				}
				mTView.setVisibility(TextView.GONE);
				
				if (bTView != null) {
					bTView.setText(info.getDate());
				}
			}
			return v;
		}
	}
	

}


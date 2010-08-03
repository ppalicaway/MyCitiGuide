package com.cellcity.citiguide.screen;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cellcity.citiguide.info.FlightInfo;
import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;

public class FlightResultScreen extends CitiGuideActivity  {
	public static FlightResultScreen instance;
	private ProgressDialog progressDialog = null;
	private Runnable initR;
	
	// share data
	private String rId;
	private String id;
	private String headerTxt;

	public static String URL = "";
	
	private TextView headerT, statusT, deptT, arrT, cTitle1, cDesc1, cTitle2, cDesc2; 
	private FlightInfo fInfo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Translucent);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.flight_result);
		
		instance = this;
		
		initActivity(instance, "");

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

	 
	private void settingLayout(){
		//#### read share data ####
		SharedPreferences preferences = getSharedPreferences(Constants.DEFAUL_SHARE_DATA, 0);
		rId = preferences.getString("rId", "1");
		id = preferences.getString("id", "1");
		headerTxt = preferences.getString("name", "");
		//#########################
		
		
		TextView titleView = (TextView)findViewById(R.id.templateTopTitleTView);
		titleView.setText(headerTxt);
		
		headerT = (TextView)findViewById(R.id.headerTV);
		statusT = (TextView)findViewById(R.id.status);
		deptT = (TextView)findViewById(R.id.departrure);
		arrT = (TextView)findViewById(R.id.arrival);
		cTitle1 = (TextView)findViewById(R.id.locatTimeTitle1);
		cDesc1 = (TextView)findViewById(R.id.locatTimeDesc1);
		cTitle2 = (TextView)findViewById(R.id.locatTimeTitle2);
		cDesc2 = (TextView)findViewById(R.id.locatTimeDesc2);
	}
	
	private Runnable returnRes = new Runnable() {
		@Override
		public void run() {
			if (fInfo != null) {
				headerT.setText(fInfo.getAirportNameOrigin() + " - " + fInfo.getAirportNameDest());
				statusT.setText(fInfo.getStatus());
				deptT.setText(fInfo.getDeparture());
				arrT.setText(fInfo.getArrival());
				cTitle1.setText("Current Local Time at "+fInfo.getAirportNameOrigin()+" : ");
				cDesc1.setText(fInfo.getDeparture_local());
				cTitle2.setText("Current Local Time at "+fInfo.getAirportNameDest()+" : ");
				cDesc2.setText(fInfo.getArrival_local());
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
							String airline = jArray2.getString("airline");
							String status = jArray2.getString("status");
							String airportCode = jArray2.getString("airportCode");
							String arrival = jArray2.getString("arrival");
							String arrival_local = jArray2.getString("arrival_local");
							String departure = jArray2.getString("departure");
							String departure_local = jArray2.getString("departure_local");
							String flightNumber = jArray2.getString("flightNumber");
							String aircraftType = jArray2.getString("aircraftType");
							String airportNameOrigin = jArray2.getString("airportNameOrigin");
							String airportNameDest = jArray2.getString("airportNameDest");
						
							fInfo = new FlightInfo(airline, status,
									airportCode, arrival, arrival_local,
									departure, departure_local, flightNumber,
									aircraftType, airportNameOrigin,
									airportNameDest);
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

}
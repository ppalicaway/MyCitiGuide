package com.cellcity.citiguide.screen;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cellcity.citiguide.adapter.Controller;
import com.cellcity.citiguide.info.IconButtonInfo;
import com.cellcity.citiguide.map.GPSLocationListener;
import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;

public class MainCitiGuideScreen extends CitiGuideActivity{
	public static MainCitiGuideScreen instance;
	private ArrayList<IconButtonInfo> iconList;
	public static LocationManager locationManager;
	private Location location;
	public static GPSLocationListener locationListener;
	
	public static double lat;
	public static double lng;
	private int dpi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Translucent);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainmenu);
		
		CitiGuideListActivity.message = "";
		CitiGuideActivity.message = "";
		CitiGuideListActivity.subject = "";
		CitiGuideActivity.subject = "";
		
//		if(instance != null)
//			instance.finish();
		instance = this;
		
		try {
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			dpi = dm.densityDpi;
			System.out.println(dm.densityDpi + " dpi");
			System.out.println(dm.xdpi + " x " + dm.ydpi);
			System.out.println(dm.widthPixels + " x " + dm.heightPixels);
		}
		catch(Exception e) {
			e.printStackTrace();
			Util.showAlert(instance, "f.y.i. Singapore", "This application only supports Android Firmware Version 1.6 and later.", "OK", true);
		}
		
		try {
        	ImageView splashImage = (ImageView)findViewById(R.id.background);
        	Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        	int newWidth = Util.getScreenWidth(instance) + 18;
        	int newHeight = Util.getScreenHeight(instance) + 10;
        	image = Util.resizeImage(image, newWidth, newHeight);
        	splashImage.setImageBitmap(image);
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        
		initActivity(instance, getText(R.string.search).toString());
		
		init();
	}
	private void init(){
		
		if(locationManager == null)
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if(locationListener == null)
			locationListener = new GPSLocationListener();
		
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 300, locationListener);
			location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		else {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 300, locationListener);
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		
		lat = Constants.SINGAPORE_LATITUDE;
		lng = Constants.SINGAPORE_LONGITUDE;
		
		try {
			lat = location.getLatitude();
			lng = location.getLongitude();
		}
		catch(Exception ex) {
			double[] latLong = Util.queryLatLong(instance);
			lat = latLong[0];
			lng = latLong[1];
			ex.printStackTrace();
		}
			
		iconList = new ArrayList<IconButtonInfo>();
		/*IconButtonInfo none1 = new IconButtonInfo(Constants.TYPE_NONE, R.drawable.pic1, R.drawable.pic1, "");
		IconButtonInfo none2 = new IconButtonInfo(Constants.TYPE_GOURMET, R.drawable.pic2, R.drawable.pic2, getString(R.string.dining));
		IconButtonInfo pub1 = new IconButtonInfo(Constants.TYPE_GOURMET, R.drawable.pic3, R.drawable.pic3, getString(R.string.dining));
		IconButtonInfo none3 = new IconButtonInfo(Constants.TYPE_SHOPPING, R.drawable.pic4, R.drawable.pic4, getString(R.string.shopping));		
		IconButtonInfo none4 = new IconButtonInfo(Constants.TYPE_SHOPPING, R.drawable.pic5, R.drawable.pic5, getString(R.string.shopping));
		
		IconButtonInfo dine1 = new IconButtonInfo(Constants.TYPE_NONE, R.drawable.pic6, R.drawable.pic6, "");
		IconButtonInfo dine2 = new IconButtonInfo(Constants.TYPE_GOURMET, R.drawable.pic7, R.drawable.pic7, getString(R.string.dining));
		IconButtonInfo pub2 = new IconButtonInfo(Constants.TYPE_GOURMET, R.drawable.pic8, R.drawable.pic8, getString(R.string.dining));
		IconButtonInfo shop1 = new IconButtonInfo(Constants.TYPE_SHOPPING, R.drawable.pic9, R.drawable.pic9, getString(R.string.shopping));
		IconButtonInfo shop2 = new IconButtonInfo(Constants.TYPE_SHOPPING, R.drawable.pic10, R.drawable.pic10, getString(R.string.shopping));
		
		IconButtonInfo dine3 = new IconButtonInfo(Constants.TYPE_PROMOTION, R.drawable.pic11, R.drawable.pic11, getString(R.string.promo));
		IconButtonInfo dine4 = new IconButtonInfo(Constants.TYPE_PROMOTION, R.drawable.pic12, R.drawable.pic12, getString(R.string.promo));
		IconButtonInfo none5 = new IconButtonInfo(Constants.TYPE_AR, R.drawable.pic13, R.drawable.pic13, "");
		IconButtonInfo shop3 = new IconButtonInfo(Constants.TYPE_MOVIE, R.drawable.pic14, R.drawable.pic14, getString(R.string.movies));
		IconButtonInfo shop4 = new IconButtonInfo(Constants.TYPE_MOVIE, R.drawable.pic15, R.drawable.pic15, getString(R.string.movies));
		
		IconButtonInfo promo1 = new IconButtonInfo(Constants.TYPE_PROMOTION, R.drawable.pic16, R.drawable.pic16, getString(R.string.promo));
		IconButtonInfo promo2 = new IconButtonInfo(Constants.TYPE_PROMOTION, R.drawable.pic17, R.drawable.pic17, getString(R.string.promo));
		IconButtonInfo ar1 = new IconButtonInfo(Constants.TYPE_AR, R.drawable.pic18, R.drawable.pic18, "");
		IconButtonInfo movie1 = new IconButtonInfo(Constants.TYPE_MOVIE, R.drawable.pic19, R.drawable.pic19, getString(R.string.movies));
		IconButtonInfo movie2 = new IconButtonInfo(Constants.TYPE_MOVIE, R.drawable.pic20, R.drawable.pic20, getString(R.string.movies));
		
		IconButtonInfo promo3 = new IconButtonInfo(Constants.TYPE_HOTEL, R.drawable.pic21, R.drawable.pic21, getString(R.string.hotels));
		IconButtonInfo hotel1 = new IconButtonInfo(Constants.TYPE_HOTEL, R.drawable.pic22, R.drawable.pic22, getString(R.string.hotels));
		IconButtonInfo none6 = new IconButtonInfo(Constants.TYPE_NONE, R.drawable.pic23, R.drawable.pic23, "");
		IconButtonInfo bank1 = new IconButtonInfo(Constants.TYPE_BANK, R.drawable.pic24, R.drawable.pic24, getString(R.string.bank));
		IconButtonInfo movie3 = new IconButtonInfo(Constants.TYPE_NONE, R.drawable.pic25, R.drawable.pic25, "");
		
		IconButtonInfo none7 = new IconButtonInfo(Constants.TYPE_HOTEL, R.drawable.pic26, R.drawable.pic26, getString(R.string.hotels));
		IconButtonInfo hotel2 = new IconButtonInfo(Constants.TYPE_HOTEL, R.drawable.pic27, R.drawable.pic27, getString(R.string.hotels));
		IconButtonInfo none8 = new IconButtonInfo(Constants.TYPE_NONE, R.drawable.pic28, R.drawable.pic28, "");
		IconButtonInfo bank2 = new IconButtonInfo(Constants.TYPE_BANK, R.drawable.pic29, R.drawable.pic29, getString(R.string.bank));
		IconButtonInfo none9 = new IconButtonInfo(Constants.TYPE_NONE, R.drawable.pic30, R.drawable.pic30, "");*/
		
		IconButtonInfo none1 = new IconButtonInfo(Constants.TYPE_NONE, R.drawable.pic1, R.drawable.pic1, "");
		IconButtonInfo none2 = new IconButtonInfo(Constants.TYPE_NONE, R.drawable.pic2, R.drawable.pic2, "");
		IconButtonInfo pub1 = new IconButtonInfo(Constants.TYPE_NONE, R.drawable.pic3, R.drawable.pic3, "");
		IconButtonInfo none3 = new IconButtonInfo(Constants.TYPE_NONE, R.drawable.pic4, R.drawable.pic4, "");		
		IconButtonInfo none4 = new IconButtonInfo(Constants.TYPE_NONE, R.drawable.pic5, R.drawable.pic5, "");
		
		IconButtonInfo dine1 = new IconButtonInfo(Constants.TYPE_NONE, R.drawable.pic6, R.drawable.pic6, "");
		IconButtonInfo dine2 = new IconButtonInfo(Constants.TYPE_ATM, R.drawable.pic7, R.drawable.pic7, getString(R.string.atm));
		IconButtonInfo pub2 = new IconButtonInfo(Constants.TYPE_ATM, R.drawable.pic8, R.drawable.pic8, getString(R.string.atm));
		IconButtonInfo shop1 = new IconButtonInfo(Constants.TYPE_ATM, R.drawable.pic9, R.drawable.pic9, getString(R.string.atm));
		IconButtonInfo shop2 = new IconButtonInfo(Constants.TYPE_NONE, R.drawable.pic10, R.drawable.pic10, "");
		
		IconButtonInfo dine3 = new IconButtonInfo(Constants.TYPE_NONE, R.drawable.pic11, R.drawable.pic11, "");
		IconButtonInfo dine4 = new IconButtonInfo(Constants.TYPE_ATM, R.drawable.pic12, R.drawable.pic12, getString(R.string.atm));
		IconButtonInfo none5 = new IconButtonInfo(Constants.TYPE_ATM, R.drawable.pic13, R.drawable.pic13, getString(R.string.atm));
		IconButtonInfo shop3 = new IconButtonInfo(Constants.TYPE_ATM, R.drawable.pic14, R.drawable.pic14, getString(R.string.atm));
		IconButtonInfo shop4 = new IconButtonInfo(Constants.TYPE_NONE, R.drawable.pic15, R.drawable.pic15, "");
		
		IconButtonInfo promo1 = new IconButtonInfo(Constants.TYPE_BANK, R.drawable.pic16, R.drawable.pic16, getString(R.string.bank));
		IconButtonInfo promo2 = new IconButtonInfo(Constants.TYPE_BANK, R.drawable.pic17, R.drawable.pic17, getString(R.string.bank));
		IconButtonInfo ar1 = new IconButtonInfo(Constants.TYPE_AR, R.drawable.pic18, R.drawable.pic18, "");
		IconButtonInfo movie1 = new IconButtonInfo(Constants.TYPE_AR, R.drawable.pic19, R.drawable.pic19, "");
		IconButtonInfo movie2 = new IconButtonInfo(Constants.TYPE_BARS, R.drawable.pic20, R.drawable.pic20, getString(R.string.pubs));
		
		IconButtonInfo promo3 = new IconButtonInfo(Constants.TYPE_GOURMET, R.drawable.pic21, R.drawable.pic21, getString(R.string.dining));
		IconButtonInfo hotel1 = new IconButtonInfo(Constants.TYPE_GOURMET, R.drawable.pic22, R.drawable.pic22, getString(R.string.dining));
		IconButtonInfo none6 = new IconButtonInfo(Constants.TYPE_AR, R.drawable.pic23, R.drawable.pic23, "");
		IconButtonInfo bank1 = new IconButtonInfo(Constants.TYPE_SHOPPING, R.drawable.pic24, R.drawable.pic24, getString(R.string.shopping));
		IconButtonInfo movie3 = new IconButtonInfo(Constants.TYPE_SHOPPING, R.drawable.pic25, R.drawable.pic25, getString(R.string.shopping));
		
		IconButtonInfo none7 = new IconButtonInfo(Constants.TYPE_GOURMET, R.drawable.pic26, R.drawable.pic26, getString(R.string.dining));
		IconButtonInfo hotel2 = new IconButtonInfo(Constants.TYPE_GOURMET, R.drawable.pic27, R.drawable.pic27, getString(R.string.dining));
		IconButtonInfo none8 = new IconButtonInfo(Constants.TYPE_NONE, R.drawable.pic28, R.drawable.pic28, "");
		IconButtonInfo bank2 = new IconButtonInfo(Constants.TYPE_SHOPPING, R.drawable.pic29, R.drawable.pic29, getString(R.string.shopping));
		IconButtonInfo none9 = new IconButtonInfo(Constants.TYPE_SHOPPING, R.drawable.pic30, R.drawable.pic30, getString(R.string.shopping));
		
		iconList.add(none1);
		iconList.add(none2);
		iconList.add(pub1);
		iconList.add(none3);
		iconList.add(none4);
		
		iconList.add(dine1);
		iconList.add(dine2);
		iconList.add(pub2);
		iconList.add(shop1);
		iconList.add(shop2);
		
		iconList.add(dine3);
		iconList.add(dine4);
		iconList.add(none5);
		iconList.add(shop3);
		iconList.add(shop4);
		
		iconList.add(promo1);
		iconList.add(promo2);
		iconList.add(ar1);
		iconList.add(movie1);
		iconList.add(movie2);
		
		iconList.add(promo3);
		iconList.add(hotel1);
		iconList.add(none6);
		iconList.add(bank1);
		iconList.add(movie3);
		
		iconList.add(none7);
		iconList.add(hotel2);
		iconList.add(none8);
		iconList.add(bank2);
		iconList.add(none9);
		
		GridView g = (GridView) findViewById(R.id.myGrid);
		//g.setPadding(0, 0, 33, 0);
		if(dpi == DisplayMetrics.DENSITY_HIGH) {
			g.setHorizontalSpacing(-66);
			g.setVerticalSpacing(-13);
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(10, 10, 0, 0);
			g.setLayoutParams(params);
		}
		g.setFocusable(false);
		g.setAdapter(new IconAdapter(this, iconList));

		Button search = (Button)findViewById(R.id.search_Button);
		search.setOnClickListener(new MenuListener());
		Button nearby = (Button)findViewById(R.id.nearbyButton);
		nearby.setOnClickListener(new MenuListener());
		Button share = (Button)findViewById(R.id.shareButton);
		share.setOnClickListener(new MenuListener());
		Button map = (Button)findViewById(R.id.map_Button);
		map.setOnClickListener(new MenuListener());
		
		HorizontalScrollView scrollView = (HorizontalScrollView)findViewById(R.id.scrollView);
		scrollView.setVisibility(HorizontalScrollView.GONE);
	}
	
	private class IconAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<IconButtonInfo> iconAdapList;

		public IconAdapter(Context c, ArrayList<IconButtonInfo> iconAdapList) {
			this.mContext = c;
			this.iconAdapList = iconAdapList;
		}

		public int getCount() {
			return iconAdapList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			final IconButtonInfo apptInfo = iconAdapList.get(position);
			ImageButton imageView = new ImageButton(mContext);
			imageView.setImageResource(apptInfo.getIconRId());
			imageView.setBackgroundColor(0x7B418C);
			
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					nextScreen(apptInfo);
				}
			});
			return imageView;
		}
		
		private void nextScreen(IconButtonInfo apptInfo) {
			SharedPreferences preferences = getSharedPreferences(Constants.DEFAUL_SHARE_DATA, 0);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("id", apptInfo.getIconId()+"");
			editor.putString("name", apptInfo.getName());
			editor.putString("rId", Controller.getResourceImageTypeId(apptInfo.getIconId())+"");
			
			switch(apptInfo.getIconId()){
			
			case Constants.TYPE_PROMOTION:
				WebScreen.URL = Constants.URL_BANK_PROMO;
				Intent webPromo = new Intent(instance, WebScreen.class);
				((Activity) mContext).startActivityForResult(webPromo, 0);
				break;
			case Constants.TYPE_ATM:
				WebScreen.URL = Constants.URL_ATM_LOCATOR;
				Intent atm = new Intent(instance, WebScreen.class);
				((Activity) mContext).startActivityForResult(atm, 0);
				break;
			case Constants.TYPE_BANK:
				WebScreen.URL = Constants.URL_BANK_HOME;
				Intent bank = new Intent(instance, WebScreen.class);
				((Activity) mContext).startActivityForResult(bank, 0);
				break;
			case Constants.TYPE_SHOPPING:
			case Constants.TYPE_GOURMET:
				Intent gourmet = new Intent(instance, CuisineListing.class);
				((Activity) mContext).startActivityForResult(gourmet, 0);
				break;
			case Constants.TYPE_HOTEL:
				Intent intent = new Intent(instance, ListingMerchantScreen.class);
				((Activity) mContext).startActivityForResult(intent, 0);
				break;			
			case Constants.TYPE_FLIGHT:
				Intent flight = new Intent(instance, FlightScreen.class);
				startActivityForResult(flight, 0);
				break;
			case Constants.TYPE_WEATHER:
				Intent weather = new Intent(instance, WeatherScreen.class);
				startActivityForResult(weather, 0);
				break;
			case Constants.TYPE_MOVIE:
				Intent movie = new Intent(instance,MovieScreen.class);
				startActivityForResult(movie,0);
				break;
			case Constants.TYPE_BARS:
				MerchantListingScreen.catKeyword = "Bars%20&%20Pubs";
				Intent shopping = new Intent(instance, MerchantListingScreen.class);
				startActivityForResult(shopping,0);
				break;
			case Constants.TYPE_NONE:
				break;
			case Constants.TYPE_AR:
				if(ARScreen.instance != null)
					ARScreen.instance.finish();				
				Intent ar = new Intent(instance, ARScreen.class);
				startActivity(ar);
				break;
			default:
				Intent inten = new Intent(instance, ListingMerchantScreen.class);
				((Activity) mContext).startActivityForResult(inten, 0);
				break;
			}
			if (editor.commit()) {
				setResult(RESULT_OK);
			}
		}
	}
	
	@Override
	protected void onPause() {
		locationManager.removeUpdates(locationListener);
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 300, locationListener);
			location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		else {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 300, locationListener);
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		
		try {
			lat = location.getLatitude();
			lng = location.getLongitude();
		}
		catch(Exception ex) {
			double[] latLong = Util.queryLatLong(instance);
			lat = latLong[0];
			lng = latLong[1];
			ex.printStackTrace();
		}
		
		ARScreen.isMerchantList = false;
		CitiGuideActivity.message = "";
		CitiGuideListActivity.message = "";
		CitiGuideActivity.subject = "";
		CitiGuideListActivity.subject = "";
		super.onResume();
	}
}
package com.cellcity.citiguide.screen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.cellcity.citiguide.ar.GetJSON;
import com.cellcity.citiguide.util.Util;

public class CitiGuideARActivity extends Activity {

	private Activity act;
	protected int menuId;
	
	public static double lat = 0.0;
	public static double longi = 0.0;
	
	public static String message = "";
	public static String subject = "";
	
	CitiGuideARActivity this_obj;
	private CategoriesPopup catDialog;
	private Menu mMenu;
	
	private static boolean isDining = true;
	private static boolean isPubs = true;
	private static boolean isShopping = true;
	
	protected void initActivity(Activity act, String title){
		this.act = act;
		this_obj = this;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.mMenu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_ar, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_filter_category:
			ARScreen.cats = ARScreen.cats.replaceAll(",", "");
			if( catDialog == null ) catDialog = new CategoriesPopup(this_obj, new CatPopOKListener(), getString(R.string.catfilter_title));
			if(!ARScreen.isMerchantList) {
				catDialog.show();
			}
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	private class CatPopOKListener implements android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch(v.getId()) {
				case R.id.btnCategoryFilter:
					ARScreen.merchantList.clear();
					String temp = "";
					if(ARScreen.cats.contains("Dining")) {
    					temp += "Dining,";
    				}
					if(ARScreen.cats.contains("Pubs")) {
    					temp += "Pubs,";
    				}
					if(ARScreen.cats.contains("Shopping")) {
    					temp += "Shopping";
    				}
					
					try {
						if(temp.charAt(temp.length()-1) == ',') {
							temp = temp.substring(0, temp.length()-1);
						}
					}
					catch(Exception e) {
						e.printStackTrace();
					}
					
					ARScreen.cats = temp;
					
					if(!ARScreen.cats.equalsIgnoreCase("")) {
						if(!ARScreen.cats.equalsIgnoreCase("")) {
		    				ARScreen.progressDialog = ProgressDialog.show(ARScreen.instance, "", ARScreen.instance.getResources().getString(R.string.retrieving_data), true);
		        			Thread t = new Thread(null, new GetJSON(), "initR");
		        			t.start();
		    			}
		    			catDialog.dismiss();
					}
					else {
						Util.showAlert(act, "f.y.i. Singapore", "Please select at least one category.", "OK", false);
					}
					
					break;
				case R.id.cbDining:
					if(!isDining && CategoriesPopup.dining.isChecked()) {
						ARScreen.cats += "Dining";
						isDining = true;
					}
					else {
						ARScreen.cats = ARScreen.cats.replaceAll("Dining", "");
						isDining = false;
					}
					break;
				case R.id.cbPubs:
					if(!isPubs && CategoriesPopup.pubs.isChecked()) {
						ARScreen.cats += "Pubs";
						isPubs = true;
					}
					else {
						ARScreen.cats = ARScreen.cats.replaceAll("Pubs", "");
						isPubs = false;
					}
					break;
				case R.id.cbShopping:
					if(!isShopping && CategoriesPopup.shopping.isChecked()) {
						ARScreen.cats += "Shopping";
						isShopping = true;
					}
					else {
						ARScreen.cats = ARScreen.cats.replaceAll("Shopping", "");
						isShopping = false;
					}
					break;
			}
		}
	}
}

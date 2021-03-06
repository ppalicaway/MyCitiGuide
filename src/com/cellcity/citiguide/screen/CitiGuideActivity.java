package com.cellcity.citiguide.screen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.cellcity.citiguide.adapter.Controller;
import com.cellcity.citiguide.util.Util;

public class CitiGuideActivity extends Activity{
	private Activity act;
	protected int menuId;
	
	public static double lat = 0.0;
	public static double longi = 0.0;
	
	public static String message = "";
	public static String subject = "";
	
	protected void initActivity(Activity act, String title){
		this.act = act;
	}
	
	// new menu
	private Menu mMenu;
	//public boolean onCreateOptionsMenu(Menu menu) {
	//	this.mMenu = menu;
	//	MenuInflater inflater = getMenuInflater();
	//	inflater.inflate(R.menu.main, menu);
	//	return true;
	//}
	
	//@Override
	//public boolean onPrepareOptionsMenu(Menu menu) {
	//	return super.onPrepareOptionsMenu(menu);
	//}
	
	//@Override
	//public boolean onMenuItemSelected(int featureId, MenuItem item) {
	//	switch (item.getItemId()) {
	//		case R.id.menu_search:
	//			//act.finish();
	//			Intent search = new Intent(act, SearchScreen.class);
	//			startActivity(search);
	//			return true;			
	//		case R.id.menu_share:
	//			new AlertDialog.Builder(act)
	//	   		//.setIcon(R.drawable.alert_dialog_icon)
	//		    		.setTitle(R.string.sharTitle)
	//		            .setPositiveButton(R.string.sms, new DialogInterface.OnClickListener() {
	//		                public void onClick(DialogInterface dialog, int whichButton) {
	//							Util.sendMessage(act, "", message);
	//		                }
	//		            })
	//		            .setNegativeButton(R.string.email, new DialogInterface.OnClickListener() {
	//		                public void onClick(DialogInterface dialog, int whichButton) {
	//							Util.sendEmail(act, "", "", subject, message);
	//		                }
	//		            })
	//		            .create().show();
	//			return true;
	//		case R.id.menu_nearby:
//	//			act.finish();
	//			Intent nearby = new Intent(act,NearbyScreen.class);
	//			startActivity(nearby);
	//			return true;			
	//		case R.id.menu_ar:
	//			if(ARScreen.instance != null)
	//				ARScreen.instance.finish();				
	//			Intent ar = new Intent(act, ARScreen.class);
	//			startActivity(ar);
	//			return true;
	//		case R.id.menu_map:
	//			Controller.displayMapScreen(act);
	//			return true;
	//		}			
	//	return super.onMenuItemSelected(featureId, item);
	//}
	
	//public boolean onMenuItemSelected(int featureId, MenuItem item) {
	//	switch (item.getItemId()) {
	//	case R.id.homeButton:
	//		act.finish();
	//		if(MainCitiGuideScreen.instance != null)
	//			MainCitiGuideScreen.instance.finish();				
	//		Intent main = new Intent(act, MainCitiGuideScreen.class);
	//		startActivity(main);
	//		break;
	//	case R.id.menu_search:
	//		//act.finish();
	//		Intent search = new Intent(act, SearchScreen.class);
	//		startActivity(search);
	//		return true;			
	//	case R.id.menu_share:
	//		new AlertDialog.Builder(act)
	//   		//.setIcon(R.drawable.alert_dialog_icon)
	//	    		.setTitle(R.string.sharTitle)
	//	            .setPositiveButton(R.string.sms, new DialogInterface.OnClickListener() {
	//	                public void onClick(DialogInterface dialog, int whichButton) {
	//						Util.sendMessage(act, "", message);
	//	                }
	//	            })
	//	            .setNegativeButton(R.string.email, new DialogInterface.OnClickListener() {
	//	                public void onClick(DialogInterface dialog, int whichButton) {
	//						Util.sendEmail(act, "", "", subject, message);
	//	                }
	//	            })
	//	            .create().show();
	//		break;
	//	case R.id.menu_nearby:
//	//		act.finish();
	//		Intent nearby = new Intent(act,NearbyScreen.class);
	//		startActivity(nearby);
	//		return true;
			
	//	case R.id.menu_map:
	//		Controller.displayMapScreen(act);
	//		break;
	//	}
	//	return super.onMenuItemSelected(featureId, item);
	//}
	
	protected class MenuListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.homeButton:
				act.finish();
				Util.closeAllInstance();
				Intent main = new Intent(act, MainCitiGuideScreen.class);
				startActivity(main);
				break;
			case R.id.search_Button:
				Intent search = new Intent(act, SearchScreen.class);
				startActivity(search);
				break;
			case R.id.nearbyButton:
				Intent nearby = new Intent(act,NewNearbyScreen.class);
				startActivity(nearby);
				break;
			case R.id.shareButton:
				new AlertDialog.Builder(act)
			    		//.setIcon(R.drawable.alert_dialog_icon)
			    		.setTitle(R.string.sharTitle)
			            .setPositiveButton(R.string.sms, new DialogInterface.OnClickListener() {
			            	public void onClick(DialogInterface dialog, int whichButton) {
			            		if(!message.equalsIgnoreCase("")) {
			            			message = "Check out this Citibank Credit Card exclusive privilege:\n\n" + message;
			            		}
								Util.sendMessage(act, "", message);
			                }
			            })
			            .setNegativeButton(R.string.email, new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int whichButton) {
			                	if(!message.equalsIgnoreCase("")) {
			            			message = "Your friend would like to recommend the following Citibank Credit Card exclusive privilege to you.\n\n" + message;
			            		}
								Util.sendEmail(act, "", "", subject, message);
			                }
			            })
			            .create().show();
				break;
			case R.id.map_Button:
				//Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + longi + "," + lat)); 
				//startActivity(navigation);
				Controller.displayMapScreen(act);
				break;
			}
		}		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}

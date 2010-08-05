package com.cellcity.citiguide.screen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.cellcity.citiguide.adapter.Controller;
import com.cellcity.citiguide.util.Util;

public class CitiGuideListActivity extends ListActivity {
	private Activity act;
	protected int menuId;
	
	public static String message = "";
	public static String subject = "";

	protected void initActivity(Activity act, String title) {
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
		//switch (item.getItemId()) {
		//case R.id.menu_directory:
		//	act.finish();
		//	Intent main = new Intent(act, MainCitiGuideScreen.class);
		//	startActivity(main);
		//	return true;
		//case R.id.homeButton:
		//	act.finish();
		//	if(MainCitiGuideScreen.instance != null)
		//		MainCitiGuideScreen.instance.finish();				
		//	Intent main = new Intent(act, MainCitiGuideScreen.class);
		//	startActivity(main);
		//	return true;
		//case R.id.menu_search:
//			act.finish();
		//	Intent search = new Intent(act, SearchScreen.class);
		//	startActivity(search);
		//	return true;
		//case R.id.menu_nearby:
//			act.finish();
		//	Intent nearby = new Intent(act, NearbyScreen.class);
		//	startActivity(nearby);
		//	return true;
		//case R.id.menu_more:
//		//	act.finish();
		//	Intent more = new Intent(act, ListingMoreScreen.class);
		//	startActivity(more);
		//	return true;
		//}
		
		//return super.onMenuItemSelected(featureId, item);
		
		//switch (item.getItemId()) {
		//	case R.id.menu_search:
		//		//act.finish();
		//		Intent search = new Intent(act, SearchScreen.class);
		//		startActivity(search);
		//		return true;			
		//	case R.id.menu_share:
		//		new AlertDialog.Builder(act)
		//		//.setIcon(R.drawable.alert_dialog_icon)
		//   	.setTitle(R.string.sharTitle)
		//        .setPositiveButton(R.string.sms, new DialogInterface.OnClickListener() {
		//        	public void onClick(DialogInterface dialog, int whichButton) {
		//        		Util.sendMessage(act, "", message);
		//            }
		//        })
		//        .setNegativeButton(R.string.email, new DialogInterface.OnClickListener() {
		//        	public void onClick(DialogInterface dialog, int whichButton) {
		//        		Util.sendEmail(act, "", "", subject, message);
		//        	}
		//        })
		//        .create().show();
		//		return true;
		//	case R.id.menu_nearby:
//		//		act.finish();
		//		Intent nearby = new Intent(act,NearbyScreen.class);
		//		startActivity(nearby);
		//		return true;
		//	case R.id.menu_ar:
		//		if(ARScreen.instance != null)
		//			ARScreen.instance.finish();				
		//		Intent ar = new Intent(act, ARScreen.class);
		//		startActivity(ar);
		//		return true;
		//	case R.id.menu_map:
		//		Controller.displayMapScreen(act);
		//		return true;
		//}			
		//return super.onMenuItemSelected(featureId, item);
		//}
	
	protected class MenuListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.homeButton:
				Util.closeAllInstance();			
				Intent main = new Intent(act, MainCitiGuideScreen.class);
				startActivity(main);
				break;
			case R.id.search_Button:
				Intent search = new Intent(act, SearchScreen.class);
				startActivity(search);
				break;
			case R.id.nearbyButton:
				Intent nearby = new Intent(act,NearbyScreen.class);
				startActivity(nearby);
				break;
			case R.id.shareButton:
				new AlertDialog.Builder(act)
			    		//.setIcon(R.drawable.alert_dialog_icon)
			    		.setTitle(R.string.sharTitle)
			            .setPositiveButton(R.string.sms, new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int whichButton) {
								Util.sendMessage(act, "", message);
			                }
			            })
			            .setNegativeButton(R.string.email, new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int whichButton) {
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
}

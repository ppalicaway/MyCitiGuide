package com.cellcity.citiguide.ar;

import com.cellcity.citiguide.screen.ARScreen;
import com.cellcity.citiguide.screen.R;
import com.cellcity.citiguide.util.Util;

public class ReturnRes implements Runnable {

	@Override
	public void run() {
		if (ARScreen.merchantList != null && ARScreen.merchantList.size() > 0) {
			ARScreen.addMerchant();				
		}
		else {
			Util.showAlert(ARScreen.instance, "", 
					ARScreen.instance.getResources().getString(R.string.no_item_found),
					ARScreen.instance.getResources().getString(R.string.ok), true);
		}
		try {
			if (ARScreen.progressDialog.isShowing())
				ARScreen.progressDialog.dismiss();
		} catch (Exception e) {
		}
	}
}

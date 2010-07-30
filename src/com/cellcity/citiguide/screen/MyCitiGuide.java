package com.cellcity.citiguide.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.cellcity.citiguide.util.Constants;

public class MyCitiGuide extends Activity {
	public static MyCitiGuide instance;
	protected boolean isActive = true;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Black_NoTitleBar);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        
        instance = this;
        splashThread.start();
    }
    
    private Thread splashThread = new Thread() {
    	@Override
		public void run() {
			try {
				int waited = 0;
				while(isActive &&(waited < Constants.SPLASH_WAIT_MILLISEC)) {
					sleep(100);
					if(isActive) {
						waited += 100;
					}
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			finally {
				finish();
				startActivity(new Intent(instance, MainCitiGuideScreen.class));
				stop();
			}
		}
    };
}
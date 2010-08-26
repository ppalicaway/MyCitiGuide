package com.cellcity.citiguide.screen;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;

public class NewNearbyScreen extends CitiGuideActivity implements OnClickListener, OnKeyListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.newnearby);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}

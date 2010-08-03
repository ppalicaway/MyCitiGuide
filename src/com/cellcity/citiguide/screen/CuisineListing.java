package com.cellcity.citiguide.screen;

import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

public class CuisineListing extends CitiGuideListActivity {
	
	public static CuisineListing instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Translucent);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listing);

		instance = this;

		initActivity(instance, "");
		
		ListView listView = (ListView)findViewById(android.R.id.list);
		listView.setDividerHeight(0);

		settingLayout();
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		
	}

	private void settingLayout() {
		// TODO Auto-generated method stub
		
	}
}

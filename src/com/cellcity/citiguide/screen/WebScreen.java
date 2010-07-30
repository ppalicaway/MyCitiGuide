package com.cellcity.citiguide.screen;

import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WebScreen extends CitiGuideActivity {

	public static WebScreen instance;
	public static String URL = "";
	private WebView webView;
	private View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bankscreen);
		
		view = (View)findViewById(R.id.bankView);
		instance = this;
		initActivity(this, "");		
		settingLayout();
	}
	
	private void settingLayout() {
		
		SharedPreferences shared = getSharedPreferences(Constants.DEFAUL_SHARE_DATA, 0);
		
		TextView titleTxt = (TextView)findViewById(R.id.templateTopTitleTView);
		titleTxt.setText(shared.getString("name", ""));
		
		webView = (WebView)findViewById(R.id.myWebView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(URL);
		webView.setWebViewClient(new CitiWebViewClient());
		registerForContextMenu(titleTxt);
		
		Button map = (Button)findViewById(R.id.mapButton);
		Button ar = (Button)findViewById(R.id.arButton);
		map.setVisibility(Button.GONE);
		ar.setVisibility(Button.GONE);
		
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.widget35);
		linearLayout.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

		Button homeButton = (Button)findViewById(R.id.homeButton);
		homeButton.setOnClickListener(new MenuListener());
	}

	private class CitiWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    	if(!url.contains("tel:")) {
	    		view.loadUrl(url);
	    	}
	    	else {
	    		Util.makeCall(instance, url);
	    	}
	    	view.showContextMenu();
	        return true;
	    }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			
			if(webView.canGoBack()) {
				webView.goBack();
			}
			else {
				instance.finish();
			}
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
}
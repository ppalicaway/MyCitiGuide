package com.cellcity.citiguide.screen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class CategoriesPopup extends Dialog {

	Context instance;
	View.OnClickListener onOK;
	String title;
	public static CheckBox dining, pubs, shopping;
	
	public CategoriesPopup(Context context, View.OnClickListener listener, String t) {
		super(context);
        instance = context;
        onOK = listener;
        title = t;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(title);
        setContentView(R.layout.categories_popup);
        
        dining = (CheckBox)findViewById(R.id.cbDining);
        dining.setOnClickListener(onOK);
        dining.setChecked(true);
        pubs = (CheckBox)findViewById(R.id.cbPubs);
        pubs.setOnClickListener(onOK);
        pubs.setChecked(true);
        shopping = (CheckBox)findViewById(R.id.cbShopping);
        shopping.setOnClickListener(onOK);
        shopping.setChecked(true);
        
        Button btnOK = (Button)findViewById(R.id.btnCategoryFilter);		
        btnOK.setOnClickListener(onOK);
	}
}

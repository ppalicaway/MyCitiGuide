package com.cellcity.citiguide.screen;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cellcity.citiguide.info.CategoryInfo;
import com.cellcity.citiguide.info.IconButtonInfo;
import com.cellcity.citiguide.util.Constants;
import com.cellcity.citiguide.util.Util;

public class DescriptionCategoryScreen extends CitiGuideActivity implements
		OnClickListener, OnTouchListener {
	public static DescriptionCategoryScreen instance;

	private ArrayList<IconButtonInfo> iconList;
	private Dialog ratingDialog, shareDialog;
	private static final int TYPE_TWITTER = 1;
	private static final int TYPE_COMMENT = 2;
	private static final int TYPE_DIRECTION = 3;
	private static final int TYPE_MAP = 4;

	private CategoryInfo categoryInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.description);

		instance = this;

		initActivity(this, "");
		init();
	}
	private void init(){
		categoryInfo = CategoryListScreen.categoryInfo;

		TextView titleTView = (TextView)findViewById(R.id.templateTopTitleTView);
		titleTView.setText(categoryInfo.getTitle());
		TextView addressTView = (TextView)findViewById(R.id.descAddrTView);
		addressTView.setText(Html.fromHtml("<font color=\"black\"></font> " + categoryInfo.getAddress()));

		TextView telTView = (TextView)findViewById(R.id.descTelTView);
		telTView.setText(getText(R.string.telTitle) + categoryInfo.getPhone());
		ImageView typeIconView = (ImageView)findViewById(R.id.typeIconIView);
		typeIconView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Util.makeCall(instance, categoryInfo.getPhone());
				}
			});	
	//	typeIconView.setImageResource(Controller.getResourceImageTypeId(merchantInfo.getgType()));
		
		TextView rateTView = (TextView)findViewById(R.id.descRatingTView);
		rateTView.setText(Constants.SPACE_HORIZONTAL+getText(R.string.ratingTitle));
		RatingBar ratingBar = (RatingBar)findViewById(R.id.indicator_ratingbar);
		ratingBar.setRating(Float.parseFloat("0"));
		ratingBar.setOnTouchListener(this);
		
		GridView g = (GridView) findViewById(R.id.descIconGrid);
		g.setFocusable(false);
		iconList = new ArrayList<IconButtonInfo>();
		ImageView lefticon = (ImageView)findViewById(R.id.lefticon);
		IconButtonInfo iconInfo1 = new IconButtonInfo(TYPE_TWITTER, R.drawable.twitter1, R.drawable.twitter2, "");
		IconButtonInfo iconInfo2 = new IconButtonInfo(TYPE_COMMENT, R.drawable.comment1, R.drawable.comment2, "");
		IconButtonInfo iconInfo3 = new IconButtonInfo(TYPE_DIRECTION, R.drawable.direction1, R.drawable.direction2, "");
		IconButtonInfo iconInfo4 = new IconButtonInfo(TYPE_MAP, R.drawable.map1, R.drawable.map2, "");		
		ImageView righticon = (ImageView)findViewById(R.id.righticon);
		iconList.add(iconInfo1);
		iconList.add(iconInfo2);
		iconList.add(iconInfo3);
		iconList.add(iconInfo4);
		
		g.setAdapter(new IconAdapter(this, iconList));		
		
		TextView descTView = (TextView)findViewById(R.id.descDetailTView);
		descTView.setText(Html.fromHtml("<font color=\"green\">Description : </font> " + categoryInfo.getDesc()));			 	    
		//TextView urlTView = (TextView)findViewById(R.id.descUrlTView);
		
	}
	/**
	 * ==========================================================
	 * inner class 
	 * display icon on screen.
	 * ==========================================================
	 * @author Administrator
	 *
	 */
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
			ImageButtonAdapter imageView = new ImageButtonAdapter(mContext, apptInfo);
			imageView.setBackgroundResource(R.color.COLOR_BG);
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					nextScreen(apptInfo);
				}
			});
			return imageView;
		}
		private void nextScreen(IconButtonInfo apptInfo){
			switch(apptInfo.getIconId()){			
			case TYPE_TWITTER:
				Intent twitter = new Intent(instance, TwitterScreen.class);
				startActivityForResult(twitter, 0);
				break;
			case TYPE_COMMENT:
				Intent comment = new Intent(instance, CommentScreen.class);
				startActivityForResult(comment, 0);
				break;
//			case TYPE_MAP:
//				//Get the current location in start-up
//				double lat = Double.parseDouble(categoryInfo.getLat());
//				double lng = Double.parseDouble(categoryInfo.getLng());
//				
//				MapLocationInfo mLocation = new MapLocationInfo(categoryInfo.getTitle(), 
//						"", categoryInfo.getAddress(), categoryInfo.getPhone(), 
//						lat, lng, R.drawable.pin_violet, null);
//				System.out.println("Result ====>>>"+categoryInfo.getgType());
//				Bitmap bitmap = BitmapFactory.decodeResource(instance.getResources(), 
//						Controller.getResourceImageTypeId(merchantInfo.getgType()));
//				mLocation.setBitmap(bitmap);
//				mLocation.setCurrentPost(true);
//				
//				//				
//				MapLocationViewer.setMapLocation(mLocation, 0, false);
//				
//			//	MapScreen.showTab = false;
//				Intent map = new Intent(instance, MapScreen.class);
//				startActivityForResult(map, 0);
//				break;
			case TYPE_DIRECTION:
				Intent direction = new Intent(instance, DirectionInputScreen.class);
				startActivityForResult(direction, 0);
				break;
			}
		}
	}
	private class ImageButtonAdapter extends ImageButton {
		private int imageW, imageH;
		private IconButtonInfo bInfo;
		private boolean isButtonPressed;
		public ImageButtonAdapter(Context context, IconButtonInfo bInfo) {
			super(context);
			// TODO Auto-generated constructor stub
			this.bInfo = bInfo;
			
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_call);
			imageW = bitmap.getWidth();
			imageH = bitmap.getHeight();
			setLayoutParams(new GridView.LayoutParams(imageW, imageH));
			setScaleType(ImageView.ScaleType.CENTER);
		}
		protected void onDraw(Canvas c) {
			if (isButtonPressed) {
				setBackgroundResource(bInfo.getIconPressRId());
//				setImageResource(bInfo.getIconPressRId());
			} else if (isFocused()) {
				// Since this Button now has no background. We must swap out the
				// image to display
				// one that indicates it has focus.
				setBackgroundResource(bInfo.getIconPressRId());
//				setImageResource(bInfo.getIconPressRId());
			} else {
				setBackgroundResource(bInfo.getIconRId());
//				setImageResource(bInfo.getIconRId());
			}
			super.onDraw(c);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// Request a redraw to update the button color
				isButtonPressed = true;
				invalidate();
			} else {
				isButtonPressed = false;

				// Requesting focus doesn't work for some reason. If you find a
				// solution to setting
				// the focus, please let me know so I can update the tutorial
				requestFocus();

				// Request a redraw to update the button color
				invalidate();
			}
			return super.onTouchEvent(event);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.buttonOK:
			ratingDialog.dismiss();
			break;
		case R.id.buttonCancel:
			ratingDialog.dismiss();
			break;
		}
		
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (v instanceof RatingBar) {
			ratingDialog = new Dialog(instance);
			ratingDialog.setTitle("Rating...");
			ratingDialog.setContentView(R.layout.dialog_rating);

			Button buttonOK = (Button) ratingDialog.findViewById(R.id.buttonOK);
			buttonOK.setOnClickListener(this);
			Button buttonCancel = (Button) ratingDialog.findViewById(R.id.buttonCancel);
			buttonCancel.setOnClickListener(this);
			
			ratingDialog.show();
		}
		return false;
	}
}

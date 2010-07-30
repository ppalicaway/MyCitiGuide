package com.cellcity.citiguide.map;

import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Style;

import com.cellcity.citiguide.screen.DescriptionScreen;
import com.cellcity.citiguide.screen.MainCitiGuideScreen;
import com.cellcity.citiguide.screen.R;
import com.cellcity.citiguide.util.Util;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class MapLocationOverlay  extends Overlay {
	private Context context;
	// Store these as global instances so we don't keep reloading every time
//	private Bitmap greenPin, violetPin, defaultPin; //redPin, greenPin, violetPin; // , shadowIcon;
	private Bitmap defaultPin, currentPin;
	private MapLocationViewer mapLocationViewer;
	private Paint innerPaint, borderPaint, textPaint;

	// The currently selected Map Location...if any is selected. This tracks
	// whether an information
	// window should be displayed & where...i.e. whether a user 'clicked' on a
	// known map location
	private MapLocationInfo selectedMapLocation;

	// draw route
	private GeoPoint gp1;
	private GeoPoint gp2;
	private int mRadius = 6;
	private int mode = 0;
	private int defaultColor;
	private boolean isShowRoute;
	private Path routePath;
    private int elemWidth, elemHeight;
    private int paddingTop;
    private Canvas canvas = new Canvas();
    private int infoWindowOffsetX;
    int infoWindowOffsetY;
    
	public MapLocationOverlay(Context context, MapLocationViewer mapLocationViewer, 
			boolean isShowRoute, int paddingTop) {
		this.context = context;
		this.mapLocationViewer = mapLocationViewer;
		this.paddingTop = paddingTop;
		
//		redPin = BitmapFactory.decodeResource(mapLocationViewer.getResources(),R.drawable.pin_red);
//		greenPin =  BitmapFactory.decodeResource(mapLocationViewer.getResources(),R.drawable.pin_green);
//		violetPin = BitmapFactory.decodeResource(mapLocationViewer.getResources(),R.drawable.pin_violet);
		
		currentPin = BitmapFactory.decodeResource(mapLocationViewer.getResources(),R.drawable.pin_violet);
		defaultPin = BitmapFactory.decodeResource(mapLocationViewer.getResources(),R.drawable.pin_1);
		
		elemWidth = Util.getScreenWidth(context);
		elemHeight = 20*4;
	}

	public MapLocationOverlay(GeoPoint gp1, GeoPoint gp2, int mode,
			int defaultColor, boolean isShowRoute) {
		this.gp1 = gp1;
		this.gp2 = gp2;
		this.mode = mode;
		this.defaultColor = defaultColor;
		this.isShowRoute = isShowRoute;
	}
	
	public MapLocationOverlay(Path routePath, int defaultColor,
			boolean isShowRoute) {
		this.routePath = routePath;
		this.defaultColor = defaultColor;
		this.isShowRoute = isShowRoute;
	}
	
	private void nextScreen(){
		DescriptionScreen.merchantInfo = selectedMapLocation.getMerchantInfo();
		Intent intent = new Intent(context, DescriptionScreen.class);
	    ((Activity) context).startActivityForResult(intent, 0);
	}
	
	@Override
	public boolean onTap(GeoPoint p, MapView mapView)  {
		//  Store whether prior popup was displayed so we can call invalidate() & remove it if necessary.
		boolean isRemovePriorPopup = selectedMapLocation != null;  

		if(selectedMapLocation != null){
			if(getHitDescription(mapView, p) && selectedMapLocation.getMerchantInfo() != null){
				System.out.println("Hit NextScreen");
				nextScreen();
			}
			else {
				System.out.println("No Hit NextScreen");
			}
		}
		
		//  Next test whether a new popup should be displayed
//		System.out.println("selectedMapLocation a = " + selectedMapLocation);
		selectedMapLocation = getHitMapLocation(mapView,p);
		
		if (isRemovePriorPopup || selectedMapLocation != null) {
			mapView.invalidate();
		}
//		System.out.println("selectedMapLocation b = " + selectedMapLocation);
		
		//  Lastly return true if we handled this onTap()
		return selectedMapLocation != null;
	}
	
	
    @Override
	public void draw(Canvas canvas, MapView	mapView, boolean shadow) {
    	this.canvas = canvas;
   		if(isShowRoute){
//   	    	drawMapPointLocations(canvas, mapView, shadow);
//   	   		drawMapPointDescription(canvas, mapView, shadow);
   			drawMapRoute(canvas, mapView, shadow);
   		}else{
   			drawMapPointLocations(canvas, mapView, shadow);
   	   		drawMapPointDescription(canvas, mapView, shadow);
   		}
    }
    
    private boolean getHitDescription(MapView mapView, GeoPoint	tapPoint){
    	RectF hitTestRecr = new RectF();
    	Point screenCoords = new Point();
    	int start_y = Util.getScreenHeight(MainCitiGuideScreen.instance) - elemHeight-80 - paddingTop;
    	
    	hitTestRecr.set(0, 0, elemWidth, elemHeight);
		//hitTestRecr.offset(0, start_y);
		hitTestRecr.offset(infoWindowOffsetX-34, infoWindowOffsetY-20);
		mapView.getProjection().toPixels(tapPoint, screenCoords);
		
//		System.out.println("hit test ====>> " + hitTestRecr.width() + " " + hitTestRecr.height());
//		System.out.println("hit test ====>> " + screenCoords.x + " " + screenCoords.y);
		
		if (hitTestRecr.contains(screenCoords.x, screenCoords.y)) {
			System.out.println("###############  HIT !!!!");
			return true;
		}
		
    	return false;
    }
    
    
    /**
     * Test whether an information balloon should be displayed or a prior balloon hidden.
     */
    private MapLocationInfo getHitMapLocation(MapView mapView, GeoPoint	tapPoint) {
    	
    	//  Track which MapLocation was hit...if any
    	MapLocationInfo hitMapLocation = null;
		
    	RectF hitTestRecr = new RectF();
		Point screenCoords = new Point();
    	Iterator<MapLocationInfo> iterator = null;
    	try{
    		iterator = mapLocationViewer.getMapLocations().iterator();
    	}catch(Exception ex){
    		System.out.println(" Press on route :: " + ex.getMessage() );
    		return null;
    	}
    	
    	while(iterator.hasNext()) {
    		MapLocationInfo testLocation = iterator.next();
    		
    		//  Translate the MapLocation's lat/long coordinates to screen coordinates
    		mapView.getProjection().toPixels(testLocation.getPoint(), screenCoords);

	    	// Create a 'hit' testing Rectangle w/size and coordinates of our icon
	    	// Set the 'hit' testing Rectangle with the size and coordinates of our on screen icon
    		hitTestRecr.set(-defaultPin.getWidth()/2,-defaultPin.getHeight(),defaultPin.getWidth()/2,0);
    		hitTestRecr.offset(screenCoords.x, screenCoords.y);

//    		System.out.println("====>> " + screenCoords.x + " " + screenCoords.y);
//    		System.out.println("rect ====>> " + hitTestRecr.width() + " " + hitTestRecr.height());
    		
    		//  Finally test for a match between our 'hit' Rectangle and the location clicked by the user
    		mapView.getProjection().toPixels(tapPoint, screenCoords);
//    		System.out.println("====>>>> " + screenCoords.x + " " + screenCoords.y);
    		if (hitTestRecr.contains(screenCoords.x, screenCoords.y)) {
    			hitMapLocation = testLocation;
    			break;
    		}
    	}
    	
    	//  Lastly clear the newMouseSelection as it has now been processed
    	tapPoint = null;
    	
    	return hitMapLocation; 
    }
    
    /**
     * =====================================================================
     * Draw point, description, route on mapview.
     * =====================================================================
     */
    /**
     * draw point
     * @param canvas
     * @param mapView
     * @param shadow
     */
    private void drawMapPointLocations(Canvas canvas, MapView	mapView, boolean shadow) {
    	
		Iterator<MapLocationInfo> iterator = mapLocationViewer.getMapLocations().iterator();
		Point screenCoords = new Point();
    	while(iterator.hasNext()) {	   
    		MapLocationInfo location = iterator.next();
    		mapView.getProjection().toPixels(location.getPoint(), screenCoords);
			
	    	if (shadow) {
	    		//  Only offset the shadow in the y-axis as the shadow is angled so the base is at x=0; 
//	    		canvas.drawBitmap(shadowIcon, screenCoords.x, screenCoords.y - shadowIcon.getHeight(),null);
	    	} else {
	    		if(location.isCurrentPost())
	    			canvas.drawBitmap(currentPin, screenCoords.x - currentPin.getWidth()/2, screenCoords.y - currentPin.getHeight(),null);
	    		else{
	    			Bitmap pin = BitmapFactory.decodeResource(mapLocationViewer.getResources(), location.getrId());
	    			canvas.drawBitmap(pin, screenCoords.x - defaultPin.getWidth()/2, screenCoords.y - defaultPin.getHeight(),null);
	    		}
	    	}
    	}
    }

    private void drawMapPointDescription(Canvas canvas, MapView	mapView, boolean shadow) {
    	
    	if ( selectedMapLocation != null) {
    		if ( shadow) {
    			//  Skip painting a shadow in this tutorial
    		} else {
				//  First determine the screen coordinates of the selected MapLocation
				Point selDestinationOffset = new Point();
				mapView.getProjection().toPixels(selectedMapLocation.getPoint(), selDestinationOffset);
		    	
		    	//  Setup the info window with the right size & location
				int INFO_WINDOW_WIDTH = 150;
				int INFO_WINDOW_HEIGHT = 30;
				RectF infoWindowRect = new RectF(0,0,INFO_WINDOW_WIDTH,INFO_WINDOW_HEIGHT);				
				infoWindowOffsetX = selDestinationOffset.x-INFO_WINDOW_WIDTH/2;
				infoWindowOffsetY = selDestinationOffset.y-INFO_WINDOW_HEIGHT-defaultPin.getHeight();
				int start_y = Util.getScreenHeight(MainCitiGuideScreen.instance) - elemHeight-80 - paddingTop;
				//infoWindowRect.offset(0, start_y);
				infoWindowRect.offset(infoWindowOffsetX, infoWindowOffsetY);

				//  Draw inner info window
//				canvas.drawRoundRect(infoWindowRect, 5, 5, getInnerPaint());
				//canvas.drawRoundRect(infoWindowRect, 5, 5, getInnerPaint());
				
				//  Draw border for info window
				//canvas.drawRoundRect(infoWindowRect, 5, 5, getBorderPaint());
				
				int TEXT_OFFSET_X = -19;
				int TEXT_OFFSET_Y = 2;
				
				Bitmap bm = BitmapFactory.decodeResource(MainCitiGuideScreen.instance.getResources(), R.drawable.bubble);
				canvas.drawBitmap(bm, infoWindowOffsetX-34, infoWindowOffsetY-20, null);
				
				canvas.drawText(selectedMapLocation.getTitle(),infoWindowOffsetX+TEXT_OFFSET_X,infoWindowOffsetY+TEXT_OFFSET_Y,getTextPaint());
				
				//  Draw the MapLocation's name
				//int TEXT_OFFSET_X = 10;
				//int TEXT_OFFSET_Y = 5;
				//int x = TEXT_OFFSET_X;
				//int y = start_y+TEXT_OFFSET_Y+20;
				//canvas.drawText(selectedMapLocation.getTitle(),x, y, FontDefault(20, Color.WHITE, false));
			
				//if(selectedMapLocation.getBitmap() != null){
				//	Bitmap bm = selectedMapLocation.getBitmap();
				//	canvas.drawBitmap(bm, x, y+bm.getHeight()/2, null);
				//	x += bm.getWidth()+5;
				//}
				//y += 25;

				//canvas.drawText(selectedMapLocation.getPhone(),x, y, FontDefault(15, Color.WHITE, false));
				//y += 20;
				//canvas.drawText(selectedMapLocation.getAddress(),x,	y, FontDefault(15, Color.WHITE, false));
    		}
    	}
    }
    
	public void drawMapRoute(Canvas canvas, MapView mapView, boolean shadow) {
		Projection projection = mapView.getProjection();
		if (shadow == false) {
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			PathEffect pEffect =  new CornerPathEffect(10);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(5);
			paint.setPathEffect(pEffect);
			
			try {
				Point point = new Point();
				projection.toPixels(gp1, point);
				Point point2 = new Point();
				projection.toPixels(gp2, point2);

				paint.setColor(defaultColor);
				canvas.drawLine(point.x, point.y, point2.x, point2.y, paint);
				System.out.println(point.x + " " + point.y + " " + point2.x
						+ " " + point2.y);
				// test
//				canvas.drawPath(routePath, paint);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	private Path makePath(MapView mapView, String[] pairs, GeoPoint startGP){
		Path path = new Path();
		Projection projection = mapView.getProjection();
		GeoPoint gp1;
		GeoPoint gp2 = startGP;
		String[] lngLat; 
		for (int i = 1; i < pairs.length; i++){
			lngLat = pairs[i].split(",");
			gp1 = gp2;
			// first:latitude, second:longitude
			gp2 = new GeoPoint(
					(int) (Double.parseDouble(lngLat[1]) * 1E6),
					(int) (Double.parseDouble(lngLat[0]) * 1E6));
			
			Point point1 = new Point();
			projection.toPixels(gp1, point1);
			Point point2 = new Point();
			projection.toPixels(gp2, point2);
			path.lineTo(point1.x, point1.y);
		}
		
		return path;
	}
	
    
    /**
     * ==================================================================
     * Paint information.
     * ==================================================================
     */
	public Paint getInnerPaint() {
		if ( innerPaint == null) {
			innerPaint = new Paint();
			innerPaint.setARGB(225, 75, 75, 75); //gray
			innerPaint.setAntiAlias(true);
		}
		return innerPaint;
	}

	public Paint getBorderPaint() {
		if ( borderPaint == null) {
			borderPaint = new Paint();
			borderPaint.setARGB(255, 255, 255, 255);
			borderPaint.setAntiAlias(true);
			borderPaint.setStyle(Style.STROKE);
			borderPaint.setStrokeWidth(2);
		}
		return borderPaint;
	}

	public Paint getTextPaint() {
		if ( textPaint == null) {
			textPaint = new Paint();
			textPaint.setARGB(255, 255, 255, 255);
			textPaint.setAntiAlias(true);
		}
		return textPaint;
	}
	
	public static Paint FontDefault(int size, int color, boolean shadow){
		Paint textPaint = new Paint();
		textPaint.setTextSize(size);
		textPaint.setColor(color);

		textPaint.setTypeface(Typeface.DEFAULT);
		textPaint.setTextAlign(Paint.Align.LEFT);
		textPaint.setAntiAlias(true);
		if(shadow)
			textPaint.setShadowLayer(2, 2, 2, color+15);
		return textPaint;
	}
}
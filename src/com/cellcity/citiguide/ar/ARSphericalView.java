package com.cellcity.citiguide.ar;

import android.content.Context;
import android.graphics.Paint;
import android.location.Location;
import android.view.View;

public class ARSphericalView extends View 
{
	public volatile float azimuth; //Angle from east
	public volatile float distance; //Distance to object
	public volatile float inclination = -1; //angle off horizon.
	public volatile Location location;
	
	public volatile int x;
	public volatile int y;
	public volatile boolean visible = false;
	
	public static Location deviceLocation;
	
	//used to compute inclination
	public static float currentAltitude = 0;
	protected Paint p = new Paint();
	
	public ARSphericalView(Context ctx) {
		super(ctx);
	}
}
package com.cellcity.citiguide.map;

import android.graphics.Bitmap;

import com.cellcity.citiguide.info.MerchantInfo2;
import com.google.android.maps.GeoPoint;

/** Class to hold our location information */
public class MapLocationInfo {

	private GeoPoint point;
	private String title;
	private String address;
	private String phone;
	private boolean isCurrentPost;
	private int rId;
	private Bitmap bitmap;
	//private MerchantInfo merchantInfo;
	private MerchantInfo2 merchantInfo;
	
	//public MapLocationInfo(String title, String desc, String address,
	//		String phone, double latitude, double longitude, int rId, MerchantInfo merchantInfo) {
	public MapLocationInfo(String title, String address,
			double latitude, double longitude, int rId, MerchantInfo2 merchantInfo) {
		this.title = title;
		this.address = address;
		//this.phone = phone;
		point = new GeoPoint((int) (latitude * 1e6), (int) (longitude * 1e6));
		this.rId = rId;
		this.merchantInfo = merchantInfo;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public MerchantInfo2 getMerchantInfo() {
		return merchantInfo;
	}

	public void setMerchantInfo(MerchantInfo2 merchantInfo) {
		this.merchantInfo = merchantInfo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public GeoPoint getPoint() {
		return point;
	}

	public boolean isCurrentPost() {
		return isCurrentPost;
	}

	public void setCurrentPost(boolean isCurrentPost) {
		this.isCurrentPost = isCurrentPost;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPoint(GeoPoint point) {
		this.point = point;
	}

	public int getrId() {
		return rId;
	}

	public void setrId(int rId) {
		this.rId = rId;
	}
	
	

}

package com.cellcity.citiguide.info;

public class MerchantInfo2 {

	private int id;
	private String image;
	private String outletName;
	private String address;
	private double latitude;
	private double longitude;
	private String distance;
	
	public MerchantInfo2() {
	}
	
	public MerchantInfo2(int id, String image, String outletName, String address, double latitude, double longitude) {
		this.id = id;
		this.image = image;
		this.outletName = outletName;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}

	public String getOutletName() {
		return outletName;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getDistance() {
		return distance;
	}
}

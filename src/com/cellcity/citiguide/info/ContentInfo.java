package com.cellcity.citiguide.info;

public class ContentInfo {
	private int gType;
	private String id, title;
	private String lat, lng;
	private String countryId, countryName, sId;
	private boolean isCheck = false;
	
	public ContentInfo(int gType, String countryId, String countryName, String sId, boolean isCheck){
		this.gType = gType;
		this.countryId = countryId;
		this.countryName = countryName;
		this.sId = sId;
	}
	
	public ContentInfo(int gType, String sId, String title){
		this.gType = gType;
		this.sId = sId;
		this.title = title;
	}

	
	public String getsId() {
		return sId;
	}

	public void setsId(String sId) {
		this.sId = sId;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}


	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public int getgType() {
		return gType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setgType(int gType) {
		this.gType = gType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	
}

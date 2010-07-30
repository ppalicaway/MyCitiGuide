package com.cellcity.citiguide.info;

public class CategoryInfo {

	private String id, title, address;
	private String phone;
	private String latitude, longitude;
	private String desc, url;
	private int index;

	public CategoryInfo(String id, String title, String address, String phone,
			String latitude, String longitude, String desc, String url) {

		this.id = id;
		this.title = title;
		this.address = address;
		this.phone = phone;
		this.desc = desc;
		this.latitude = latitude;
		this.longitude = longitude;
		this.url = url;

	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getcId() {
		return id;
	}

	public void setcId(String id) {
		this.id = id;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getWebsite() {
		return url;
	}

	public void setWebsite(String url) {
		this.url = url;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLat() {
		return latitude;
	}

	public void setLat(String latitude) {
		this.latitude = latitude;
	}

	public String getLng() {
		return longitude;
	}

	public void setLng(String longitude) {
		this.longitude = longitude;
	}

}

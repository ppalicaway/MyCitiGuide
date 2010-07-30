package com.cellcity.citiguide.info;

public class MerchantInfo1 {

	private String offerName = "";
	private String offerDescription;
	private String offerType = "";
	private String cardType = "";
	private String thumbImageName = "";
	private String merchantName = "";
	private String country = "";
	private String postalAddress = "";
	private String locationCity = "";
	private String latitude = "";
	private String longitude = "";
	private double distance = 0.0;
	private String phoneNumber = "";
	private int index;
	
	public MerchantInfo1(String merchantName, String offerDescription, String postalAddress, String latitude, String longitude) {
		this.merchantName = merchantName;
		this.offerDescription = offerDescription;
		this.postalAddress = postalAddress;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public MerchantInfo1() {
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	
	public String getOfferName() {
		return offerName;
	}
	
	public void setOfferDescription(String offerDescription) {
		this.offerDescription = offerDescription;
	}
	
	public String getOfferDescription() {
		return offerDescription;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}

	public String getOfferType() {
		return offerType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardType() {
		return cardType;
	}

	public void setThumbImageName(String thumbImageName) {
		this.thumbImageName = thumbImageName;
	}

	public String getThumbImageName() {
		return thumbImageName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	public String getLocationCity() {
		return locationCity;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
}

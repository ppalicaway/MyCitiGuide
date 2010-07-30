package com.cellcity.citiguide.parser;

import java.util.ArrayList;

import com.cellcity.citiguide.info.MerchantInfo1;

public class ParsedDataSet {

	private MerchantInfo1 merchantInfo;
	private ArrayList<MerchantInfo1> merchants = new ArrayList<MerchantInfo1>();
	
	private static String offerName = "";
	public static String offerDescription = "";
	private static String offerType = "";
	private static String cardType = "";
	private static String thumbImageName = "";
	private static String merchantName = "";
	private String country = "";
	private String postalAddress = "";
	private String locationCity = "";
	private String latitude = "";
	private String longitude = "";
	private String phoneNumber = "";
	private int numberOfResult;
	
	public void setOfferName(String offerName) {
		ParsedDataSet.offerName = offerName;
	}

	public void setOfferDescription(String offerDescription) {
		ParsedDataSet.offerDescription += offerDescription;
	}

	public void setOfferType(String offerType) {
		ParsedDataSet.offerType = offerType;
	}

	public void setCardType(String cardType) {
		ParsedDataSet.cardType = cardType;
	}

	public void setThumbImageName(String thumbImageName) {
		ParsedDataSet.thumbImageName = thumbImageName;
	}

	public void setMerchantName(String merchantName) {
		ParsedDataSet.merchantName = merchantName;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void addMerchantToList() {
		merchantInfo = new MerchantInfo1();
		
		merchantInfo.setOfferName(offerName);
		merchantInfo.setOfferDescription(offerDescription);
		merchantInfo.setOfferType(offerType);
		merchantInfo.setCardType(cardType);
		merchantInfo.setThumbImageName(thumbImageName);
		merchantInfo.setMerchantName(merchantName);
		merchantInfo.setCountry(country);
		
		merchantInfo.setPostalAddress(postalAddress);
		merchantInfo.setLocationCity(locationCity);
		merchantInfo.setLatitude(latitude);
		merchantInfo.setLongitude(longitude);
		merchantInfo.setPhoneNumber(phoneNumber);
		
		merchants.add(merchantInfo);
		
		postalAddress = "";
		locationCity = "";
		latitude = "";
		longitude = "";
	}
	
	public ArrayList<MerchantInfo1> getMerchants() {
		
		/*for(int i = 0; i < merchants.size(); i++) {
			System.out.println(merchants.get(i).getOfferName());
			System.out.println(merchants.get(i).getOfferDescription());
			System.out.println(merchants.get(i).getOfferType());
			System.out.println(merchants.get(i).getCardType());
			System.out.println(merchants.get(i).getThumbImageName());
			System.out.println(merchants.get(i).getMerchantName());
			System.out.println(merchants.get(i).getCountry());
			System.out.println(merchants.get(i).getPostalAddress());
			System.out.println(merchants.get(i).getLocationCity());
			System.out.println(merchants.get(i).getLatitude());
			System.out.println(merchants.get(i).getLongitude());
		}*/
		
		return this.merchants;
	}

	public void setNumberOfResult(int numberOfResult) {
		this.numberOfResult = numberOfResult;
	}

	public int getNumberOfResult() {
		return numberOfResult;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
}

package com.cellcity.citiguide.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.text.Html;

public class XMLParserHandler extends DefaultHandler {

	private boolean resultSet = false;
	private boolean result = false;
	private boolean offerDetails = false;
	private boolean offerName = false;
	private boolean offerDescription = false;
	private boolean offerType = false;
	private boolean cardType = false;
	private boolean thumbImageName = false;
	private boolean merchantDetails = false;
	private boolean merchantName = false;
	private boolean country = false;
	private boolean location = false;
	private boolean postalAddress = false;
	private boolean locationCity = false;
	private boolean latitude = false;
	private boolean longitude = false;
	private boolean phoneNumber = false;
	
	private ParsedDataSet myParsedExampleDataSet = new ParsedDataSet();
	
	public ParsedDataSet getParsedData() {
        return this.myParsedExampleDataSet;
	}
	
	@Override
	public void startDocument() throws SAXException {
		this.myParsedExampleDataSet = new ParsedDataSet();
	}
	
	@Override
	public void endDocument() throws SAXException {
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(localName.equals("ResultSet")) {
			String attrValue = attributes.getValue("totalResultsAvailable");
			int resultNum = Integer.parseInt(attrValue);
			myParsedExampleDataSet.setNumberOfResult(resultNum);
			this.resultSet = true;
        } else if (localName.equals("Result")) {
        	this.result = true;
        }else if (localName.equals("OfferDetails")) {
          	this.offerDetails = true;
        }else if (localName.equals("OfferName")) {
			this.offerName = true;
        }else if (localName.equals("OfferDescription")) {
         	this.offerDescription = true;
        }else if (localName.equals("OfferType")) {
         	this.offerType = true;
        }else if (localName.equals("CardType")) {
        	this.cardType = true;
        }else if (localName.equals("ThumbImageName")) {
        	this.thumbImageName = true;
        }else if (localName.equals("MerchantDetails")) {
        	this.merchantDetails = true;
        }else if (localName.equals("MerchantName")) {
        	this.merchantName = true;
        }else if (localName.equals("Country")) {
        	this.country = true;
        }else if (localName.equals("Location")) {
        	this.location = true;
        }else if (localName.equals("PostalAddress")) {
        	this.postalAddress = true;
        }else if (localName.equals("LocationCity")) {
        	this.locationCity = true;
        }else if (localName.equals("Latitude")) {
        	this.latitude = true;
        }else if (localName.equals("Longtitude")) {
        	this.longitude = true;
        }else if (localName.equals("PhoneNumber")) {
        	this.phoneNumber = true;
        }
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(localName.equals("ResultSet")) {
			this.resultSet = false;
        } else if (localName.equals("Result")) {
        	ParsedDataSet.offerDescription = "";
        	this.result = false;
        }else if (localName.equals("OfferDetails")) {
          	this.offerDetails = false;
        }else if (localName.equals("OfferName")) {
		   	this.offerName = false;
        }else if (localName.equals("OfferDescription")) {
        	this.offerDescription = false;
        }else if (localName.equals("OfferType")) {
         	this.offerType = false;
        }else if (localName.equals("CardType")) {
        	this.cardType = false;
        }else if (localName.equals("ThumbImageName")) {
        	this.thumbImageName = false;
        }else if (localName.equals("MerchantDetails")) {
        	this.merchantDetails = false;
        }else if (localName.equals("MerchantName")) {
        	this.merchantName = false;
        }else if (localName.equals("Country")) {
        	this.country = false;
        }else if (localName.equals("Location")) {
        	this.location = false;
        }else if (localName.equals("PostalAddress")) {
        	this.postalAddress = false;
        }else if (localName.equals("LocationCity")) {
        	this.locationCity = false;
        }else if (localName.equals("Latitude")) {
        	this.latitude = false;
        }else if (localName.equals("Longtitude")) {
        	this.longitude = false;
        }else if (localName.equals("PhoneNumber")) {
        	this.phoneNumber = false;
        }
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(this.offerName){
			myParsedExampleDataSet.setOfferName(Html.fromHtml(new String(ch, start, length)).toString());
        }
		if(this.offerDescription){
			//myParsedExampleDataSet.setOfferDescription(Html.fromHtml(new String(ch, start, length)).toString());
			myParsedExampleDataSet.setOfferDescription(new String(ch, start, length).toString());
        }
		if(this.offerType){
    		myParsedExampleDataSet.setOfferType(Html.fromHtml(new String(ch, start, length)).toString());
        }
		if(this.cardType){
    		myParsedExampleDataSet.setCardType(Html.fromHtml(new String(ch, start, length)).toString());
        }
		if(this.thumbImageName){
    		myParsedExampleDataSet.setThumbImageName(Html.fromHtml(new String(ch, start, length)).toString());
        }
		if(this.merchantName){
			myParsedExampleDataSet.setMerchantName(Html.fromHtml(new String(ch, start, length)).toString());
        }
		if(this.country){
    		myParsedExampleDataSet.setCountry(Html.fromHtml(new String(ch, start, length)).toString());
        }
		if(this.postalAddress){
    		myParsedExampleDataSet.setPostalAddress(Html.fromHtml(new String(ch, start, length)).toString());
        }
		if(this.locationCity){
    		myParsedExampleDataSet.setLocationCity(Html.fromHtml(new String(ch, start, length)).toString());
        }
		if(this.latitude){
    		myParsedExampleDataSet.setLatitude(Html.fromHtml(new String(ch, start, length)).toString());
        }
		if(this.longitude){
    		myParsedExampleDataSet.setLongitude(Html.fromHtml(new String(ch, start, length)).toString());
        }
		if(this.phoneNumber){
    		myParsedExampleDataSet.setPhoneNumber(new String(ch, start, length).toString());
    		myParsedExampleDataSet.addMerchantToList();
        }
	}
}
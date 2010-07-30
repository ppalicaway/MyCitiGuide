package com.cellcity.citiguide.info;

public class TheatreInfo {
	private String tName;
	private String tAddress;
	private String tTime;
	
	public TheatreInfo(String tName, String tAddress, String tTime){
		this.tName = tName;
		this.tAddress = tAddress;
		this.tTime = tTime;
	}

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	public String gettAddress() {
		return tAddress;
	}

	public void settAddress(String tAddress) {
		this.tAddress = tAddress;
	}

	public String gettTime() {
		return tTime;
	}

	public void settTime(String tTime) {
		this.tTime = tTime;
	}
	
	

}

package com.cellcity.citiguide.info;

public class WeatherInfo {
	private String icon;
	private String location;
	private String description;
	private String temperature;
	
	private String weekday;
	private String date;
	private String high_temp;
	private String low_temp;
	private String uv;
	private String uv_index;

	public WeatherInfo(String icon, String location, String description,
			String temperature) {
		this.icon = icon;
		this.location = location;
		this.description = description;
		this.temperature = temperature;
	}
	
	public WeatherInfo(String icon, String weekday, String date, String high_temp,
			String low_temp, String uv, String uv_index, String description){
		this.icon = icon;
		this.weekday = weekday;
		this.date =date;
		this.high_temp = high_temp;
		this.low_temp = low_temp;
		this.uv = uv;
		this.uv_index = uv_index;
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHigh_temp() {
		return high_temp;
	}

	public void setHigh_temp(String highTemp) {
		high_temp = highTemp;
	}

	public String getLow_temp() {
		return low_temp;
	}

	public void setLow_temp(String lowTemp) {
		low_temp = lowTemp;
	}

	public String getUv() {
		return uv;
	}

	public void setUv(String uv) {
		this.uv = uv;
	}

	public String getUv_index() {
		return uv_index;
	}

	public void setUv_index(String uvIndex) {
		uv_index = uvIndex;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

}


package com.cellcity.citiguide.info;

public class FlightInfo {
	private String airline;
	private String status;
	private String airportCode;
	private String arrival;
	private String arrival_local;
	private String departure;
	private String departure_local;
	private String flightNumber;
	private String aircraftType;
	private String airportNameOrigin;
	private String airportNameDest;
	
	public FlightInfo(String airline, String status, String airportCode, String arrival,
			String arrival_local, String departure, String departure_local, String flightNumber,
			String aircraftType, String airportNameOrigin, String airportNameDest){
		this.airline = airline;
		this.status = status;
		this.airportCode = airportCode;
		this.arrival = arrival;
		this.arrival_local = arrival_local;
		this.departure = departure;
		this.departure_local = departure_local;
		this.flightNumber = flightNumber;
		this.aircraftType = aircraftType;
		this.airportNameOrigin = airportNameOrigin;
		this.airportNameDest = airportNameDest;
		
	}
	
	public String getAirline() {
		return airline;
	}
	public void setAirline(String airline) {
		this.airline = airline;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAirportCode() {
		return airportCode;
	}
	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}
	public String getArrival() {
		return arrival;
	}
	public void setArrival(String arrival) {
		this.arrival = arrival;
	}
	public String getArrival_local() {
		return arrival_local;
	}
	public void setArrival_local(String arrivalLocal) {
		arrival_local = arrivalLocal;
	}
	public String getDeparture() {
		return departure;
	}
	public void setDeparture(String departure) {
		this.departure = departure;
	}
	public String getDeparture_local() {
		return departure_local;
	}
	public void setDeparture_local(String departureLocal) {
		departure_local = departureLocal;
	}
	public String getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	public String getAircraftType() {
		return aircraftType;
	}
	public void setAircraftType(String aircraftType) {
		this.aircraftType = aircraftType;
	}
	public String getAirportNameOrigin() {
		return airportNameOrigin;
	}
	public void setAirportNameOrigin(String airportNameOrigin) {
		this.airportNameOrigin = airportNameOrigin;
	}
	public String getAirportNameDest() {
		return airportNameDest;
	}
	public void setAirportNameDest(String airportNameDest) {
		this.airportNameDest = airportNameDest;
	}
	
	
	
	
}

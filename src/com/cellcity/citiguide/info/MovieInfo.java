package com.cellcity.citiguide.info;

import java.util.ArrayList;

public class MovieInfo {
	
	private String movieName;
	private String theatreName;
	private String address;
	private String showtimes;
	
	private ArrayList theatreList;
	
	public MovieInfo(String movieName, String theatreName, String address, String showtimes){
		
		this.movieName= movieName;
		this.theatreName=theatreName;
		this.address=address;
		this.showtimes=showtimes;
	}
	
	public MovieInfo(String movieName, ArrayList theatreList){
		this.movieName = movieName;
		this.theatreList = theatreList;
	}
	
	public String getMovieName(){
		return movieName;
	}
	
	public String getShowtimes() {
		return showtimes;
	}

	public void setShowtimes(String showtimes) {
		this.showtimes = showtimes;
	}

	public ArrayList getTheatreList() {
		return theatreList;
	}

	public void setTheatreList(ArrayList theatreList) {
		this.theatreList = theatreList;
	}

	public void setMovieName(String movieName){
		this.movieName= movieName;
	}
	
	public String getTheatreName(){
		return theatreName;
	}
	
	public void setTheatreName(String theareName){
		this.theatreName= theatreName;		
	}
	
	public String getAddress(){
		return address;
	}
	
	public void setAddress(String address){
		this.address=address;
	}
	
	public String getShowTimes(){
		return showtimes;
	}
	
	public void setShowTimes(String showtimes){
		this.showtimes=showtimes;
	}
}

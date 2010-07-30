package com.cellcity.citiguide.info;

public class CommentInfo {
	private String comment;
	private String date;
	
	public CommentInfo(String comment, String date){
		this.comment = comment;
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
}


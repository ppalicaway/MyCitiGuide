package com.cellcity.citiguide.info;

public class TwitterInfo {
	String profile_image_url = "";
	String created_at = "";
	String from_user = "";
	String text = "";
	String index;

	public TwitterInfo(String index, String profile_image_url, String create_at,
			String from_user, String text) {
		this.index = index;
		this.profile_image_url = profile_image_url;
		this.created_at = create_at;
		this.from_user = from_user;
		this.text = text;
	}
	
	

	public String getIndex() {
		return index;
	}



	public void setIndex(String index) {
		this.index = index;
	}



	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profileImageUrl) {
		profile_image_url = profileImageUrl;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String createdAt) {
		created_at = createdAt;
	}

	public String getFrom_user() {
		return from_user;
	}

	public void setFrom_user(String fromUser) {
		from_user = fromUser;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}


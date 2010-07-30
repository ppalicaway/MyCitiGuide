package com.cellcity.citiguide.info;

public class IconButtonInfo {
	private int iconRId;
	private int iconPressRId;
	private int iconId;
	private String name;

	public IconButtonInfo(int iconId, int iconRId, int iconPressRId, String name){
		this.iconId = iconId;
		this.iconPressRId = iconPressRId;
		this.iconRId = iconRId;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getIconRId() {
		return iconRId;
	}

	public void setIconRId(int iconRId) {
		this.iconRId = iconRId;
	}

	public int getIconPressRId() {
		return iconPressRId;
	}

	public void setIconPressRId(int iconPressRId) {
		this.iconPressRId = iconPressRId;
	}

	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}
	
	
}

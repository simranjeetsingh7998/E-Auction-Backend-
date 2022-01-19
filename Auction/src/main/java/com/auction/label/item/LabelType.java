package com.auction.label.item;

public enum LabelType {
	
	STANDARD("Standard"),CUSTOM("Custom");
	
	private String label;
	
	private LabelType(String labelType) {
		this.label = labelType;
	}
	
	public String getLabelType() {
		return label;
	}
	

}

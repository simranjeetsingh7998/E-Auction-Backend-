package com.auction.user;

public enum RoleEnum {
      ADMIN("Admin"),BIDDER("Bidder");

	 private String role;
	 private RoleEnum(String role) {
		 this.role = role;
	 }
	 
	 public String getRole() {
		return role;
	}
}

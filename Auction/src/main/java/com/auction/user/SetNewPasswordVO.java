package com.auction.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SetNewPasswordVO {
	
	@JsonProperty("new_password")
	private String newPassword;
	
//	@JsonProperty("email")
//	private String email;
	
	@JsonProperty("mobile_number")
	private String mobile_number;
	
	@JsonProperty("otp")
	private String otp;
	
	@JsonProperty("confirmpassword")
	private String confirmpassword;

	
}

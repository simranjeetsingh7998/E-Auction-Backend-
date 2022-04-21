package com.auction.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SetNewPasswordVO {
	
	@JsonProperty("new_password")
	private String newPassword;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("otp")
	private String otp;

	
}

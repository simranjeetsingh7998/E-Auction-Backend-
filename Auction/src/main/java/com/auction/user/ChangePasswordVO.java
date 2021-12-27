package com.auction.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChangePasswordVO {

	@JsonProperty("old_password")
	private String oldPassword;

	@JsonProperty("new_password")
	private String newPassword;

}

package com.auction.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
class Login {
	
	@Schema(defaultValue = "md@gmail.com")
	private String email;
	@Schema(defaultValue = "manojd")
	private String password;
	@JsonProperty("force_logout")
	private boolean forceLogout;

}

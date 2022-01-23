package com.auction.screen.mapping;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ScreenRoleMappingVO {
	
	@Min(value = 1, message = "{screen.role.mapping.roleId.min}")
	@JsonProperty("role_id")
	private Integer roleId;
	
	@Size(min = 1, message = "{screen.role.mapping.screensId.size}")
	@JsonProperty("screens")
	private List<ScreenVO> screens = new ArrayList<>();

}


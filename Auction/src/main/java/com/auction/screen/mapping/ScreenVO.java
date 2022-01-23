package com.auction.screen.mapping;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ScreenVO {
	
	private Integer id;
	
	@JsonProperty("route")
	private String url;
	
	@NotNull(message = "{screen.name.required}")
	private String name;
	
	@NotNull(message = "{screen.heading.required}")
	private String heading;
	
	@JsonProperty("class")
	private String cssClass;
	
	@JsonProperty("parent_menu_id")
	private Integer parentMenuId;
	
	@JsonProperty("mapping_status")
	private boolean mappingStatus;
	
	@JsonProperty("mapped_id")
	private Integer mappedId;
	
	@JsonProperty("subMenu")
	private List<ScreenVO> screenVOs = new ArrayList<>();

	public Screen screenVOToScreen() {
		 Screen screen = new Screen();
		 screen.setId(id);
		 screen.setCssClass(cssClass);
		 screen.setHeading(heading);
		 screen.setName(name);
		 screen.setMenuId(parentMenuId);
		 screen.setUrl(url);
		return screen; 
	}

}

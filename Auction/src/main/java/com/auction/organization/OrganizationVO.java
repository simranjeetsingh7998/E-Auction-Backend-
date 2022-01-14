package com.auction.organization;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrganizationVO {
	
	private Integer id;
	@Schema(defaultValue = "HSIIDC", description = "enter organization name")
	@NotBlank(message = "{organizantionName.required}")
	private String name;
	@JsonProperty("active")
	private boolean isActive;
	
    public Organization organizationVOToOrganization() {
    	Organization organization = new Organization();
    	 organization.setActive(isActive);
    	 organization.setOrgName(name);
    	 organization.setId(id);
    	return organization; 
    }

}

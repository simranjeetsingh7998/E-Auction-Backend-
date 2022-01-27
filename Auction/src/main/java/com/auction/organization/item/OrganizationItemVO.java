package com.auction.organization.item;

import javax.validation.constraints.NotBlank;

import com.auction.organization.item.label.master.ItemLabelMasterVO;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrganizationItemVO {
	
	private Long id;
	@Schema(defaultValue = "Sonipat", description = "enter value of Organization item")
	@NotBlank(message = "{itemValue.required}")
	@JsonProperty("value")
	private String itemValue;
	@JsonProperty("parent_item_id")
	private Long itemId;
    
	@JsonProperty("item_label_master")
	private ItemLabelMasterVO itemLabelMasterVO;
	
	@JsonProperty("active")
	private boolean isActive;
	
    public OrganizationItem organizationItemVOToOrganizationItem() {
    	 OrganizationItem organizationItem = new OrganizationItem();
    	 organizationItem.setActive(isActive);
    	 organizationItem.setItemValue(itemValue);
    	 organizationItem.setId(id);
    	 organizationItem.setItemId(itemId);
    	return organizationItem; 
    }

}

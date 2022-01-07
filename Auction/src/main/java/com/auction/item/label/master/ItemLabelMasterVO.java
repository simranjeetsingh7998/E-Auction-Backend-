package com.auction.item.label.master;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ItemLabelMasterVO {
	
	private Integer id;
	@Schema(defaultValue = "Division", description = "enter Item Label")
	@NotBlank(message = "{itemLabelMaster.required}")
	@JsonProperty("label_name")
	private String labelName;
	@JsonProperty("active")
	private boolean isActive;
	
    public ItemLabelMaster itemLabelMasterVOToItemLabelMaster() {
    	 ItemLabelMaster itemLabelMaster = new ItemLabelMaster();
    	 itemLabelMaster.setActive(isActive);
    	 itemLabelMaster.setItemLabel(labelName);
    	 itemLabelMaster.setId(id);
    	return itemLabelMaster; 
    }

}

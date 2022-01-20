package com.auction.organization.item.label.master;

import java.util.Objects;

import javax.validation.constraints.NotBlank;

import com.auction.organization.OrganizationVO;
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
//	@Schema(defaultValue = "1", description = "enter Item Label Order")
//	@Min(message = "{itemLabelMasterOrder.required}",value = 1)
//	@JsonProperty("order")
//	private Integer order;
	@JsonProperty("parent_item_label_id")
	private Integer parentItemLabelMasterId;
	@JsonProperty("active")
	private boolean isActive;
    @JsonProperty("organization")
	private OrganizationVO organizationVO;
	
    public ItemLabelMaster itemLabelMasterVOToItemLabelMaster() {
    	 ItemLabelMaster itemLabelMaster = new ItemLabelMaster();
    	 itemLabelMaster.setActive(isActive);
    	 itemLabelMaster.setItemLabel(labelName);
    	 if(!Objects.isNull(parentItemLabelMasterId) && parentItemLabelMasterId !=0)
    	           itemLabelMaster.setParentItemLabelMasterId(parentItemLabelMasterId);
    	// itemLabelMaster.setLabelOrder(order);
    	 itemLabelMaster.setId(id);
    	return itemLabelMaster; 
    }

}

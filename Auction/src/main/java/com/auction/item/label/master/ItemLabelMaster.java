package com.auction.item.label.master;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Entity
@Table(name = "itemLabelMaster", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"itemLabel"})
})
@Data
public class ItemLabelMaster implements Serializable {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = -8411326446182136958L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 50)
	private String itemLabel;
	private boolean isActive;
	
    public ItemLabelMasterVO itemLabelMasterToItemLabelMasterVO() {
   	 ItemLabelMasterVO itemLabelMasterVO = new ItemLabelMasterVO();
   	itemLabelMasterVO.setActive(isActive);
   	itemLabelMasterVO.setLabelName(itemLabel);
   	itemLabelMasterVO.setId(id);
   	return itemLabelMasterVO; 
   }

}

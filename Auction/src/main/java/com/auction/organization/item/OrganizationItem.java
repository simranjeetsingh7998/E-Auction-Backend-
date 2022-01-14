package com.auction.organization.item;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.auction.organization.item.label.master.ItemLabelMaster;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "organizationItem")
@Data
@EqualsAndHashCode(of = {"id","itemValue"})
public class OrganizationItem implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1601115037102857855L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 50)
	private String itemValue;
	@ManyToOne(fetch =  FetchType.LAZY, cascade = CascadeType.MERGE)
	private ItemLabelMaster itemLabelMaster;
	private boolean isActive;
	
    public OrganizationItemVO organizationItemToOrganizationItemVO() {
   	 OrganizationItemVO organizationItemVO = new OrganizationItemVO();
   	 organizationItemVO.setActive(isActive);
   	 organizationItemVO.setItemValue(itemValue);
   	 organizationItemVO.setId(id);
   	return organizationItemVO; 
   }

}

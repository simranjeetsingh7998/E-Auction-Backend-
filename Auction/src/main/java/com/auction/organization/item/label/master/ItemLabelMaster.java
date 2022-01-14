package com.auction.organization.item.label.master;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.auction.organization.Organization;
import com.auction.organization.item.OrganizationItem;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "itemLabelMaster", indexes =   {
		@Index(columnList  = "itemLabel")
})
@Data
@EqualsAndHashCode(of = {"id"})
public class ItemLabelMaster implements Serializable {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 881408857347796529L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 50)
	private String itemLabel;
	private Integer labelOrder;
	private boolean isActive;
	
	@ManyToOne(fetch =  FetchType.LAZY)
	private Organization organization;
	
	@OneToMany(mappedBy = "itemLabelMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrganizationItem> organizationItems = new HashSet<>();
	
	public void addOrganizationItem(OrganizationItem organizationItem) {
		  this.organizationItems.add(organizationItem);
		  organizationItem.setItemLabelMaster(this);
	}
	
    public ItemLabelMasterVO itemLabelMasterToItemLabelMasterVO() {
   	 ItemLabelMasterVO itemLabelMasterVO = new ItemLabelMasterVO();
   	 itemLabelMasterVO.setActive(isActive);
   	 itemLabelMasterVO.setLabelName(itemLabel);
   	 itemLabelMasterVO.setOrder(labelOrder);
   	 itemLabelMasterVO.setId(id);
   	return itemLabelMasterVO;
   }

}

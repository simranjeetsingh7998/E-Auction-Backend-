package com.auction.organization.item;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.auction.organization.item.label.master.ItemLabelMaster;
import com.auction.preparation.AuctionItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString.Exclude;

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
	private Long id;
	@Column(length = 50)
	private String itemValue;
    private Long itemId;
    private Double reservePrice;
    private Double emd;
	@ManyToOne(fetch =  FetchType.LAZY, cascade = CascadeType.PERSIST)
	@Exclude
	private ItemLabelMaster itemLabelMaster;
	
	@OneToMany(mappedBy = "organizationItem", cascade = CascadeType.ALL, orphanRemoval = false)
	private Set<AuctionItem>  auctionItems = new HashSet<>();
	
	private boolean isActive;
	
    public OrganizationItemVO organizationItemToOrganizationItemVO() {
   	 OrganizationItemVO organizationItemVO = new OrganizationItemVO();
   	 organizationItemVO.setActive(isActive);
   	 organizationItemVO.setItemValue(itemValue);
   	 organizationItemVO.setId(id);
   	 organizationItemVO.setItemId(itemId);
   	 organizationItemVO.setReservePrice(reservePrice);
   	 organizationItemVO.setEmd(emd);
   	return organizationItemVO; 
   }

}

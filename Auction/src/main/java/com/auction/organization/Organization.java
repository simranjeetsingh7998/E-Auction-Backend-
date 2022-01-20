package com.auction.organization;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.auction.organization.item.label.master.ItemLabelMaster;
import com.auction.user.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString.Exclude;

@Entity
@Table(name = "organizations", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"orgName"})
})
@Data
@EqualsAndHashCode(of = {"id"})
public class Organization implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4249810134111322027L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String orgName;
	private boolean isActive;
	
	@Exclude
	@OneToMany(mappedBy = "organization", cascade =  CascadeType.ALL, orphanRemoval =  true)
	private Set<ItemLabelMaster> itemLabelMasters = new HashSet<>();
	
	@Exclude
	@OneToMany(mappedBy = "organization", cascade =  CascadeType.MERGE, orphanRemoval =  true)
	private Set<User> users = new HashSet<>();
	
    public OrganizationVO organizationToOrganizationVO() {
    	OrganizationVO organizationVO = new OrganizationVO();
    	 organizationVO.setActive(isActive);
    	 organizationVO.setName(orgName);
    	 organizationVO.setId(id);
    	return organizationVO; 
    }

}

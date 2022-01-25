package com.auction.screen.mapping;

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

import lombok.Data;
import lombok.ToString.Exclude;

@Entity
@Table(name = "screens")
@Data
public class Screen implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -524087121920193645L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String url;
	
	private String name;
	
	private String heading;
	
	private String cssClass;
	
	private Integer menuId;
	
	@Exclude
	@OneToMany(mappedBy = "screen", cascade = {CascadeType.PERSIST}, orphanRemoval = true)
	private Set<ScreenRoleMapping> screenRoleMappings = new HashSet<>();
	
	public ScreenVO screenToScreenVO() {
		 ScreenVO screenVO = new ScreenVO();
		 screenVO.setId(id);
		 screenVO.setCssClass(cssClass);
		 screenVO.setHeading(heading);
		 screenVO.setName(name);
		 screenVO.setParentMenuId(menuId);
		 screenVO.setUrl(url);
		return screenVO; 
	}

}

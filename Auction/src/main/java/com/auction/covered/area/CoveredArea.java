package com.auction.covered.area;

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
@Table(name = "coveredArea", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"area"})
})
@Data
public class CoveredArea implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1180376937806008991L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 50)
	private String area;
	private boolean isActive;
	
    public CoveredAreaVO coveredAreaToCoveredAreaVO() {
   	 CoveredAreaVO coveredAreaVO = new CoveredAreaVO();
   	 coveredAreaVO.setActive(isActive);
   	 coveredAreaVO.setArea(area);
   	 coveredAreaVO.setId(id);
   	return coveredAreaVO; 
   }

}

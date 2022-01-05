package com.auction.division;

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
@Table(name = "division", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"divisionName"})
})
@Data
public class Division implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1210861967284891029L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 50)
	private String divisionName;
	private boolean isActive;
	
    public DivisionVO divisionToDivisionVO() {
   	 DivisionVO divisionVO = new DivisionVO();
   	 divisionVO.setActive(isActive);
   	 divisionVO.setDivisionName(divisionName);
   	 divisionVO.setId(id);
   	return divisionVO; 
   }

}

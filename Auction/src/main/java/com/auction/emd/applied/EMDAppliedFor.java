package com.auction.emd.applied;

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
@Table(name = "emdAppliedFor", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"emdFor"})
})
@Data
public class EMDAppliedFor implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1134272945584567100L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 30)
	private String emdFor;
	private boolean isActive;
	
	public EMDAppliedForVO emdAppliedForToEMDAppliedForVO() {
		 EMDAppliedForVO appliedForVO = new EMDAppliedForVO();
		 appliedForVO.setActive(isActive);
		 appliedForVO.setEmdAppliedFor(emdFor);
		 appliedForVO.setId(id);
		return appliedForVO; 
	}


}

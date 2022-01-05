package com.auction.sector;

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
@Table(name = "sector", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"sectorName"})
})
@Data
public class Sector implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 328060197074252259L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 50)
	private String sectorName;
	private boolean isActive;
	
    public SectorVO sectorToSectorVO() {
   	 SectorVO sectorVO = new SectorVO();
   	 sectorVO.setActive(isActive);
   	 sectorVO.setSectorName(sectorName);
   	 sectorVO.setId(id);
   	return sectorVO; 
   }

}

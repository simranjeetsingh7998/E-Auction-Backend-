package com.auction.station;

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
@Table(name = "station", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"stationName"})
})
@Data
public class Station implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 328060197074252259L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 50)
	private String stationName;
	private boolean isActive;
	
    public StationVO stationToStationVO() {
   	 StationVO stationVO = new StationVO();
   	 stationVO.setActive(isActive);
   	 stationVO.setStationName(stationName);
   	 stationVO.setId(id);
   	return stationVO; 
   }

}

package com.auction.event.processing.fee.mode;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.auction.preparation.AuctionPreparation;

import lombok.Data;

@Entity
@Table(name = "eventProcessingFeeMode", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"epfMode"})
})
@Data
public class EventProcessingFeeMode implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8958296508041872538L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 30)
	private String epfMode;
	private boolean isActive;
	
	@OneToMany(mappedBy = "eventProcessingFeeMode", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<AuctionPreparation> auctionPreparations = new HashSet<>();
	
    public EventProcessingFeeModeVO eventProcessingFeeModeToEventProcessingFeeModeVO() {
   	 EventProcessingFeeModeVO eventProcessingFeeModeVO = new EventProcessingFeeModeVO();
   	 eventProcessingFeeModeVO.setActive(isActive);
   	 eventProcessingFeeModeVO.setEpfMode(epfMode);
   	 eventProcessingFeeModeVO.setId(id);
   	return eventProcessingFeeModeVO; 
   }


}

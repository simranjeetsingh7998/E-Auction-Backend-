package com.auction.process;

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
@Table(name = "auctionProcess", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"process"})
})
@Data
public class AuctionProcess implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 4998461684123428552L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 30)
	private String process;
	private boolean isActive;
	
	@OneToMany(mappedBy = "auctionProcess", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<AuctionPreparation> auctionPreparations = new HashSet<>();
	
	public AuctionProcessVO auctionProcessToAuctionProcessVO() {
		AuctionProcessVO auctionProcessVO = new AuctionProcessVO();
		auctionProcessVO.setActive(isActive);
		auctionProcessVO.setId(id);
		auctionProcessVO.setProcess(process);
		return auctionProcessVO;
	}

}

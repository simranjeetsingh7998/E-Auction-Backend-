package com.auction.method;

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
@Table(name = "auctionMethod", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"method"})
})
@Data
public class AuctionMethod implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 435299778365888417L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 30)
	private String method;
	private boolean isActive;
	
	@OneToMany(mappedBy = "auctionMethod", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<AuctionPreparation> auctionPreparations = new HashSet<>();
	
	public AuctionMethodVO auctionMethodToAuctionMethodVO() {
		 AuctionMethodVO auctionMethodVO = new AuctionMethodVO();
		 auctionMethodVO.setActive(isActive);
		 auctionMethodVO.setId(id);
		 auctionMethodVO.setMethod(method);
		 return auctionMethodVO;
	}

}

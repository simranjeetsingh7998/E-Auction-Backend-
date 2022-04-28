package com.auction.preparation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "adminLiveBiddingAccess", indexes = {
		@Index(columnList = "auction"),
		@Index(columnList = "user")
})
@Data
public class AdminLiveBiddingAccess {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long auction;
	
	private Long user;

}

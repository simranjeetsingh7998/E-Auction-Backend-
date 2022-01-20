package com.auction.item.template;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.auction.organization.Organization;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "auctionItemTemplate", indexes = {@Index(columnList = "tName")})
@Data
@EqualsAndHashCode(of = {"id"})
public class AuctionItemTemplate implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7679413604439928638L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String tName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organizationId")
	private Organization organization;
	
	private boolean isActive;
	

}

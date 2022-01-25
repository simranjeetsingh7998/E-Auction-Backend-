package com.auction.screen.mapping;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.auction.user.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

@Entity
@Table(name = "screenRole")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenRoleMapping implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 21961906405763909L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Role role;
	
	@Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	private Screen screen;
	

}

package com.auction.sms;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "sms_template")
@Data
public class SMSTemplate implements Serializable{

	private static final long serialVersionUID = 50414313401591619L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String templateName;
	private String templateId;
	private String senderId;
}

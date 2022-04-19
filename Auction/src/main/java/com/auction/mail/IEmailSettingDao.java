package com.auction.mail;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmailSettingDao extends JpaRepository<EmailSetting, Integer>{

	List<EmailSetting> findByIdOrganization(Integer organizationId);

	
}

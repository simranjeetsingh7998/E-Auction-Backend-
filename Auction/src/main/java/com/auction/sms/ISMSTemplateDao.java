package com.auction.sms;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ISMSTemplateDao extends JpaRepository<SMSTemplate, Integer> {

	Optional<SMSTemplate> findByTemplateName(String templateName);
}

package com.auction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.auction.jpa.audit.SpringSecurityAuditorAware;

@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {

  @Bean
  public AuditorAware<Long> auditorProvider() {
    return new SpringSecurityAuditorAware();
  }
}
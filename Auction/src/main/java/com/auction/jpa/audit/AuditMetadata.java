package com.auction.jpa.audit;

import java.time.Instant;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditMetadata {

  @CreatedBy
  private Long createdBy;

  @CreatedDate
  private Instant createdDate;
  
  @CreatedBy
  private Long updatedBy;

  @CreatedDate
  private Instant updatedDate;

}
package com.voluntree.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ORGANIZATION")
public class Organization extends User {

  @Column(columnDefinition = "CHAR(14)")
  private String cnpj;

  @Column(name = "company_name")
  private String companyName;
  
  @Column(columnDefinition = "TEXT")
  private String cause;
}

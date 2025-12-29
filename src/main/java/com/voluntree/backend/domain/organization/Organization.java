package com.voluntree.backend.domain.organization;

import com.voluntree.backend.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ORGANIZATION")
public class Organization extends User {

  @Convert(converter = CnpjConverter.class)
  @Column(length = 14)
  private Cnpj cnpj;

  @Column(name = "company_name")
  private String companyName;

  @Column(columnDefinition = "TEXT")
  private String cause;
}

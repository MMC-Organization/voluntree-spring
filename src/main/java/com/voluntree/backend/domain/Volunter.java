package com.voluntree.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VOLUNTEER")
public class Volunter extends User {

  @Column(columnDefinition = "CHAR(11)")
  private String cpf;
}

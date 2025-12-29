package com.voluntree.backend.domain.volunteer;

import com.voluntree.backend.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VOLUNTEER")
public class Volunter extends User {

  @Convert(converter = CpfConverter.class)
  @Column(columnDefinition = "CHAR(11)")
  private Cpf cpf;
}

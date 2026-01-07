package com.voluntree.backend.domain.volunteer;

import org.springframework.web.bind.annotation.SessionAttributes;

import com.voluntree.backend.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@DiscriminatorValue("VOLUNTEER")
@NoArgsConstructor
@Setter
@Getter

public class Volunteer extends User {

  @Convert(converter = CpfConverter.class)
  @Column(length = 11)
  private Cpf cpf;

 
}

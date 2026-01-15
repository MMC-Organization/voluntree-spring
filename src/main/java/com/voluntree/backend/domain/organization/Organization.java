package com.voluntree.backend.domain.organization;

import com.voluntree.backend.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("ORGANIZATION")

@Getter
@Setter
public class Organization extends User {

  @Convert(converter = CnpjConverter.class)
  @Column(length = 14)
  private Cnpj cnpj;

  @Column(length = 255)
  private String companyName;

  @Column
  private String cause;
//testando so com isso pq tava dando um erro estranho
    public void setName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

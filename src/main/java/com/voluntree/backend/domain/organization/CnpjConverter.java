package com.voluntree.backend.domain.organization;

import jakarta.persistence.AttributeConverter;

public class CnpjConverter implements AttributeConverter<Cnpj, String> {

  @Override
  public String convertToDatabaseColumn(Cnpj attribute) {
    return attribute.getCnpj();
  }

  @Override
  public Cnpj convertToEntityAttribute(String dbData) {
    return new Cnpj(dbData);
  }

}

package com.voluntree.backend.domain.organization;

import com.voluntree.backend.config.security.CryptoUtils;

import jakarta.persistence.AttributeConverter;

public class CnpjConverter implements AttributeConverter<Cnpj, String> {

  @Override
  public String convertToDatabaseColumn(Cnpj attribute) {
    if (attribute == null) return null;
        return CryptoUtils.encrypt(attribute.getCnpj());
  }

  @Override
  public Cnpj convertToEntityAttribute(String dbData) {
    if (dbData == null) return null;
        return new Cnpj(CryptoUtils.decrypt(dbData));
  }

}

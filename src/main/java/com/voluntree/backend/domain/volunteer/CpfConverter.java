package com.voluntree.backend.domain.volunteer;

import com.voluntree.backend.config.security.CryptoUtils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CpfConverter implements AttributeConverter<Cpf, String> {

  @Override
  public String convertToDatabaseColumn(Cpf cpf) {
    if (cpf == null) return null;
        return CryptoUtils.encrypt(cpf.getCpf());
  }

  @Override
  public Cpf convertToEntityAttribute(String dbData) {
    if (dbData == null) return null;
        return new Cpf(CryptoUtils.decrypt(dbData));
  }
}

package com.voluntree.backend.domain.volunteer;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CpfConverter implements AttributeConverter<Cpf, String> {

  @Override
  public String convertToDatabaseColumn(Cpf cpf) {
    return cpf.getCpf();
  }

  @Override
  public Cpf convertToEntityAttribute(String dbData) {
    return new Cpf(dbData);
  }
}

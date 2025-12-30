package com.voluntree.backend.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OutcomeConverter implements AttributeConverter<Outcome, String> {

  @Override
  public String convertToDatabaseColumn(Outcome attribute) {
    return attribute == null ? null : attribute.getValue();
  }

  @Override
  public Outcome convertToEntityAttribute(String dbData) {
    return dbData == null ? null : Outcome.fromValue(dbData);
  }

}

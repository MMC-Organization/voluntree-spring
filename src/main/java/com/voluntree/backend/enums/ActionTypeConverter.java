package com.voluntree.backend.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ActionTypeConverter implements AttributeConverter<ActionType, String> {

  @Override
  public String convertToDatabaseColumn(ActionType attribute) {
    return attribute == null ? null : attribute.getValue();
  }

  @Override
  public ActionType convertToEntityAttribute(String dbData) {
    return dbData == null ? null : ActionType.fromValue(dbData);
  }

}

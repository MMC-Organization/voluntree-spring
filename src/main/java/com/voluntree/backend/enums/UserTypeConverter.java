package com.voluntree.backend.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserTypeConverter implements AttributeConverter<UserType, String> {

  @Override
  public String convertToDatabaseColumn(UserType attribute) {
    return attribute == null ? null : attribute.getValue();
  }

  @Override
  public UserType convertToEntityAttribute(String dbData) {
    return dbData == null ? null : UserType.fromValue(dbData);
  }

}

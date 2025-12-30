package com.voluntree.backend.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ModuleConverter implements AttributeConverter<Module, String> {

  @Override
  public String convertToDatabaseColumn(Module attribute) {
    return attribute == null ? null : attribute.getValue();
  }

  @Override
  public Module convertToEntityAttribute(String dbData) {
    return dbData == null ? null : Module.fromValue(dbData);
  }

}

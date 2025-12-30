package com.voluntree.backend.enums;

import java.util.stream.Stream;

public enum UserType {
  ORGANIZATION("ORGANIZATION"),
  VOLUNTEER("VOLUNTEER");

  private final String value;

  UserType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static UserType fromValue(String value) {
    return Stream.of(values()).filter(val -> val.value.equals(value)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("UserType inválido: " + value));
  }
}

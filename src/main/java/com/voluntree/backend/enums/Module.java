package com.voluntree.backend.enums;

import java.util.stream.Stream;

public enum Module {
  AUTH("AUTH"),
  PROFILE("PROFILE"),
  ACTIVITY("ACTIVITY"),
  REGISTRATION("REGISTRATION");

  private final String value;

  Module(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  public static Module fromValue(String value) {
    return Stream.of(values()).filter(val -> val.value.equals(value)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Module inválido: " + value));
  }
}

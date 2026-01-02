package com.voluntree.backend.enums;

import java.util.stream.Stream;

public enum Outcome {
  ATTEMP("ATTEMPT"),
  SUCESS("SUCESS"),
  FAIL("FAIL");

  private final String value;

  Outcome(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  public static Outcome fromValue(String value) {
    return Stream.of(values()).filter(val -> val.value.equals(value)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Outcome inválido: " + value));
  }
}

package com.voluntree.backend.enums;

import java.util.stream.Stream;

public enum ActionType {
  CREATE("CREATE"),
  UPDATE("UPDATE"),
  DELETE("DELETE"),
  SIGNIN("SIGNIN"),
  SIGNOUT("SIGNOUT"),
  ERROR("ERROR");

  private final String value;

  ActionType(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  public static ActionType fromValue(String value) {
    return Stream.of(values()).filter(val -> val.value.equals(value)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("ActionType inválido: " + value));
  }
}

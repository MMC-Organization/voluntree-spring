package com.voluntree.backend.domain;

import java.util.List;
import java.util.stream.Stream;

public class Cpf {

  private String cpf;

  public Cpf(String cpf) {
    if (cpf == null) {
      throw new IllegalArgumentException("Cpf não pode ser nulo!");
    }

    cpf = cpf.replaceAll("\\D", "");

    if (cpf.length() != 11)
      throw new IllegalArgumentException("Cpf inválido!");

    if (cpf.chars().distinct().count() == 1)
      throw new IllegalArgumentException("Cpf inválido!");

    List<Integer> numbers = Stream.of(cpf.split("")).map(Integer::parseInt).toList();

    Integer firstVerificationDigit = getDigit(numbers, 9, 10);
    Integer secondVerificationDigit = getDigit(numbers, 10, 11);

    if (!firstVerificationDigit.equals(numbers.get(9)) || !secondVerificationDigit.equals(numbers.getLast()))
      throw new IllegalArgumentException("Cpf inválido!");

    this.cpf = cpf;
  }

  private int getDigit(List<Integer> digits, Integer length, Integer initialMult) {
    int verificationDigit = 0;

    for (int i = 0; i < length; i++) {
      verificationDigit += digits.get(i) * (initialMult - i);
    }

    verificationDigit %= 11;
    verificationDigit = 11 - verificationDigit;

    if (verificationDigit >= 10) {
      verificationDigit = 0;
    }

    return verificationDigit;
  }

  public String getCpf() {
    return this.cpf;
  }
}

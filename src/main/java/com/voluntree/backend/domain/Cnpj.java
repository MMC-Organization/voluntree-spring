package com.voluntree.backend.domain;

import java.util.List;
import java.util.stream.Stream;

public class Cnpj {

  private String cnpj;

  public Cnpj(String cnpj) {
    if (cnpj == null) {
      throw new IllegalArgumentException("Cnpj não pode ser nulo!");
    }

    cnpj = cnpj.replaceAll("\\D", "");

    if (cnpj.length() != 14)
      throw new IllegalArgumentException("Cnpj inválido!");

    if (cnpj.chars().distinct().count() == 1)
      throw new IllegalArgumentException("Cnpj inválido!");

    List<Integer> numbers = Stream.of(cnpj.split("")).map(Integer::parseInt).toList();

    Integer firstVerificationDigit = getDigit(numbers, 12);
    Integer secondVerificationDigit = getDigit(numbers, 13);

    if (!firstVerificationDigit.equals(numbers.get(12)) || !secondVerificationDigit.equals(numbers.getLast()))
      throw new IllegalArgumentException("Cnpj inválido!");

    this.cnpj = cnpj;
  }

  private int getDigit(List<Integer> digits, int length) {
    int verificationDigit = 0;

    int multiplier = 2;

    for (int i = length - 1; i >= 0; i--) {
      verificationDigit += digits.get(i) * multiplier;

      if (multiplier == 9) {
        multiplier = 2;

        continue;
      }

      ++multiplier;
    }

    verificationDigit %= 11;
    verificationDigit = 11 - verificationDigit;

    if (verificationDigit >= 10) {
      verificationDigit = 0;
    }

    return verificationDigit;
  }

  public String getCnpj() {
    return this.cnpj;
  }
}

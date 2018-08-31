package com.scottlogic.deg.generator.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Isin {
  public static final List<String> VALID_COUNTRY_CODES = Arrays.asList("GB", "US");

  private String isinString;

  public Isin(String isinString) {
    this.isinString = isinString;
  }

  public String getCountryCode() {
    return isinString.substring(0, 2);
  }

  public String getNsin() {
    return isinString.substring(2, 11);
  }

  public String getCheckDigit() {
    return isinString.substring(11, 12);
  }

  public static boolean isValidIsin(String isin) {
    if (isin.length() != 12) {
      return false;
    }
    final String countryCode = isin.substring(0, 2);
    final String nsin = isin.substring(2, 11);
    if (!isValidCountryCode(countryCode)) {
      return false;
    }
    if (!isValidNsin(countryCode, nsin)) {
      return false;
    }
    return isinHasValidCheckDigit(isin);
  }

  private static boolean isValidCountryCode(String countryCode) {
    return VALID_COUNTRY_CODES.contains(countryCode);
  }

  private static boolean isValidNsin(String countryCode, String nsin) {
    if (!nsin.matches("[A-Z0-9]{9}")) {
      return false;
    }
    if (countryCode.equals("GB")) {
      return checkNsinIsSedol(nsin);
    }
    return true;
  }

  private static List<Integer> SEDOL_WEIGHTS = Arrays.asList(1, 3, 1, 7, 3, 9, 1);

  // Assumes generic NSIN checks have already been run, i.e. `nsin` is 9 alphanumeric characters
  private static boolean checkNsinIsSedol(String nsin){
    // SEDOL has length 7, so first two digits of NSIN must be padded 0s
    if (!nsin.substring(0, 2).equals("00")) {
      return false;
    }
    // SEDOL is alphanumeric but cannot contain vowels
    if (nsin.matches(".*[AEIOU].*")) {
      return false;
    }
    String sedolPreCheckDigit = nsin.substring(2, 8);
    char checkDigit = calculateSedolCheckDigit(sedolPreCheckDigit);
    return nsin.charAt(8) == checkDigit;
  }

  public static char calculateSedolCheckDigit(String sedol) {
    int sum = 0;
    for (int ii = 0; ii < 6; ++ii){
      sum += SEDOL_WEIGHTS.get(ii) * Character.digit(sedol.charAt(ii), 36);
    }
    int check = (10 - (sum % 10)) % 10;
    return Character.forDigit(check, 10);
  }

  // Validates the check digit at the end of `isin`, which is assumed to be a valid 12-character ISIN
  private static boolean isinHasValidCheckDigit(String isin) {
    final char calculatedCheckDigit = calculateIsinCheckDigit(isin.substring(0, 11));
    return isin.charAt(11) == calculatedCheckDigit;
  }

  // Generates  the check digit that should be appended to `isin`, which should be the first 11-characters of a valid ISIN
  public static char calculateIsinCheckDigit(String isin) {
    final List<Integer> isinConvertedDigits = isin.chars()
      .map(character -> Character.digit(character, 36))
      .flatMap(Isin::splitDigits)
      .collect(ArrayList::new, List::add, ArrayList::addAll);
    final int weightedDigitSum = IntStream.range(0, isinConvertedDigits.size())
      .map(reverseIndex -> isinConvertedDigits.get(isinConvertedDigits.size() - reverseIndex - 1) * (reverseIndex % 2 == 0 ? 2 : 1))
      .flatMap(Isin::splitDigits)
      .reduce(0, Integer::sum);
    int checkDigit = (10 - (weightedDigitSum % 10)) % 10;
    return Character.forDigit(checkDigit, 10);
  }

  private static IntStream splitDigits(int num) {
    return num >= 10
      ? IntStream.of(num / 10, num % 10)
      : IntStream.of(num);
  }
}

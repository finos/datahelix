package com.scottlogic.deg.generator.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class IsinStringGeneratorTest {

  @Before
  public void setup() {
  }

  @Test
  public void shouldEndAllIsinsWithValidCheckDigit() {
    IsinStringGenerator target = new IsinStringGenerator();
    final int NumberOfTests = 100;

    final Iterator<String> allIsins = target.generateAllValues().iterator();

    for (int ii = 0; ii < NumberOfTests; ++ii) {
      final String nextIsin = allIsins.next();
      final char checkDigit = Isin.calculateIsinCheckDigit(nextIsin.substring(0, 11));
      assertThat(nextIsin.charAt(11), equalTo(checkDigit));
    }
  }

  @Test
  public void shouldEndAllRandomIsinsWithValidCheckDigit() {
    IsinStringGenerator target = new IsinStringGenerator();

    final int NumberOfTests = 100;

    final Iterator<String> allIsins = target.generateRandomValues(new JavaUtilRandomNumberGenerator()).iterator();

    for (int ii = 0; ii < NumberOfTests; ++ii) {
      final String nextIsin = allIsins.next();
      final char checkDigit = Isin.calculateIsinCheckDigit(nextIsin.substring(0, 11));
      assertThat(nextIsin.charAt(11), equalTo(checkDigit));
    }
  }

  @Test
  public void shouldOnlyUseSpecifiedCountries() {
    final String testCountry = "GB";
    IsinStringGenerator target = new IsinStringGenerator(Collections.singletonList(testCountry));
    final int NumberOfTests = 100;

    final Iterator<String> allIsins = target.generateRandomValues(new JavaUtilRandomNumberGenerator()).iterator();

    for (int ii = 0; ii < NumberOfTests; ++ii) {
      final String nextIsin = allIsins.next();
      assertThat(nextIsin.substring(0,2), equalTo(testCountry));
    }
  }

}

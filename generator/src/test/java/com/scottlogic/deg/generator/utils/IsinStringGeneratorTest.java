package com.scottlogic.deg.generator.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class IsinStringGeneratorTest {

  private IsinStringGenerator target;

  @Before
  public void setup() {
    target = new IsinStringGenerator();
  }

  @Test
  public void shouldEndAllIsinsWithValidCheckDigit() {
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
    final int NumberOfTests = 100;

    final Iterator<String> allIsins = target.generateRandomValues(new JavaUtilRandomNumberGenerator()).iterator();

    for (int ii = 0; ii < NumberOfTests; ++ii) {
      final String nextIsin = allIsins.next();
      final char checkDigit = Isin.calculateIsinCheckDigit(nextIsin.substring(0, 11));
      assertThat(nextIsin.charAt(11), equalTo(checkDigit));
    }
  }

  @Test
  public void shouldAcceptValidIsin() {
    final RegexStringGenerator validFixedIsinRegexGenerator = new RegexStringGenerator("GB0002634946");
    final IStringGenerator validFixedIsinGenerator = target.intersect(validFixedIsinRegexGenerator);

    final Iterator<String> validFixedIsinIterator = validFixedIsinGenerator.generateAllValues().iterator();

    Assert.assertTrue("ISIN generator should generate at least one value for a regex matching a single valid ISIN.",
      validFixedIsinIterator.hasNext());
    final String validIsin = validFixedIsinIterator.next();

    Assert.assertEquals("Generated ISIN should match the regex passed to the generator.", validIsin, "GB0002634946");
    Assert.assertFalse("ISIN generator should not generate more than a single value for a regex matching a single valid ISIN.",
      validFixedIsinIterator.hasNext());
  }

  @Test
  public void shouldNotGenerateIsinWithInvalidCountryCode() {
    final String notCountryCodeRegex = Isin.VALID_COUNTRY_CODES.stream().collect(Collectors.joining("|", "^(", ").+"));
    final RegexStringGenerator invalidCountryStringGenerator = new RegexStringGenerator(notCountryCodeRegex);

    final IStringGenerator invalidCountryIsinGenerator = target.intersect(invalidCountryStringGenerator);
    Assert.assertFalse("ISIN generator should not generate any values with an invalid country code.",
      invalidCountryIsinGenerator.generateAllValues().iterator().hasNext());
  }

  @Test
  public void shouldCorrectlyCalculateCheckDigit() {
    final RegexStringGenerator isinWithRandomCheckDigitGenerator = new RegexStringGenerator("GB000263494[0-9]");
    final IStringGenerator isinGenerator = target.intersect(isinWithRandomCheckDigitGenerator);
    final Iterator<String> isinIterator = isinGenerator.generateAllValues().iterator();

    final String calculatedIsinString = isinIterator.next();
    Assert.assertFalse("ISIN generator should only calculate a single valid check digit.",
      isinIterator.hasNext());
    Assert.assertEquals("ISIN generator should calculate the correct check digit.",
      "6", calculatedIsinString.substring(calculatedIsinString.length() - 1));
  }

  @Test
  public void shouldBeAbleToGenerateIsinWithFixedCheckDigit() {
    final RegexStringGenerator fixedEndingStringGenerator = new RegexStringGenerator(".+5");
    final IStringGenerator fixedEndingIsinGenerator = target.intersect(fixedEndingStringGenerator);
    final int NumberOfTests = 100;
    final Iterator<String> fixedEndingIsinIterator = fixedEndingIsinGenerator.generateAllValues().iterator();

    for (int TestIndex = 0; TestIndex < NumberOfTests; TestIndex += 1) {
      final String nextIsin = fixedEndingIsinIterator.next();
      Assert.assertEquals("ISIN generator should be able to generate values with a fixed check digit.",
        "5", nextIsin.substring(nextIsin.length() - 1));
    }
  }

  @Test
  public void shouldRejectGBCountryCodeWithInvalidSedol() {
    final RegexStringGenerator invalidSedolGenerator = new RegexStringGenerator("GB0002634986"); // TODO Change ISIN check digit
    final IStringGenerator invalidSedolIsinGenerator = target.intersect(invalidSedolGenerator);

    Assert.assertFalse("ISIN generator should not generate any values with an invalid SEDOL.",
      invalidSedolIsinGenerator.generateAllValues().iterator().hasNext());
  }

  @Test
  public void shouldAlwaysGenerateIsinOfValidLength() {
    final RegexStringGenerator tooLongIsinRegexGenerator = new RegexStringGenerator(".{13,}");
    final RegexStringGenerator tooShortIsinRegexGenerator = new RegexStringGenerator(".{0,11}");
    final IStringGenerator tooLongIsinGenerator = target.intersect(tooLongIsinRegexGenerator);
    final IStringGenerator tooShortIsinGenerator = target.intersect(tooShortIsinRegexGenerator);

    Assert.assertFalse("ISIN generator should not generate any values with more than 12 characters.",
      tooLongIsinGenerator.generateAllValues().iterator().hasNext());
    Assert.assertFalse("ISIN generator should not generate any values with less than 12 characters.",
      tooShortIsinGenerator.generateAllValues().iterator().hasNext());
  }
}

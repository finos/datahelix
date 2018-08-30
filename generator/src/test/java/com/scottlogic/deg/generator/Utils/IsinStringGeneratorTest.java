package com.scottlogic.deg.generator.Utils;

import com.scottlogic.deg.generator.utils.IStringGenerator;
import com.scottlogic.deg.generator.utils.IsinStringGenerator;
import com.scottlogic.deg.generator.utils.RegexStringGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

public class IsinStringGeneratorTest {

  private static final String VALID_ISIN = "GB0002634946";

  private IsinStringGenerator target;

  @Before
  void setup() {
    target = new IsinStringGenerator();
  }

  @Test
  void shouldAcceptValidIsin() {
    final RegexStringGenerator validFixedIsinRegexGenerator = new RegexStringGenerator(VALID_ISIN);
    final IStringGenerator validFixedIsinGenerator = target.intersect(validFixedIsinRegexGenerator);

    final Iterator<String> validFixedIsinIterator = validFixedIsinGenerator.generateAllValues().iterator();

    Assert.assertTrue("ISIN generator should generate a single value for a regex matching a single valid ISIN.",
      validFixedIsinIterator.hasNext());
    final String validIsin = validFixedIsinIterator.next();

    Assert.assertEquals("Generated ISIN should match the regex passed to the generator.", validIsin, VALID_ISIN);
    Assert.assertFalse("ISIN generator should not generate more than a single value for a regex matching a single valid ISIN.",
      validFixedIsinIterator.hasNext());
  }

  @Test
  void shouldNotGenerateIsinWithInvalidCountryCode() {
    final RegexStringGenerator invalidCountryStringGenerator = new RegexStringGenerator("XX.+");
    final IStringGenerator invalidCountryIsinGenerator = target.intersect(invalidCountryStringGenerator);
    Assert.assertFalse("ISIN generator should not generate any values with an invalid country code.",
      invalidCountryIsinGenerator.generateAllValues().iterator().hasNext());
  }

  @Test
  void shouldCorrectlyCalculateCheckDigit() {
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
  void shouldBeAbleToGenerateIsinWithFixedCheckDigit() {
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
  void shouldRejectGBCountryCodeWithInvalidSedol() {
    final RegexStringGenerator invalidSedolGenerator = new RegexStringGenerator("GB0002634986");
    final IStringGenerator invalidSedolIsinGenerator = target.intersect(invalidSedolGenerator);

    Assert.assertFalse("ISIN generator should not generate any values with an invalid SEDOL.",
      invalidSedolIsinGenerator.generateAllValues().iterator().hasNext());
  }
}

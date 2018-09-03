package com.scottlogic.deg.generator.utils;

public class SedolStringGenerator implements IStringGenerator {

  private static String SEDOL_SANS_CHECK_DIGIT_REGEX = "00[B-DF-HJ-NP-TV-Z0-9]{6}";

  private RegexStringGenerator sedolSansCheckDigitGenerator;

  public SedolStringGenerator(String prefix) {
    sedolSansCheckDigitGenerator = new RegexStringGenerator(prefix + SEDOL_SANS_CHECK_DIGIT_REGEX);
  }

  @Override
  public IStringGenerator intersect(IStringGenerator stringGenerator) {
    throw new UnsupportedOperationException();
  }

  @Override
  public IStringGenerator complement() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isFinite() {
    return true;
  }

  @Override
  public long getValueCount() {
    return sedolSansCheckDigitGenerator.getValueCount();
  }

  @Override
  public Iterable<String> generateAllValues() {
    return new ProjectingIterable<>(sedolSansCheckDigitGenerator.generateAllValues(),
      sedolSansCheckDigit -> sedolSansCheckDigit + Isin.calculateSedolCheckDigit(sedolSansCheckDigit.substring(4)));
  }

  @Override
  public Iterable<String> generateRandomValues(IRandomNumberGenerator randomNumberGenerator) {
    return new ProjectingIterable<>(sedolSansCheckDigitGenerator.generateRandomValues(randomNumberGenerator),
      sedolSansCheckDigit -> sedolSansCheckDigit + Isin.calculateSedolCheckDigit(sedolSansCheckDigit.substring(4)));
  }
}

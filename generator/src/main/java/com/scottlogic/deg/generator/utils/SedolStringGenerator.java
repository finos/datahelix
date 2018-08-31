package com.scottlogic.deg.generator.utils;

public class SedolStringGenerator implements IStringGenerator {

  private static String SEDOL_SANS_CHECK_DIGIT_REGEX = "00[B-DF-HJ-NP-TV-Z0-9]{6}";

  private RegexStringGenerator sedolSansCheckDigitGenerator;
  private String prefix;

  public SedolStringGenerator(String prefix) {
    this.prefix = prefix;
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
    // 6 independently variable digits, with 31 possible values each
    return 31 ^ 6;
  }

  @Override
  public Iterable<String> generateAllValues() {
    return () -> IterableAsStream.convert(sedolSansCheckDigitGenerator.generateAllValues())
      .map(sedolSansCheckDigit -> sedolSansCheckDigit + Isin.calculateSedolCheckDigit(sedolSansCheckDigit))
      .iterator();
  }

  @Override
  public Iterable<String> generateRandomValues(IRandomNumberGenerator randomNumberGenerator) {
    return () -> IterableAsStream.convert(sedolSansCheckDigitGenerator.generateRandomValues(randomNumberGenerator))
      .map(sedolFront -> sedolFront + Isin.calculateSedolCheckDigit(sedolFront))
      .iterator();
  }
}

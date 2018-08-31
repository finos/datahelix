package com.scottlogic.deg.generator.utils;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IsinStringGenerator implements IStringGenerator {

  private static final String GENERIC_ISIN_REGEX = "[A-Z0-9]{9}";

  @Override
  public IStringGenerator intersect(IStringGenerator stringGenerator) {
    if (stringGenerator instanceof IsinStringGenerator) {
      return new IsinStringGenerator();
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public IStringGenerator complement() {
    return null;
  }

  @Override
  public boolean isFinite() {
    return true;
  }

  @Override
  public long getValueCount() {
    return getAllCountryIsinGeneratorsAsStream()
      .map(IStringGenerator::getValueCount)
      .reduce(0L, Long::sum);
  }

  @Override
  public Iterable<String> generateAllValues() {
    return () -> getAllCountryIsinGeneratorsAsStream()
      .flatMap(isinSansCheckDigitGenerator ->
        IterableAsStream.convert(isinSansCheckDigitGenerator.generateAllValues()))
      .map(isinSansCheckDigit -> isinSansCheckDigit + Isin.calculateIsinCheckDigit(isinSansCheckDigit))
      .iterator();
  }

  @Override
  public Iterable<String> generateRandomValues(IRandomNumberGenerator randomNumberGenerator) {
    return () -> {
      final List<Iterator<String>> countryIsinIterators = getAllCountryIsinGeneratorsAsStream()
        .map(isinSansCheckDigitGenerator ->
          IterableAsStream.convert(isinSansCheckDigitGenerator.generateRandomValues(randomNumberGenerator))
            .map(isinSansCheckDigit -> isinSansCheckDigit + Isin.calculateIsinCheckDigit(isinSansCheckDigit))
            .iterator())
        .collect(Collectors.toList());
      return new RandomMergeIterator<>(countryIsinIterators, randomNumberGenerator);
    };
  }

  private static Stream<IStringGenerator> getAllCountryIsinGeneratorsAsStream() {
    return Isin.VALID_COUNTRY_CODES.stream()
      .map(IsinStringGenerator::getIsinSansCheckDigitGeneratorForCountry);
  }

  private static IStringGenerator getIsinSansCheckDigitGeneratorForCountry(String countryCode) {
    if (countryCode.equals("GB")) {
      return new SedolStringGenerator(countryCode);
    }
    return new RegexStringGenerator(countryCode + GENERIC_ISIN_REGEX);
  }
}

package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.ConcatenatingIterable;
import com.scottlogic.deg.generator.utils.ExpandingIterable;
import com.scottlogic.deg.generator.utils.IRandomNumberGenerator;
import com.scottlogic.deg.generator.utils.Isin;
import com.scottlogic.deg.generator.utils.ProjectingIterable;
import com.scottlogic.deg.generator.utils.RandomMergingIterable;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SedolStringGenerator implements IStringGenerator {

  private static String SEDOL_SANS_CHECK_DIGIT_REGEX = "00[B-DF-HJ-NP-TV-Z0-9]{6}";

  private RegexStringGenerator sedolSansCheckDigitGenerator;
  private boolean negate;

  public SedolStringGenerator() {
    this.negate = false;
    this.sedolSansCheckDigitGenerator = new RegexStringGenerator(SEDOL_SANS_CHECK_DIGIT_REGEX);
  }

  public SedolStringGenerator(String prefix) {
    this.negate = false;
    this.sedolSansCheckDigitGenerator = new RegexStringGenerator(prefix + SEDOL_SANS_CHECK_DIGIT_REGEX);
  }

  private SedolStringGenerator(RegexStringGenerator sedolSansCheckDigitGenerator, boolean negate) {
    this.negate = negate;
    this.sedolSansCheckDigitGenerator = sedolSansCheckDigitGenerator;
  }

  @Override
  public IStringGenerator intersect(IStringGenerator stringGenerator) {
    throw new UnsupportedOperationException();
  }

  @Override
  public IStringGenerator complement() {
    return new SedolStringGenerator(sedolSansCheckDigitGenerator, !negate);
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
  public boolean match(String subject) {
    return Isin.isValidSedolNsin(subject);
  }

  @Override
  public Iterable<String> generateInterestingValues() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Iterable<String> generateAllValues() {
    if (negate) {
      return new ConcatenatingIterable<>(
        Arrays.asList(generateAllInvalidRegexSedols(), generateAllInvalidCheckDigitSedols()));
    }
    return new ProjectingIterable<>(sedolSansCheckDigitGenerator.generateAllValues(),
      sedolSansCheckDigit -> sedolSansCheckDigit + Isin.calculateSedolCheckDigit(
        sedolSansCheckDigit.substring(sedolSansCheckDigit.length() - 6)));
  }

  @Override
  public Iterable<String> generateRandomValues(IRandomNumberGenerator randomNumberGenerator) {
    if (negate) {
      return new RandomMergingIterable<>(
        Arrays.asList(generateRandomInvalidRegexSedols(randomNumberGenerator), generateRandomInvalidCheckDigitSedols(randomNumberGenerator)),
        randomNumberGenerator);
    }
    return new ProjectingIterable<>(sedolSansCheckDigitGenerator.generateRandomValues(randomNumberGenerator),
      sedolSansCheckDigit -> sedolSansCheckDigit + Isin.calculateSedolCheckDigit(sedolSansCheckDigit.substring(4)));
  }

  private Iterable<String> generateAllInvalidRegexSedols() {
    return sedolSansCheckDigitGenerator.complement().generateAllValues();
  }

  private Iterable<String> generateRandomInvalidRegexSedols(IRandomNumberGenerator randomNumberGenerator) {
    return sedolSansCheckDigitGenerator.complement().generateRandomValues(randomNumberGenerator);
  }

  private Iterable<String> generateAllInvalidCheckDigitSedols() {
    return generateInvalidCheckDigitSedols(sedolSansCheckDigitGenerator::generateAllValues);
  }

  private Iterable<String> generateRandomInvalidCheckDigitSedols(IRandomNumberGenerator randomNumberGenerator) {
    return generateInvalidCheckDigitSedols(
      () -> sedolSansCheckDigitGenerator.generateRandomValues(randomNumberGenerator));
  }

  private Iterable<String> generateInvalidCheckDigitSedols(Supplier<Iterable<String>> sedolSansCheckDigitSupplier) {
    return new ExpandingIterable<>(
      sedolSansCheckDigitSupplier.get(),
      sedolSansCheckDigit -> {
        final char checkDigit = Isin.calculateSedolCheckDigit(
          sedolSansCheckDigit.substring(sedolSansCheckDigit.length() - 6));
        return IntStream.range(0, 10).boxed()
          .map(digit -> Character.forDigit(digit, 10))
          .filter(digit -> digit != checkDigit)
          .map(digit -> sedolSansCheckDigit + digit)
          .collect(Collectors.toList());
      });
  }
}

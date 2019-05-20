package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CusipStringGenerator implements StringGenerator {
    public final static int CUSIP_LENGTH = 9;
    private final static String CUSIP_SANS_CHECK_DIGIT_REGEX = "[0-9]{3}[0-9A-Z]{5}";

    private final RegexStringGenerator cusipSansCheckDigitGenerator;
    private final boolean negate;

    public CusipStringGenerator() {
        this.negate = false;
        this.cusipSansCheckDigitGenerator =
            new RegexStringGenerator(CUSIP_SANS_CHECK_DIGIT_REGEX, true);
    }

    public CusipStringGenerator(String prefix) {
        this.negate = false;
        this.cusipSansCheckDigitGenerator =
            new RegexStringGenerator(prefix + CUSIP_SANS_CHECK_DIGIT_REGEX, true);
    }

    private CusipStringGenerator(RegexStringGenerator cusipSansCheckDigitGenerator, boolean negate) {
        this.negate = negate;
        this.cusipSansCheckDigitGenerator = cusipSansCheckDigitGenerator;
    }

    @Override
    public StringGenerator intersect(StringGenerator stringGenerator) {
        return new NoStringsStringGenerator(
            RegexStringGenerator.intersectRepresentation(stringGenerator.toString(), "<CUSIP>")
        );
    }

    @Override
    public StringGenerator complement() {
        return new CusipStringGenerator(cusipSansCheckDigitGenerator, !negate);
    }

    @Override
    public boolean isFinite() {
        return true;
    }

    @Override
    public long getValueCount() {
        return cusipSansCheckDigitGenerator.getValueCount();
    }

    @Override
    public boolean match(String subject) {
        boolean matches = IsinUtils.isValidCusipNsin(subject);
        return matches != negate;
    }

    @Override
    public Iterable<String> generateInterestingValues() {
        if (negate) {
            return new ConcatenatingIterable<>(
                cusipSansCheckDigitGenerator.complement().generateInterestingValues(),
                generateInvalidCheckDigitCusips(cusipSansCheckDigitGenerator::generateInterestingValues));
        }
        return new ProjectingIterable<>(cusipSansCheckDigitGenerator.generateInterestingValues(),
            cusipSansCheckDigit -> cusipSansCheckDigit + IsinUtils.calculateCusipCheckDigit(
                cusipSansCheckDigit.substring(cusipSansCheckDigit.length() - (CUSIP_LENGTH - 1))));
    }

    @Override
    public Iterable<String> generateAllValues() {
        if (negate) {
            return new ConcatenatingIterable<>(
                generateAllInvalidRegexCusips(),
                generateAllInvalidCheckDigitCusips());
        }
        return new ProjectingIterable<>(cusipSansCheckDigitGenerator.generateAllValues(),
            cusipSansCheckDigit -> cusipSansCheckDigit + IsinUtils.calculateCusipCheckDigit(
                cusipSansCheckDigit.substring(cusipSansCheckDigit.length() - (CUSIP_LENGTH - 1))));
    }

    @Override
    public Iterable<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        if (negate) {
            return new RandomMergingIterable<>(
                Arrays.asList(
                    generateRandomInvalidRegexCusips(randomNumberGenerator),
                    generateRandomInvalidCheckDigitCusips(randomNumberGenerator)),
                randomNumberGenerator);
        }
        return new ProjectingIterable<>(cusipSansCheckDigitGenerator.generateRandomValues(randomNumberGenerator),
            cusipSansCheckDigit -> cusipSansCheckDigit + IsinUtils.calculateCusipCheckDigit(
                cusipSansCheckDigit.substring(cusipSansCheckDigit.length() - (CUSIP_LENGTH - 1))));
    }

    private Iterable<String> generateAllInvalidRegexCusips() {
        return cusipSansCheckDigitGenerator.complement().generateAllValues();
    }

    private Iterable<String> generateRandomInvalidRegexCusips(RandomNumberGenerator randomNumberGenerator) {
        return cusipSansCheckDigitGenerator.complement().generateRandomValues(randomNumberGenerator);
    }

    private Iterable<String> generateAllInvalidCheckDigitCusips() {
        return generateInvalidCheckDigitCusips(cusipSansCheckDigitGenerator::generateAllValues);
    }

    private Iterable<String> generateRandomInvalidCheckDigitCusips(RandomNumberGenerator randomNumberGenerator) {
        return generateInvalidCheckDigitCusips(
            () -> cusipSansCheckDigitGenerator.generateRandomValues(randomNumberGenerator));
    }

    private Iterable<String> generateInvalidCheckDigitCusips(Supplier<Iterable<String>> cusipSansCheckDigitSupplier) {
        return new FlatteningIterable<>(
            cusipSansCheckDigitSupplier.get(),
            cusipSansCheckDigit -> {
                final char checkDigit = IsinUtils.calculateCusipCheckDigit(
                    cusipSansCheckDigit.substring(cusipSansCheckDigit.length() - (CUSIP_LENGTH - 1)));
                return IntStream.range(0, 10).boxed()
                    .map(digit -> Character.forDigit(digit, 10))
                    .filter(digit -> digit != checkDigit)
                    .map(digit -> cusipSansCheckDigit + digit)
                    .collect(Collectors.toList());
            });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CusipStringGenerator that = (CusipStringGenerator) o;
        return negate == that.negate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(negate, getClass());
    }
}

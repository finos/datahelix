package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// There's a lot of copy-paste in this class. We should address that before we make any significant changes to it
public class SedolStringGenerator implements StringGenerator {

    private final static String SEDOL_SANS_CHECK_DIGIT_REGEX = "00[B-DF-HJ-NP-TV-Z0-9]{6}";

    private final RegexStringGenerator sedolSansCheckDigitGenerator;
    private final boolean negate;

    public SedolStringGenerator() {
        this.negate = false;
        this.sedolSansCheckDigitGenerator = new RegexStringGenerator(SEDOL_SANS_CHECK_DIGIT_REGEX, true);
    }

    public SedolStringGenerator(String prefix) {
        this.negate = false;
        this.sedolSansCheckDigitGenerator = new RegexStringGenerator(prefix + SEDOL_SANS_CHECK_DIGIT_REGEX, true);
    }

    private SedolStringGenerator(RegexStringGenerator sedolSansCheckDigitGenerator, boolean negate) {
        this.negate = negate;
        this.sedolSansCheckDigitGenerator = sedolSansCheckDigitGenerator;
    }

    @Override
    public StringGenerator intersect(StringGenerator stringGenerator) {
        return this;
    }

    @Override
    public StringGenerator complement() {
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
        return IsinUtils.isValidSedolNsin(subject);
    }

    @Override
    public Iterable<String> generateInterestingValues() {
        if (negate) {
            return new ConcatenatingIterable<>(
                sedolSansCheckDigitGenerator.complement().generateInterestingValues(),
                generateInvalidCheckDigitSedols(sedolSansCheckDigitGenerator::generateInterestingValues));
        }
        return new ProjectingIterable<>(sedolSansCheckDigitGenerator.generateInterestingValues(),
            sedolSansCheckDigit -> sedolSansCheckDigit + IsinUtils.calculateSedolCheckDigit(
                sedolSansCheckDigit.substring(sedolSansCheckDigit.length() - 6)));
    }

    @Override
    public Iterable<String> generateAllValues() {
        if (negate) {
            return new ConcatenatingIterable<>(
                generateAllInvalidRegexSedols(),
                generateAllInvalidCheckDigitSedols());
        }
        return new ProjectingIterable<>(sedolSansCheckDigitGenerator.generateAllValues(),
            sedolSansCheckDigit -> sedolSansCheckDigit + IsinUtils.calculateSedolCheckDigit(
                sedolSansCheckDigit.substring(sedolSansCheckDigit.length() - 6)));
    }

    @Override
    public Iterable<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        if (negate) {
            return new RandomMergingIterable<>(
                Arrays.asList(
                    generateRandomInvalidRegexSedols(randomNumberGenerator),
                    generateRandomInvalidCheckDigitSedols(randomNumberGenerator)),
                randomNumberGenerator);
        }
        return new ProjectingIterable<>(sedolSansCheckDigitGenerator.generateRandomValues(randomNumberGenerator),
            sedolSansCheckDigit -> sedolSansCheckDigit + IsinUtils.calculateSedolCheckDigit(sedolSansCheckDigit.substring(4)));
    }

    private Iterable<String> generateAllInvalidRegexSedols() {
        return sedolSansCheckDigitGenerator.complement().generateAllValues();
    }

    private Iterable<String> generateRandomInvalidRegexSedols(RandomNumberGenerator randomNumberGenerator) {
        return sedolSansCheckDigitGenerator.complement().generateRandomValues(randomNumberGenerator);
    }

    private Iterable<String> generateAllInvalidCheckDigitSedols() {
        return generateInvalidCheckDigitSedols(sedolSansCheckDigitGenerator::generateAllValues);
    }

    private Iterable<String> generateRandomInvalidCheckDigitSedols(RandomNumberGenerator randomNumberGenerator) {
        return generateInvalidCheckDigitSedols(
            () -> sedolSansCheckDigitGenerator.generateRandomValues(randomNumberGenerator));
    }

    private Iterable<String> generateInvalidCheckDigitSedols(Supplier<Iterable<String>> sedolSansCheckDigitSupplier) {
        return new FlatteningIterable<>(
            sedolSansCheckDigitSupplier.get(),
            sedolSansCheckDigit -> {
                final char checkDigit = IsinUtils.calculateSedolCheckDigit(
                    sedolSansCheckDigit.substring(sedolSansCheckDigit.length() - 6));
                return IntStream.range(0, 10).boxed()
                    .map(digit -> Character.forDigit(digit, 10))
                    .filter(digit -> digit != checkDigit)
                    .map(digit -> sedolSansCheckDigit + digit)
                    .collect(Collectors.toList());
            });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SedolStringGenerator that = (SedolStringGenerator) o;
        return negate == that.negate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(negate, getClass());
    }
}

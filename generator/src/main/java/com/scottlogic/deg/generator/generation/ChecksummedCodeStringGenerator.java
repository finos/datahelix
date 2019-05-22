package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class ChecksummedCodeStringGenerator implements StringGenerator {
    protected final RegexStringGenerator sansCheckDigitGenerator;
    protected final boolean negate;

    public ChecksummedCodeStringGenerator(String generationPatternSansCheck) {
        negate = false;
        sansCheckDigitGenerator = new RegexStringGenerator(generationPatternSansCheck, true);
    }

    public ChecksummedCodeStringGenerator(RegexStringGenerator generator, boolean negate) {
        this.negate = negate;
        sansCheckDigitGenerator = generator;
    }

    public abstract char calculateCheckDigit(String str);

    @Override
    public abstract StringGenerator complement();

    @Override
    public abstract boolean match(String subject);

    @Override
    public StringGenerator intersect(StringGenerator stringGenerator) {
        return new NoStringsStringGenerator(
            RegexStringGenerator.intersectRepresentation(stringGenerator.toString(), "<checksummed string>")
        );
    }

    @Override
    public boolean isFinite() {
        return true;
    }

    @Override
    public long getValueCount() {
        return sansCheckDigitGenerator.getValueCount();
    }

    @Override
    public Iterable<String> generateInterestingValues() {
        if (negate) {
            return new ConcatenatingIterable<>(
                sansCheckDigitGenerator.complement().generateInterestingValues(),
                generateInvalidCheckDigitStrings(sansCheckDigitGenerator::generateInterestingValues));
        }
        return new ProjectingIterable<>(sansCheckDigitGenerator.generateInterestingValues(),
            sansCheckDigit -> sansCheckDigit + calculateCheckDigit(sansCheckDigit));
    }

    @Override
    public Iterable<String> generateAllValues() {
        if (negate) {
            return new ConcatenatingIterable<>(
                generateAllInvalidRegexStrings(),
                generateAllInvalidCheckDigitStrings());
        }
        return new ProjectingIterable<>(sansCheckDigitGenerator.generateAllValues(),
            sansCheckDigit -> sansCheckDigit + calculateCheckDigit(sansCheckDigit));
    }

    @Override
    public Iterable<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        if (negate) {
            return new RandomMergingIterable<>(
                Arrays.asList(
                    generateRandomInvalidRegexStrings(randomNumberGenerator),
                    generateRandomInvalidCheckDigitStrings(randomNumberGenerator)),
                randomNumberGenerator);
        }
        return new ProjectingIterable<>(sansCheckDigitGenerator.generateRandomValues(randomNumberGenerator),
            sansCheckDigit -> sansCheckDigit + calculateCheckDigit(sansCheckDigit));
    }

    private Iterable<String> generateAllInvalidRegexStrings() {
        return sansCheckDigitGenerator.complement().generateAllValues();
    }

    private Iterable<String> generateRandomInvalidRegexStrings(RandomNumberGenerator randomNumberGenerator) {
        return sansCheckDigitGenerator.complement().generateRandomValues(randomNumberGenerator);
    }

    private Iterable<String> generateAllInvalidCheckDigitStrings() {
        return generateInvalidCheckDigitStrings(sansCheckDigitGenerator::generateAllValues);
    }

    private Iterable<String> generateRandomInvalidCheckDigitStrings(RandomNumberGenerator randomNumberGenerator) {
        return generateInvalidCheckDigitStrings(
            () -> sansCheckDigitGenerator.generateRandomValues(randomNumberGenerator));
    }

    private Iterable<String> generateInvalidCheckDigitStrings(Supplier<Iterable<String>> sansCheckDigitSupplier) {
        return new FlatteningIterable<>(
            sansCheckDigitSupplier.get(),
            sansCheckDigit -> {
                final char checkDigit = calculateCheckDigit(sansCheckDigit);
                return IntStream.range(0, 10).boxed()
                    .map(digit -> Character.forDigit(digit, 10))
                    .filter(digit -> digit != checkDigit)
                    .map(digit -> sansCheckDigit + digit)
                    .collect(Collectors.toList());
            });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChecksummedCodeStringGenerator that = (ChecksummedCodeStringGenerator) o;
        return negate == that.negate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(negate, getClass());
    }
}

package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class ChecksummedCodeStringGenerator implements StringGenerator {
    protected final RegexStringGenerator regexGenerator;
    protected final boolean negate;

    public ChecksummedCodeStringGenerator(String generationPattern) {
        negate = false;
        regexGenerator = new RegexStringGenerator(generationPattern, true);
    }

    public ChecksummedCodeStringGenerator(RegexStringGenerator generator, boolean negate) {
        this.negate = negate;
        regexGenerator = generator;
    }

    public abstract char calculateCheckDigit(String str);

    public abstract String getRegexRepresentation();

    @Override
    public StringGenerator intersect(StringGenerator stringGenerator) {
        if (stringGenerator instanceof ChecksummedCodeStringGenerator) {
            ChecksummedCodeStringGenerator otherGenerator =
                (ChecksummedCodeStringGenerator)stringGenerator;
            return intersect(otherGenerator.negate ?
                otherGenerator.regexGenerator.complement() :
                otherGenerator.regexGenerator);
        }
        if (stringGenerator instanceof RegexStringGenerator) {
            return intersect((RegexStringGenerator)stringGenerator);
        }
        return new NoStringsStringGenerator(
            RegexStringGenerator.intersectRepresentation(stringGenerator.toString(), "<checksummed string>")
        );
    }

    private StringGenerator intersect(RegexStringGenerator other) {
        StringGenerator intersection = other.intersect(negate ? regexGenerator.complement() : regexGenerator);
        if ((intersection.isFinite() && intersection.getValueCount() == 0) || !(intersection instanceof RegexStringGenerator)) {
            return new NoStringsStringGenerator(
                RegexStringGenerator.intersectRepresentation(other.toString(), regexGenerator.toString())
            );
        }
        return instantiate((RegexStringGenerator)intersection);
    }

    abstract ChecksummedCodeStringGenerator instantiate(RegexStringGenerator generator);

    @Override
    public abstract StringGenerator complement();

    @Override
    public boolean isFinite() {
        return true;
    }

    @Override
    public long getValueCount() {
        return regexGenerator.getValueCount();
    }

    @Override
    public abstract boolean match(String subject);

    @Override
    public Iterable<String> generateInterestingValues() {
        if (negate) {
            return new ConcatenatingIterable<>(
                regexGenerator.complement().generateInterestingValues(),
                generateInvalidCheckDigitStrings(regexGenerator::generateInterestingValues));
        }
        return new FilteringIterable<>(
            new ProjectingIterable<>(
                regexGenerator.generateInterestingValues(),
                this::replaceLastCharWithCheckDigit
            ),
            regexGenerator::match
        );
    }

    @Override
    public Iterable<String> generateAllValues() {
        if (negate) {
            return new ConcatenatingIterable<>(
                generateAllInvalidRegexStrings(),
                generateAllInvalidCheckDigitStrings());
        }
        return new FilteringIterable<>(
            new ProjectingIterable<>(
                regexGenerator.generateAllValues(),
                this::replaceLastCharWithCheckDigit
            ),
            regexGenerator::match
        );
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
        return new FilteringIterable<>(
            new ProjectingIterable<>(
                regexGenerator.generateRandomValues(randomNumberGenerator),
                this::replaceLastCharWithCheckDigit
            ),
            regexGenerator::match
        );
    }

    private Iterable<String> generateAllInvalidRegexStrings() {
        return new FilteringIterable<>(
            regexGenerator.complement().generateAllValues(),
            str -> !str.isEmpty()
        );
    }

    private Iterable<String> generateRandomInvalidRegexStrings(RandomNumberGenerator randomNumberGenerator) {
        return new FilteringIterable<>(
            regexGenerator.complement().generateRandomValues(randomNumberGenerator),
            str -> !str.isEmpty()
        );
    }

    private Iterable<String> generateAllInvalidCheckDigitStrings() {
        return generateInvalidCheckDigitStrings(regexGenerator::generateAllValues);
    }

    private Iterable<String> generateRandomInvalidCheckDigitStrings(RandomNumberGenerator randomNumberGenerator) {
        return generateInvalidCheckDigitStrings(
            () -> regexGenerator.generateRandomValues(randomNumberGenerator));
    }

    private Iterable<String> generateInvalidCheckDigitStrings(Supplier<Iterable<String>> valueSupplier) {
        return new FlatteningIterable<>(
            valueSupplier.get(),
            initialValue -> {
                String sansCheckDigit = initialValue.substring(0, initialValue.length() - 1);
                final char checkDigit = calculateCheckDigit(sansCheckDigit);
                return IntStream.range(0, 10).boxed()
                    .map(digit -> Character.forDigit(digit, 10))
                    .filter(digit -> digit != checkDigit)
                    .map(digit -> sansCheckDigit + digit)
                    .collect(Collectors.toList());
            });
    }

    private String replaceLastCharWithCheckDigit(String str) {
        if (str.length() <= 1) { return str; }
        final String sansCheckDigit = str.substring(0, str.length() - 1);
        final char checkDigit = calculateCheckDigit(sansCheckDigit);
        return sansCheckDigit + checkDigit;
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

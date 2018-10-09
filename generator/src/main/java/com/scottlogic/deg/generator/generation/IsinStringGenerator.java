package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IsinStringGenerator implements IStringGenerator {
    private static final String GENERIC_NSIN_REGEX = "[A-Z0-9]{9}";

    private final boolean isNegated;

    public IsinStringGenerator() {
        this(false);
    }

    private IsinStringGenerator(boolean isNegated) {
        this.isNegated = isNegated;
    }

    @Override
    public IStringGenerator intersect(IStringGenerator stringGenerator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IStringGenerator complement() {
        return new IsinStringGenerator(!isNegated);
    }

    @Override
    public boolean isFinite() {
        return !isNegated;
    }

    @Override
    public long getValueCount() {
        return getAllCountryIsinGeneratorsAsStream()
            .map(IStringGenerator::getValueCount)
            .reduce(0L, Long::sum);
    }

    @Override
    public boolean match(String subject) {
        return IsinUtils.isValidIsin(subject);
    }

    @Override
    public Iterable<String> generateInterestingValues() {
        return new LimitingIterable<>(generateAllValues(), 1);
    }

    @Override
    public Iterable<String> generateAllValues() {
        if (isNegated) {
            return new ConcatenatingIterable<>(
                Arrays.asList(
                    generateAllInvalidCountryStrings(),
                    generateAllCountriesWithInvalidNsins(),
                    generateAllInvalidCheckDigitIsins()));
        }
        final List<Iterable<String>> countryCodeIterables = getAllCountryIsinGeneratorsAsStream()
            .map(isinSansCheckDigitGenerator ->
                new ProjectingIterable<>(isinSansCheckDigitGenerator.generateAllValues(),
                    isinSansCheckDigit -> isinSansCheckDigit + IsinUtils.calculateIsinCheckDigit(isinSansCheckDigit)))
            .collect(Collectors.toList());
        return new ConcatenatingIterable<>(countryCodeIterables);
    }

    @Override
    public Iterable<String> generateRandomValues(IRandomNumberGenerator randomNumberGenerator) {
        if (isNegated) {
            return new RandomMergingIterable<>(
                    Arrays.asList(
                            generateRandomInvalidCountryStrings(randomNumberGenerator),
                            generateRandomCountriesWithInvalidNsins(randomNumberGenerator),
                            generateRandomInvalidCheckDigitIsins(randomNumberGenerator)),
                    randomNumberGenerator);
        }
        final List<Iterable<String>> countryCodeIterables = getAllCountryIsinGeneratorsAsStream()
                .map(isinSansCheckDigitGenerator ->
                        new ProjectingIterable<>(isinSansCheckDigitGenerator.generateRandomValues(randomNumberGenerator),
                                isinSansCheckDigit -> isinSansCheckDigit + IsinUtils.calculateIsinCheckDigit(isinSansCheckDigit)))
                .collect(Collectors.toList());
        return new RandomMergingIterable<>(countryCodeIterables, randomNumberGenerator);
    }

    private Iterable<String> generateAllInvalidCountryStrings() {
        final String invalidCountryCodeRegex = IsinUtils.VALID_COUNTRY_CODES.stream()
            .collect(Collectors.joining("|", "((?!", ")).*"));
        return new RegexStringGenerator(invalidCountryCodeRegex, true).generateAllValues();
    }

    private Iterable<String> generateRandomInvalidCountryStrings(IRandomNumberGenerator randomNumberGenerator) {
        final String invalidCountryCodeRegex = IsinUtils.VALID_COUNTRY_CODES.stream()
            .collect(Collectors.joining("|", "((?!", ")).*"));
        return new RegexStringGenerator(invalidCountryCodeRegex, true).generateRandomValues(randomNumberGenerator);
    }

    private Iterable<String> generateAllCountriesWithInvalidNsins() {
        final List<Iterable<String>> countryWithInvalidNsinIterables = IsinUtils.VALID_COUNTRY_CODES.stream()
                .map(countryCode -> {
                    final IStringGenerator nsinGeneratorForCountry = getNsinGeneratorForCountry(countryCode);
                    final Iterable<String> invalidNsinIterators = nsinGeneratorForCountry.complement().generateAllValues();
                    return new ProjectingIterable<>(invalidNsinIterators, invalidNsin -> countryCode + invalidNsin);
                })
                .collect(Collectors.toList());
        return new FilteringIterable<>(new ConcatenatingIterable<>(countryWithInvalidNsinIterables),
                isin -> !IsinUtils.isValidIsin(isin));
    }

    private Iterable<String> generateRandomCountriesWithInvalidNsins(IRandomNumberGenerator randomNumberGenerator) {
        final List<Iterable<String>> countryWithInvalidNsinIterables = IsinUtils.VALID_COUNTRY_CODES.stream()
                .map(countryCode -> {
                    final IStringGenerator nsinGeneratorForCountry = getNsinGeneratorForCountry(countryCode);
                    final Iterable<String> invalidNsinIterators = nsinGeneratorForCountry.complement().generateRandomValues(randomNumberGenerator);
                    return new ProjectingIterable<>(invalidNsinIterators, invalidNsin -> countryCode + invalidNsin);
                })
                .collect(Collectors.toList());
        return new FilteringIterable<>(new RandomMergingIterable<>(countryWithInvalidNsinIterables, randomNumberGenerator),
                isin -> !IsinUtils.isValidIsin(isin));
    }

    private Iterable<String> generateAllInvalidCheckDigitIsins() {
        final List<Iterable<String>> countryCodeIterables = getAllCountryIsinGeneratorsAsStream()
                .map(isinSansCheckDigitGenerator ->
                        new FlatteningIterable<>(
                                isinSansCheckDigitGenerator.generateAllValues(),
                                isinSansCheckDigit -> {
                                    final char checkDigit = IsinUtils.calculateIsinCheckDigit(isinSansCheckDigit);
                                    return IntStream.range(0, 10).boxed()
                                            .map(digit -> Character.forDigit(digit, 10))
                                            .filter(digit -> digit != checkDigit)
                                            .map(digit -> isinSansCheckDigit + digit)
                                            .collect(Collectors.toList());
                                }))
                .collect(Collectors.toList());
        return new ConcatenatingIterable<>(countryCodeIterables);
    }

    private Iterable<String> generateRandomInvalidCheckDigitIsins(IRandomNumberGenerator randomNumberGenerator) {
        final List<Iterable<String>> countryCodeIterables = getAllCountryIsinGeneratorsAsStream()
                .map(isinSansCheckDigitGenerator ->
                        new FlatteningIterable<>(
                                isinSansCheckDigitGenerator.generateRandomValues(randomNumberGenerator),
                                isinSansCheckDigit -> {
                                    final char checkDigit = IsinUtils.calculateIsinCheckDigit(isinSansCheckDigit);
                                    return IntStream.range(0, 10).boxed()
                                            .map(digit -> Character.forDigit(digit, 10))
                                            .filter(digit -> digit != checkDigit)
                                            .map(digit -> isinSansCheckDigit + digit)
                                            .collect(Collectors.toList());
                                }))
                .collect(Collectors.toList());
        return new RandomMergingIterable<>(countryCodeIterables, randomNumberGenerator);
    }

    private Stream<IStringGenerator> getAllCountryIsinGeneratorsAsStream() {
        return IsinUtils.VALID_COUNTRY_CODES.stream()
                .map(IsinStringGenerator::getIsinSansCheckDigitGeneratorForCountry);
    }

    private static IStringGenerator getIsinSansCheckDigitGeneratorForCountry(String countryCode) {
        if (countryCode.equals("GB")) {
            return new SedolStringGenerator(countryCode);
        }
        return new RegexStringGenerator(countryCode + GENERIC_NSIN_REGEX, true);
    }

    private static IStringGenerator getNsinGeneratorForCountry(String countryCode) {
        if (countryCode.equals("GB")) {
            return new SedolStringGenerator();
        }
        return new RegexStringGenerator(GENERIC_NSIN_REGEX, true);
    }
}

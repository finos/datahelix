package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// There's a HUGE amount of copy-paste in this class. We should address that before we make any significant changes to it
public class IsinStringGenerator implements StringGenerator {
    public static final int ISIN_LENGTH = 12;
    private static final String GENERIC_NSIN_REGEX = "[A-Z0-9]{9}";

    // This generator is not used in generation itself, but is used to describe the possible
    // range of output values when combining with other string generators.
    private RegexStringGenerator isinRegexGenerator;

    private final boolean isNegated;

    public IsinStringGenerator() {
        this(getRegexGeneratorForAllLegalIsinFormats(), false);
    }

    private IsinStringGenerator(RegexStringGenerator regexGenerator, boolean isNegated) {
        this.isNegated = isNegated;
        isinRegexGenerator = regexGenerator;
    }

    @Override
    public StringGenerator intersect(StringGenerator stringGenerator) {
        if (stringGenerator instanceof IsinStringGenerator) {
            if (isNegated == ((IsinStringGenerator)stringGenerator).isNegated) {
                RegexStringGenerator otherRegexGenerator =
                    ((IsinStringGenerator) stringGenerator).isinRegexGenerator;
                return new IsinStringGenerator(
                    isNegated
                        ? isinRegexGenerator.union(otherRegexGenerator)
                        : (RegexStringGenerator)isinRegexGenerator.intersect(otherRegexGenerator),
                    isNegated
                );
            }
            return new NoStringsStringGenerator(
                RegexStringGenerator.intersectRepresentation(stringGenerator.toString(), "<ISIN>")
            );
        }
        if (stringGenerator instanceof ChecksummedCodeStringGenerator) {
            // Assume that no other checksummed string format we know about is going to be
            // compatible with the ISIN format.  This is true at the time of writing.
            return new NoStringsStringGenerator(
                RegexStringGenerator.intersectRepresentation(stringGenerator.toString(), "<ISIN>")
            );
        }
        if (stringGenerator instanceof RegexStringGenerator) {
            return intersect((RegexStringGenerator)stringGenerator);
        }
        return new NoStringsStringGenerator(
            RegexStringGenerator.intersectRepresentation(stringGenerator.toString(), "<ISIN>")
        );
    }

    private StringGenerator intersect(RegexStringGenerator other) {
        StringGenerator intersection =
            other.intersect(isNegated ? isinRegexGenerator.complement() : isinRegexGenerator);
        if ((intersection.isFinite() && intersection.getValueCount() == 0) ||
                !(intersection instanceof RegexStringGenerator)) {
            return new NoStringsStringGenerator(
                RegexStringGenerator.intersectRepresentation(
                    other.toString(),
                    isinRegexGenerator.toString()
                )
            );
        }
        if (!isNegated) {
            return new IsinStringGenerator((RegexStringGenerator)intersection, false);
        }
        return new IsinStringGenerator(
            isinRegexGenerator.union((RegexStringGenerator)other.complement()),
            true
        );
    }

    @Override
    public StringGenerator complement() {
        return new IsinStringGenerator(isinRegexGenerator, !isNegated);
    }

    @Override
    public boolean isFinite() {
        return !isNegated;
    }

    @Override
    public long getValueCount() {
        return getAllCountryIsinGeneratorsAsStream()
            .map(StringGenerator::getValueCount)
            .reduce(0L, Long::sum);
    }

    @Override
    public boolean match(String subject) {
        boolean matches = IsinUtils.isValidIsin(subject);
        return matches != isNegated;
    }

    @Override
    public Iterable<String> generateInterestingValues() {
        if (isNegated) {
            return new ConcatenatingIterable<>(
                Arrays.asList(
                    generateInterestingInvalidCountryStrings(),
                    generateInterestingCountriesWithInvalidNsins(),
                    generateInterestingInvalidCheckDigitIsins()));
        }
        final List<Iterable<String>> countryCodeIterables = getAllCountryIsinGeneratorsAsStream()
            .limit(2)
            .map(isinSansCheckDigitGenerator ->
                new ProjectingIterable<>(isinSansCheckDigitGenerator.generateInterestingValues(),
                    isinSansCheckDigit -> isinSansCheckDigit + IsinUtils.calculateIsinCheckDigit(isinSansCheckDigit)))
            .collect(Collectors.toList());
        return new ConcatenatingIterable<>(countryCodeIterables);
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
    public Iterable<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
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

    private Iterable<String> generateInterestingInvalidCountryStrings() {
        final String invalidCountryCodeRegex = IsinUtils.VALID_COUNTRY_CODES.stream()
            .limit(2)
            .collect(Collectors.joining("|", "((?!", ")).*"));
        return new RegexStringGenerator(invalidCountryCodeRegex, true).generateInterestingValues();
    }

    private Iterable<String> generateAllInvalidCountryStrings() {
        final String invalidCountryCodeRegex = IsinUtils.VALID_COUNTRY_CODES.stream()
            .collect(Collectors.joining("|", "((?!", ")).*"));
        return new RegexStringGenerator(invalidCountryCodeRegex, true).generateAllValues();
    }

    private Iterable<String> generateRandomInvalidCountryStrings(RandomNumberGenerator randomNumberGenerator) {
        final String invalidCountryCodeRegex = IsinUtils.VALID_COUNTRY_CODES.stream()
            .collect(Collectors.joining("|", "((?!", ")).*"));
        return new RegexStringGenerator(invalidCountryCodeRegex, true).generateRandomValues(randomNumberGenerator);
    }

    private static Iterable<String> generateInterestingCountriesWithInvalidNsins() {
        final List<Iterable<String>> countryWithInvalidNsinIterables = IsinUtils.VALID_COUNTRY_CODES.stream()
            .limit(2)
            .map(countryCode -> {
                final StringGenerator nsinGeneratorForCountry = getNsinGeneratorForCountry(countryCode);
                final Iterable<String> invalidNsinIterators = nsinGeneratorForCountry.complement().generateInterestingValues();
                return new ProjectingIterable<>(invalidNsinIterators, invalidNsin -> countryCode + invalidNsin);
            })
            .collect(Collectors.toList());
        return new FilteringIterable<>(new ConcatenatingIterable<>(countryWithInvalidNsinIterables),
            isin -> !IsinUtils.isValidIsin(isin));
    }

    private static Iterable<String> generateAllCountriesWithInvalidNsins() {
        final List<Iterable<String>> countryWithInvalidNsinIterables = IsinUtils.VALID_COUNTRY_CODES.stream()
                .map(countryCode -> {
                    final StringGenerator nsinGeneratorForCountry = getNsinGeneratorForCountry(countryCode);
                    final Iterable<String> invalidNsinIterators = nsinGeneratorForCountry.complement().generateAllValues();
                    return new ProjectingIterable<>(invalidNsinIterators, invalidNsin -> countryCode + invalidNsin);
                })
                .collect(Collectors.toList());
        return new FilteringIterable<>(new ConcatenatingIterable<>(countryWithInvalidNsinIterables),
                isin -> !IsinUtils.isValidIsin(isin));
    }

    private static Iterable<String> generateRandomCountriesWithInvalidNsins(RandomNumberGenerator randomNumberGenerator) {
        final List<Iterable<String>> countryWithInvalidNsinIterables = IsinUtils.VALID_COUNTRY_CODES.stream()
                .map(countryCode -> {
                    final StringGenerator nsinGeneratorForCountry = getNsinGeneratorForCountry(countryCode);
                    final Iterable<String> invalidNsinIterators = nsinGeneratorForCountry.complement().generateRandomValues(randomNumberGenerator);
                    return new ProjectingIterable<>(invalidNsinIterators, invalidNsin -> countryCode + invalidNsin);
                })
                .collect(Collectors.toList());
        return new FilteringIterable<>(new RandomMergingIterable<>(countryWithInvalidNsinIterables, randomNumberGenerator),
                isin -> !IsinUtils.isValidIsin(isin));
    }

    private Iterable<String> generateInterestingInvalidCheckDigitIsins() {
        final List<Iterable<String>> countryCodeIterables = getAllCountryIsinGeneratorsAsStream()
            .limit(2)
            .map(isinSansCheckDigitGenerator ->
                new FlatteningIterable<>(
                    isinSansCheckDigitGenerator.generateInterestingValues(),
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

    private Iterable<String> generateRandomInvalidCheckDigitIsins(RandomNumberGenerator randomNumberGenerator) {
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

    private Stream<StringGenerator> getAllCountryIsinGeneratorsAsStream() {
        return IsinUtils.VALID_COUNTRY_CODES.stream()
                .map(IsinStringGenerator::getIsinSansCheckDigitGeneratorForCountry);
    }

    private static StringGenerator getIsinSansCheckDigitGeneratorForCountry(String countryCode) {
        if (countryCode.equals("GB")) {
            return new SedolStringGenerator("GB00");
        }
        if (countryCode.equals("US")) {
            return new CusipStringGenerator("US");
        }
        return new RegexStringGenerator(countryCode + GENERIC_NSIN_REGEX, true);
    }

    private static RegexStringGenerator getRegexGeneratorForAllLegalIsinFormats() {
        Stream<RegexStringGenerator> countryGenerators = IsinUtils.VALID_COUNTRY_CODES
            .stream()
            .map(
                country -> new RegexStringGenerator(getIsinRegexRepresentationForCountry(country), true)
        );
        RegexStringGenerator.UnionCollector collector = countryGenerators.collect(
            RegexStringGenerator.UnionCollector::new,
            RegexStringGenerator.UnionCollector::accumulate,
            RegexStringGenerator.UnionCollector::combine
        );
        return collector.getUnionGenerator();
    }

    private static String getIsinRegexRepresentationForCountry(String countryCode) {
        String sansCheckDigit;
        if (countryCode.equals("GB")) {
            sansCheckDigit = "GB00" + SedolStringGenerator.STANDARD_REGEX_REPRESENTATION;
        }
        else if (countryCode.equals("US")) {
            sansCheckDigit = "US" + CusipStringGenerator.STANDARD_REGEX_REPRESENTATION;
        }
        else {
            sansCheckDigit = countryCode + GENERIC_NSIN_REGEX;
        }
        return sansCheckDigit + "[0-9]";
    }

    private static StringGenerator getNsinGeneratorForCountry(String countryCode) {
        if (countryCode.equals("GB")) {
            return new SedolStringGenerator("00");
        }
        if (countryCode.equals("US")) {
            return new CusipStringGenerator();
        }
        return new RegexStringGenerator(GENERIC_NSIN_REGEX, true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IsinStringGenerator that = (IsinStringGenerator) o;
        return isNegated == that.isNegated;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isNegated, getClass());
    }
}

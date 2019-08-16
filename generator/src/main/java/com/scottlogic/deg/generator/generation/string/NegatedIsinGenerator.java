/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scottlogic.deg.generator.generation.string;

import com.scottlogic.deg.generator.utils.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class NegatedIsinGenerator implements StringGenerator {
    private static final String GENERIC_NSIN_REGEX = "[A-Z0-9]{9}";

    // This generator is not used in generation itself, but is used to describe the possible
    // range of output values when combining with other string generators.
    private RegexStringGenerator isinRegexGenerator;
    private StringGenerator notIsinRegexGenerator;


    public NegatedIsinGenerator(RegexStringGenerator isinRegexGenerator) {
        this.isinRegexGenerator = isinRegexGenerator;
        notIsinRegexGenerator = isinRegexGenerator.complement();
    }

    @Override
    public StringGenerator intersect(StringGenerator stringGenerator) {
        if (stringGenerator instanceof NegatedIsinGenerator) {
            RegexStringGenerator otherRegexGenerator =
                ((NegatedIsinGenerator) stringGenerator).isinRegexGenerator;
            return new NegatedIsinGenerator(isinRegexGenerator.union(otherRegexGenerator));
        }
        if (stringGenerator instanceof IsinStringGenerator) {
            return new NoStringsStringGenerator(
                RegexStringGenerator.intersectRepresentation(stringGenerator.toString(), "<ISIN>")
            );
        }
        if (stringGenerator instanceof ChecksummedCodeStringGenerator) {
            return stringGenerator;
        }
        if (stringGenerator instanceof RegexStringGenerator) {
            return stringGenerator.intersect(notIsinRegexGenerator);
        }
        return new NoStringsStringGenerator(
            RegexStringGenerator.intersectRepresentation(stringGenerator.toString(), "<ISIN>")
        );
    }

    @Override
    public StringGenerator complement() {
        return new IsinStringGenerator(isinRegexGenerator);
    }

    @Override
    public boolean match(String subject) {
        return !FinancialCodeUtils.isValidIsin(subject);
    }

    @Override
    public Iterable<String> generateInterestingValues() {
        return new ConcatenatingIterable<>(
            Arrays.asList(
                generateInterestingInvalidCountryStrings(),
                generateInterestingCountriesWithInvalidNsins(),
                generateInterestingInvalidCheckDigitIsins()));
    }

    @Override
    public Iterable<String> generateAllValues() {
        return new ConcatenatingIterable<>(
            Arrays.asList(
                generateAllInvalidCountryStrings(),
                generateAllCountriesWithInvalidNsins(),
                generateAllInvalidCheckDigitIsins()));
    }

    @Override
    public Iterable<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return new RandomMergingIterable<>(
            Arrays.asList(
                generateRandomInvalidCountryStrings(randomNumberGenerator),
                generateRandomCountriesWithInvalidNsins(randomNumberGenerator),
                generateRandomInvalidCheckDigitIsins(randomNumberGenerator)),
            randomNumberGenerator);
    }

    private Iterable<String> generateInterestingInvalidCountryStrings() {
        final String invalidCountryCodeRegex = FinancialCodeUtils.VALID_COUNTRY_CODES.stream()
            .limit(2)
            .collect(Collectors.joining("|", "((?!", ")).*"));
        return new RegexStringGenerator(invalidCountryCodeRegex, true).generateInterestingValues();
    }

    private Iterable<String> generateAllInvalidCountryStrings() {
        final String invalidCountryCodeRegex = FinancialCodeUtils.VALID_COUNTRY_CODES.stream()
            .collect(Collectors.joining("|", "((?!", ")).*"));
        return new RegexStringGenerator(invalidCountryCodeRegex, true).generateAllValues();
    }

    private Iterable<String> generateRandomInvalidCountryStrings(RandomNumberGenerator randomNumberGenerator) {
        final String invalidCountryCodeRegex = FinancialCodeUtils.VALID_COUNTRY_CODES.stream()
            .collect(Collectors.joining("|", "((?!", ")).*"));
        return new RegexStringGenerator(invalidCountryCodeRegex, true).generateRandomValues(randomNumberGenerator);
    }

    private static Iterable<String> generateInterestingCountriesWithInvalidNsins() {
        final List<Iterable<String>> countryWithInvalidNsinIterables = FinancialCodeUtils.VALID_COUNTRY_CODES.stream()
            .limit(2)
            .map(countryCode -> {
                final StringGenerator nsinGeneratorForCountry = getNsinGeneratorForCountry(countryCode);
                final Iterable<String> invalidNsinIterators = nsinGeneratorForCountry.complement().generateInterestingValues();
                return new ProjectingIterable<>(invalidNsinIterators, invalidNsin -> countryCode + invalidNsin);
            })
            .collect(Collectors.toList());
        return new FilteringIterable<>(new ConcatenatingIterable<>(countryWithInvalidNsinIterables),
            isin -> !FinancialCodeUtils.isValidIsin(isin));
    }

    private static Iterable<String> generateAllCountriesWithInvalidNsins() {
        final List<Iterable<String>> countryWithInvalidNsinIterables = FinancialCodeUtils.VALID_COUNTRY_CODES.stream()
            .map(countryCode -> {
                final StringGenerator nsinGeneratorForCountry = getNsinGeneratorForCountry(countryCode);
                final Iterable<String> invalidNsinIterators = nsinGeneratorForCountry.complement().generateAllValues();
                return new ProjectingIterable<>(invalidNsinIterators, invalidNsin -> countryCode + invalidNsin);
            })
            .collect(Collectors.toList());
        return new FilteringIterable<>(new ConcatenatingIterable<>(countryWithInvalidNsinIterables),
            isin -> !FinancialCodeUtils.isValidIsin(isin));
    }

    private static Iterable<String> generateRandomCountriesWithInvalidNsins(RandomNumberGenerator randomNumberGenerator) {
        final List<Iterable<String>> countryWithInvalidNsinIterables = FinancialCodeUtils.VALID_COUNTRY_CODES.stream()
            .map(countryCode -> {
                final StringGenerator nsinGeneratorForCountry = getNsinGeneratorForCountry(countryCode);
                final Iterable<String> invalidNsinIterators = nsinGeneratorForCountry.complement().generateRandomValues(randomNumberGenerator);
                return new ProjectingIterable<>(invalidNsinIterators, invalidNsin -> countryCode + invalidNsin);
            })
            .collect(Collectors.toList());
        return new FilteringIterable<>(new RandomMergingIterable<>(countryWithInvalidNsinIterables, randomNumberGenerator),
            isin -> !FinancialCodeUtils.isValidIsin(isin));
    }

    private Iterable<String> generateInterestingInvalidCheckDigitIsins() {
        final List<Iterable<String>> countryCodeIterables = getAllCountryIsinGeneratorsAsStream()
            .limit(2)
            .map(isinSansCheckDigitGenerator ->
                new FlatteningIterable<>(
                    isinSansCheckDigitGenerator.generateInterestingValues(),
                    isinSansCheckDigit -> {
                        final char checkDigit = FinancialCodeUtils.calculateIsinCheckDigit(isinSansCheckDigit);
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
                        final char checkDigit = FinancialCodeUtils.calculateIsinCheckDigit(isinSansCheckDigit);
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
                        final char checkDigit = FinancialCodeUtils.calculateIsinCheckDigit(isinSansCheckDigit);
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
        return FinancialCodeUtils.VALID_COUNTRY_CODES.stream()
            .map(this::getIsinGeneratorForCountry);
    }

    private StringGenerator getIsinGeneratorForCountry(String countryCode) {
        if (countryCode.equals("GB")) {
            return new SedolStringGenerator("GB00", "[0-9]", isinRegexGenerator);
        }
        if (countryCode.equals("US")) {
            return new CusipStringGenerator("US", "[0-9]", isinRegexGenerator);
        }
        return new RegexStringGenerator(countryCode + GENERIC_NSIN_REGEX + "[0-9]", true);
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

        NegatedIsinGenerator that = (NegatedIsinGenerator) o;
        return Objects.equals(this.isinRegexGenerator, that.isinRegexGenerator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isinRegexGenerator);
    }
}

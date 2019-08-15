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

import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.utils.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottlogic.deg.generator.generation.string.streamy.ChecksumStringGeneratorFactory.createIsinGenerator;

public class IsinStringGenerator implements StringGenerator {
    public static final int ISIN_LENGTH = 12;
    private static final String GENERIC_NSIN_REGEX = "[A-Z0-9]{9}";

    // This generator is not used in generation itself, but is used to describe the possible
    // range of output values when combining with other string generators.
    private RegexStringGenerator isinRegexGenerator;

    public IsinStringGenerator() {
        this(getRegexGeneratorForAllLegalIsinFormats());
    }

    IsinStringGenerator(RegexStringGenerator regexGenerator) {
        isinRegexGenerator = regexGenerator;
    }

    @Override
    public StringGenerator intersect(StringGenerator stringGenerator) {
        if (stringGenerator instanceof IsinStringGenerator) {
            return
                new IsinStringGenerator((RegexStringGenerator)isinRegexGenerator
                .intersect(((IsinStringGenerator) stringGenerator).isinRegexGenerator));
        }
        if (stringGenerator instanceof NegatedIsinGenerator) {
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
            other.intersect(isinRegexGenerator);
        if (!(intersection instanceof RegexStringGenerator)) {
            return new NoStringsStringGenerator(
                RegexStringGenerator.intersectRepresentation(
                    other.toString(),
                    isinRegexGenerator.toString()
                )
            );
        }
        return new IsinStringGenerator((RegexStringGenerator)intersection);
    }

    @Override
    public StringGenerator complement() {
        return new NegatedIsinGenerator(isinRegexGenerator);
    }

    @Override
    public boolean match(String subject) {
        return FinancialCodeUtils.isValidIsin(subject);
    }

    @Override
    public Iterable<String> generateInterestingValues() {
        final List<Iterable<String>> countryCodeIterables = getAllCountryIsinGeneratorsAsStream()
            .limit(2)
            .map(generator -> IterableUtils.wrapIterableWithProjectionAndFilter(
                generator.generateInterestingValues(),
                this::replaceCheckDigit,
                isinRegexGenerator::match))
            .collect(Collectors.toList());
        return new ConcatenatingIterable<>(countryCodeIterables);
    }

    @Override
    public Iterable<String> generateAllValues() {
        final List<Iterable<String>> countryCodeIterables = getAllCountryIsinGeneratorsAsStream()
            .map(generator -> new FilteringIterable<>(
                generator.generateAllValues(),
                (x) -> x.equals(replaceCheckDigit(x))))
            .collect(Collectors.toList());
        return new ConcatenatingIterable<>(countryCodeIterables);
    }

    @Override
    public Iterable<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        final List<Iterable<String>> countryCodeIterables = getAllCountryIsinGeneratorsAsStream()
            .map(generator -> IterableUtils.wrapIterableWithProjectionAndFilter(
                generator.generateRandomValues(randomNumberGenerator),
                this::replaceCheckDigit,
                isinRegexGenerator::match
            ))
            .collect(Collectors.toList());
        return new RandomMergingIterable<>(countryCodeIterables, randomNumberGenerator);
    }

    private String replaceCheckDigit(String isin) {
        String isinWithoutCheckDigit = isin.substring(0, isin.length() - 1);
        return isinWithoutCheckDigit + FinancialCodeUtils.calculateIsinCheckDigit(isinWithoutCheckDigit);
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

    private static RegexStringGenerator getRegexGeneratorForAllLegalIsinFormats() {
        Stream<RegexStringGenerator> countryGenerators = FinancialCodeUtils.VALID_COUNTRY_CODES
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

    private static String getIsinRegexRepresentationForCountrySansCheckDigit(String countryCode) {
        if (countryCode.equals("GB")) {
            return "GB00" + SedolStringGenerator.STANDARD_REGEX_REPRESENTATION;
        }
        else if (countryCode.equals("US")) {
            return "US" + CusipStringGenerator.STANDARD_REGEX_REPRESENTATION;
        }
        else {
            return countryCode + GENERIC_NSIN_REGEX;
        }
    }

    private static String getIsinRegexRepresentationForCountry(String countryCode) {
        return getIsinRegexRepresentationForCountrySansCheckDigit(countryCode) + "[0-9]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IsinStringGenerator that = (IsinStringGenerator) o;
        return Objects.equals(this.isinRegexGenerator, that.isinRegexGenerator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isinRegexGenerator);
    }

    @Override
    public FieldValueSource asFieldValueSource(){
        return new StreamStringGeneratorAsFieldValueSource(createIsinGenerator());
    }
}

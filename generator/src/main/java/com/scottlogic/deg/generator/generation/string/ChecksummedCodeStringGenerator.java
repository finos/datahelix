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
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class ChecksummedCodeStringGenerator implements StringGenerator {
    protected final StringGenerator regexGenerator;
    protected final boolean negate;
    protected final int prefixLength;
    protected final int codeLength;

    public ChecksummedCodeStringGenerator(
        String generationPattern,
        int codeLength,
        int prefixLength
    ) {
        this(
            new RegexStringGenerator(generationPattern, true),
            false,
            codeLength,
            prefixLength
        );
    }

    public ChecksummedCodeStringGenerator(
        String generationPattern,
        RegexStringGenerator intersectingGenerator,
        int codeLength,
        int prefixLength
    ) {
        this(
            new RegexStringGenerator(generationPattern, true)
                .intersect(intersectingGenerator),
            false,
            codeLength,
            prefixLength
        );
    }

    public ChecksummedCodeStringGenerator(
        StringGenerator generator,
        boolean negate,
        int codeLength,
        int prefixLength
    ) {
        this.negate = negate;
        regexGenerator = generator;
        this.codeLength = codeLength;
        this.prefixLength = prefixLength;
    }

    public abstract char calculateCheckDigit(String str);

    public abstract int getLength();

    public String fixCheckDigit(String str) {
        char checkDigit = calculateCheckDigit(str);
        int codeLength = getLength();
        if (str.length() > prefixLength + codeLength) {
            return str.substring(0, prefixLength + codeLength - 1) +
                checkDigit + str.substring(prefixLength + codeLength);
        }
        return str.substring(0, str.length() - 1) + checkDigit;
    }

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
        if (!intersection.generateAllValues().iterator().hasNext() || !(intersection instanceof RegexStringGenerator)) {
            return new NoStringsStringGenerator(
                RegexStringGenerator.intersectRepresentation(other.toString(), regexGenerator.toString())
            );
        }
        return instantiate((RegexStringGenerator)intersection);
    }

    abstract ChecksummedCodeStringGenerator instantiate(RegexStringGenerator generator);

    @Override
    public Iterable<String> generateInterestingValues() {
        if (negate) {
            return new ConcatenatingIterable<>(
                regexGenerator.complement().generateInterestingValues(),
                generateInvalidCheckDigitStrings(regexGenerator::generateInterestingValues));
        }
        return wrapIterableWithProjectionAndFilter(regexGenerator.generateInterestingValues());
    }

    @Override
    public Iterable<String> generateAllValues() {
        if (negate) {
            return new ConcatenatingIterable<>(
                generateAllInvalidRegexStrings(),
                generateAllInvalidCheckDigitStrings());
        }
        return new FilteringIterable<>(
            regexGenerator.generateAllValues(),
            (x) -> x.equals(fixCheckDigit(x)));
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
        return wrapIterableWithProjectionAndFilter(
            regexGenerator.generateRandomValues(randomNumberGenerator)
        );
    }

    private Iterable<String> wrapIterableWithProjectionAndFilter(Iterable<String> iterable) {
        return IterableUtils.wrapIterableWithProjectionAndFilter(
            iterable,
            this::fixCheckDigit,
            regexGenerator::match
        );
    }

    private Iterable<String> generateAllInvalidRegexStrings() {
        return IterableUtils.wrapIterableWithNonEmptyStringCheck(
            regexGenerator.complement().generateAllValues()
        );
    }

    private Iterable<String> generateRandomInvalidRegexStrings(RandomNumberGenerator randomNumberGenerator) {
        return IterableUtils.wrapIterableWithNonEmptyStringCheck(
            regexGenerator.complement().generateRandomValues(randomNumberGenerator)
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
                String sansCheckDigit = initialValue.substring(
                    prefixLength,
                    prefixLength + codeLength - 1
                );
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

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
package com.scottlogic.datahelix.generator.core.utils;

import com.scottlogic.datahelix.generator.common.SetUtils;
import com.scottlogic.datahelix.generator.common.util.Defaults;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JavaUtilRandomNumberGeneratorTest {

    JavaUtilRandomNumberGenerator random = new JavaUtilRandomNumberGenerator();

    @Test
    void nextBigDecimal_withPositiveSameScale_works() {
        BigDecimal lower = BigDecimal.ONE;
        BigDecimal upper = BigDecimal.valueOf(3);

        Set<BigDecimal> expected = SetUtils.setOf(BigDecimal.ONE, BigDecimal.valueOf(2));

        assertTrue(Stream.generate(() -> random.nextBigDecimal(lower, upper))
            .limit(10)
            .allMatch(expected::contains));
    }

    @Test
    void nextBigDecimal_withPositiveDifferentScales_work() {
        BigDecimal lower = BigDecimal.valueOf(9);
        BigDecimal upper = BigDecimal.valueOf(12);

        Set<BigDecimal> expected = SetUtils.setOf(
            BigDecimal.valueOf(9),
            BigDecimal.valueOf(10),
            BigDecimal.valueOf(11));

        assertTrue(Stream.generate(() -> random.nextBigDecimal(lower, upper))
            .limit(10)
            .allMatch(expected::contains));
    }

    @Test
    void nextBigDecimal_withNegativeSameScale_works() {
        BigDecimal lower = new BigDecimal("0.1");
        BigDecimal upper = new BigDecimal("0.4");

        Set<BigDecimal> expected = SetUtils.setOf(lower, new BigDecimal("0.2"), new BigDecimal("0.3"));

        assertTrue(Stream.generate(() -> random.nextBigDecimal(lower, upper))
            .limit(10)
            .allMatch(expected::contains));
    }

    @Test
    void nextBigDecimal_withNegativeDifferentScales_work() {
        BigDecimal lower = new BigDecimal("0.08");
        BigDecimal upper = new BigDecimal("0.1");

        Set<BigDecimal> expected = SetUtils.setOf(lower, new BigDecimal("0.09"));

        assertTrue(Stream.generate(() -> random.nextBigDecimal(lower, upper))
            .limit(10)
            .allMatch(expected::contains));
    }

    @Test
    void nextBigDecimal_withPositiveAndNegativeScales_works() {
        BigDecimal lower = new BigDecimal("0.9");
        BigDecimal upper = BigDecimal.TEN;

        assertTrue(Stream.generate(() -> random.nextBigDecimal(lower, upper))
            .limit(10)
            .allMatch(x -> BigDecimal.TEN.compareTo(x) > 0 && lower.compareTo(x) <= 0));
    }

    @Test
    void nextBigDecimal_withLargeNumbers_givesValuesWithHighPrecision() {
        BigDecimal lower = Defaults.NUMERIC_MIN.setScale(20);
        BigDecimal upper = Defaults.NUMERIC_MAX.setScale(20);

        Stream<String> result = Stream.generate(() -> random.nextBigDecimal(lower, upper))
            .map(BigDecimal::toPlainString)
            .peek(System.out::println)
            .map(this::lastFourLetters);

        assertTrue(result.limit(10).noneMatch("0000"::equals));
    }

    private String lastFourLetters(String input) {
        return input.substring(input.length() - 4);
    }
}

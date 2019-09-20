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

import com.scottlogic.deg.generator.generation.string.generators.RegexStringGenerator;
import com.scottlogic.deg.generator.generation.string.generators.StringGenerator;
import com.scottlogic.deg.generator.utils.FinancialCodeUtils;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import static com.scottlogic.deg.generator.generation.string.generators.ChecksumStringGeneratorFactory.createIsinGenerator;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IsinStringGeneratorTests {

    @Test
    public void shouldEndAllIsinsWithValidCheckDigit() {
        StringGenerator target = createIsinGenerator();
        final int NumberOfTests = 100;

        final Iterator<String> allIsins = target.generateAllValues().iterator();

        for (int ii = 0; ii < NumberOfTests; ++ii) {
            final String nextIsin = allIsins.next();
            final char checkDigit = FinancialCodeUtils.calculateIsinCheckDigit(nextIsin.substring(0, 11));
            assertThat(nextIsin.charAt(11), equalTo(checkDigit));
        }
    }

    @Test
    public void shouldEndAllRandomIsinsWithValidCheckDigit() {
        StringGenerator target = createIsinGenerator();

        final int NumberOfTests = 100;

        final Iterator<String> allIsins = target.generateRandomValues(new JavaUtilRandomNumberGenerator()).iterator();

        for (int ii = 0; ii < NumberOfTests; ++ii) {
            final String nextIsin = allIsins.next();
            final char checkDigit = FinancialCodeUtils.calculateIsinCheckDigit(nextIsin.substring(0, 11));
            assertThat(nextIsin.charAt(11), equalTo(checkDigit));
        }
    }

    @Test
    public void shouldUseSedolWhenCountryIsGB() {
        AtomicInteger numberOfIsinsTested = new AtomicInteger(0);
        createIsinGenerator().generateRandomValues(new JavaUtilRandomNumberGenerator())
            .filter(isinString -> isinString.substring(0, 2).equals("GB"))
            .limit(100)
            .forEach(isinString -> {
                assertThat(
                    FinancialCodeUtils.isValidSedolNsin(isinString.substring(2, 11)), is(true));

                numberOfIsinsTested.incrementAndGet();
            });

        // make sure we tested the number we expected
        assertThat(numberOfIsinsTested.get(), equalTo(100));
    }

    @Test
    public void shouldUseCusipWhenCountryIsUS() {
        AtomicInteger numberOfIsinsTested = new AtomicInteger(0);
        createIsinGenerator().generateRandomValues(new JavaUtilRandomNumberGenerator())
            .filter(isinString -> isinString.substring(0, 2).equals("US"))
            .limit(100)
            .forEach(isinString -> {
                assertThat(
                    FinancialCodeUtils.isValidCusipNsin(isinString.substring(2, 11)), is(true));

                numberOfIsinsTested.incrementAndGet();
            });

        // make sure we tested the number we expected
        assertThat(numberOfIsinsTested.get(), equalTo(100));
    }

    @Test
    public void shouldUseGeneralRegexWhenCountryIsNotGbOrUs() {
        AtomicInteger numberOfIsinsTested = new AtomicInteger(0);
        createIsinGenerator().generateRandomValues(new JavaUtilRandomNumberGenerator())
            .filter(isinString -> {
                String countryCode = isinString.substring(0, 2);
                return !countryCode.equals("GB") && !countryCode.equals("US");
            })
            .limit(100)
            .forEach(isinString -> {
                String APPROX_ISIN_REGEX = "[A-Z]{2}[A-Z0-9]{9}[0-9]";
                RegexStringGenerator regexStringGenerator = new RegexStringGenerator(APPROX_ISIN_REGEX, true);
                assertTrue(regexStringGenerator.matches(isinString));

                numberOfIsinsTested.incrementAndGet();
            });

        // make sure we tested the number we expected
        assertThat(numberOfIsinsTested.get(), equalTo(100));
    }

    @Test
    public void shouldMatchAValidIsinCodeWhenNotNegated(){
        StringGenerator isinGenerator = createIsinGenerator();

        boolean matches = isinGenerator.matches("GB0002634946");

        Assert.assertTrue(matches);
    }

    @Test
    public void shouldNotMatchAnInvalidIsinCodeWhenNotNegated(){
        StringGenerator isinGenerator = createIsinGenerator();

        boolean matches = isinGenerator.matches("not an isin");

        Assert.assertFalse(matches);
    }
}

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

import com.scottlogic.deg.generator.generation.fieldvaluesources.datetime.DateTimeFieldValueSourceTests;
import com.scottlogic.deg.generator.generation.string.generators.StringGenerator;
import com.scottlogic.deg.generator.utils.FinancialCodeUtils;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import static com.scottlogic.deg.generator.generation.string.generators.ChecksumStringGeneratorFactory.createIsinGenerator;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    @Disabled("Standard constraints e.g. ISINs currently cannot be negated")
    public void complementShouldProduceNoRandomValidIsins() {
        StringGenerator target = createIsinGenerator().complement();

        final int NumberOfTests = 100;

        final Iterator<String> allIsins = target.generateRandomValues(new JavaUtilRandomNumberGenerator()).iterator();

        for (int ii = 0; ii < NumberOfTests; ++ii) {
            final String nextIsin = allIsins.next();
            assertThat(FinancialCodeUtils.isValidIsin(nextIsin), is(false));
        }
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

    @Test
    @Disabled("Standard constraints e.g. ISINs currently cannot be negated")
    public void shouldNotMatchAValidIsinCodeWhenNegated(){
        StringGenerator isinGenerator = createIsinGenerator().complement();

        boolean matches = isinGenerator.matches("GB0002634946");

        Assert.assertFalse(matches);
    }

    @Test
    @Disabled("Standard constraints e.g. ISINs currently cannot be negated")
    public void shouldMatchAnInvalidIsinCodeWhenNegated(){
        StringGenerator isinGenerator = createIsinGenerator().complement();

        boolean matches = isinGenerator.matches("not an isin");

        Assert.assertTrue(matches);
    }
}

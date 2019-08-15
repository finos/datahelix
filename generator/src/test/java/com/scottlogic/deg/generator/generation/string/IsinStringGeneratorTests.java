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

import com.scottlogic.deg.generator.utils.FinancialCodeUtils;
import com.scottlogic.deg.generator.utils.IterableAsStream;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IsinStringGeneratorTests {

    @Test
    public void shouldEndAllIsinsWithValidCheckDigit() {
        IsinStringGenerator target = new IsinStringGenerator();
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
        IsinStringGenerator target = new IsinStringGenerator();

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
        // this assumes that the first batch of values produced by the generator are GB-flavoured. If this changes in the future, this test might need to get more complicated

        AtomicInteger numberOfIsinsTested = new AtomicInteger(0);
        IterableAsStream.convert(new IsinStringGenerator().generateAllValues())
            .limit(100)
            .forEach(isinString -> {
                if (!isinString.substring(0, 2).equals("GB"))
                    throw new IllegalStateException("Test assumes that the first 100 ISINs will be GB-flavoured");

                assertThat(
                    FinancialCodeUtils.isValidSedolNsin(isinString.substring(2, 11)), is(true));

                numberOfIsinsTested.incrementAndGet();
            });

        // make sure we tested the number we expected
        assertThat(numberOfIsinsTested.get(), equalTo(100));
    }

    @Test
    public void complementShouldProduceNoRandomValidIsins() {
        StringGenerator target = new IsinStringGenerator().complement();

        final int NumberOfTests = 100;

        final Iterator<String> allIsins = target.generateRandomValues(new JavaUtilRandomNumberGenerator()).iterator();

        for (int ii = 0; ii < NumberOfTests; ++ii) {
            final String nextIsin = allIsins.next();
            assertThat(FinancialCodeUtils.isValidIsin(nextIsin), is(false));
        }
    }

    @Test
    public void shouldMatchAValidIsinCodeWhenNotNegated(){
        StringGenerator isinGenerator = new IsinStringGenerator();

        boolean matches = isinGenerator.match("GB0002634946");

        Assert.assertTrue(matches);
    }

    @Test
    public void shouldNotMatchAnInvalidIsinCodeWhenNotNegated(){
        StringGenerator isinGenerator = new IsinStringGenerator();

        boolean matches = isinGenerator.match("not an isin");

        Assert.assertFalse(matches);
    }

    @Test
    public void shouldNotMatchAValidIsinCodeWhenNegated(){
        StringGenerator isinGenerator = new IsinStringGenerator().complement();

        boolean matches = isinGenerator.match("GB0002634946");

        Assert.assertFalse(matches);
    }

    @Test
    public void shouldMatchAnInvalidIsinCodeWhenNegated(){
        StringGenerator isinGenerator = new IsinStringGenerator().complement();

        boolean matches = isinGenerator.match("not an isin");

        Assert.assertTrue(matches);
    }
}

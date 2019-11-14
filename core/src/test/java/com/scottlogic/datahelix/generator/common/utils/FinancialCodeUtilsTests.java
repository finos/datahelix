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

package com.scottlogic.datahelix.generator.common.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FinancialCodeUtilsTests {

    private static final List<String> VALID_ISINS = Arrays.asList(
            "US0378331005", "US5949181045", "US38259P5089",
            "GB0000566504", "GB0001411924", "GB0002162385", "GB0002374006", "GB0002634946"
    );

    private static final List<String> INCORRECT_CHECK_DIGIT_ISINS = Arrays.asList(
            "US5949181042", "US38259P5084", "US0378331006"
    );

    private static final List<String> INCORRECT_SEDOL_ISINS = Arrays.asList(
            "GB0000566512", "GB0001411932", "GB0002162369", "GB0002374022", "GB0002634954"
    );

    private static final List<String> INCORRECT_CUSIP_ISINS = Arrays.asList(
            "US0378331025", "US5949181079", "US38259P5055"
    );

    @Test
    public void testCorrectCheckDigitIsCalculated() {
        for (String isin : VALID_ISINS) {
            final char checkDigit = FinancialCodeUtils.calculateIsinCheckDigit(isin.substring(0, 11));
            assertThat(isin.charAt(11), equalTo(checkDigit));
        }
    }

    @Test
    public void testValidIsinsAreVerified() {
        for (String isin : VALID_ISINS) {
            assertThat(FinancialCodeUtils.isValidIsin(isin), is(true));
        }
    }

    @Test
    public void testIncorrectCheckDigitIsinsAreVerifiedInvalid() {
        for (String isin : INCORRECT_CHECK_DIGIT_ISINS) {
            assertThat(FinancialCodeUtils.isValidIsin(isin), is(false));
        }
    }

    @Test
    public void testIncorrectSedolCheckDigitIsinsAreVerifiedInvalid() {
        for (String isin : INCORRECT_SEDOL_ISINS) {
            assertThat(FinancialCodeUtils.isValidIsin(isin), is(false));
        }
    }

    @Test
    public void testCusipCheckDigitsCorrectlyCalculated() {
        for (String isin : INCORRECT_CUSIP_ISINS) {
            assertThat(FinancialCodeUtils.isValidIsin(isin), is(false));
        }
    }
}

package com.scottlogic.deg.generator.utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IsinTest {

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
            final char checkDigit = Isin.calculateIsinCheckDigit(isin.substring(0, 11));
            assertThat(isin.charAt(11), equalTo(checkDigit));
        }
    }

    @Test
    public void testValidIsinsAreVerified() {
        for (String isin : VALID_ISINS) {
            assertThat(Isin.isValidIsin(isin), is(true));
        }
    }

    @Test
    public void testIncorrectCheckDigitIsinsAreVerifiedInvalid() {
        for (String isin : INCORRECT_CHECK_DIGIT_ISINS) {
            assertThat(Isin.isValidIsin(isin), is(false));
        }
    }

    @Test
    public void testIncorrectSedolCheckDigitIsinsAreVerifiedInvalid() {
        for (String isin : INCORRECT_SEDOL_ISINS) {
            assertThat(Isin.isValidIsin(isin), is(false));
        }
    }

    @Test
    public void testCusipCheckDigitsCorrectlyCalculated() {
        for (String isin : INCORRECT_CUSIP_ISINS) {
            assertThat(Isin.isValidIsin(isin), is(false));
        }
    }
}

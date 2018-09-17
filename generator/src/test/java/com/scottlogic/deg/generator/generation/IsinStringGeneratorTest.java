package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.IsinUtils;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IsinStringGeneratorTest {

    @Before
    public void setup() {
    }

    @Test
    public void shouldEndAllIsinsWithValidCheckDigit() {
        IsinStringGenerator target = new IsinStringGenerator();
        final int NumberOfTests = 100;

        final Iterator<String> allIsins = target.generateAllValues().iterator();

        for (int ii = 0; ii < NumberOfTests; ++ii) {
            final String nextIsin = allIsins.next();
            final char checkDigit = IsinUtils.calculateIsinCheckDigit(nextIsin.substring(0, 11));
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
            final char checkDigit = IsinUtils.calculateIsinCheckDigit(nextIsin.substring(0, 11));
            assertThat(nextIsin.charAt(11), equalTo(checkDigit));
        }
    }

    @Test
    public void shouldOnlyUseSpecifiedCountries() {
        final String testCountry = "GB";
        IsinStringGenerator target = new IsinStringGenerator(Collections.singletonList(testCountry));
        final int NumberOfTests = 100;

        final Iterator<String> allIsins = target.generateRandomValues(new JavaUtilRandomNumberGenerator()).iterator();

        for (int ii = 0; ii < NumberOfTests; ++ii) {
            final String nextIsin = allIsins.next();
            assertThat(nextIsin.substring(0, 2), equalTo(testCountry));
        }
    }

    @Test
    public void shouldUseSedolWhenCountryIsGB() {
        final String testCountry = "GB";
        IsinStringGenerator target = new IsinStringGenerator(Collections.singletonList(testCountry));
        final int NumberOfTests = 100;

        final Iterator<String> allIsins = target.generateAllValues().iterator();

        for (int ii = 0; ii < NumberOfTests; ++ii) {
            final String nextIsin = allIsins.next();
            assertThat(IsinUtils.isValidSedolNsin(nextIsin.substring(2, 11)), is(true));
        }
    }

    @Test
    public void complementShouldNotSupportGeneratingAllStrings() {
        final UnsupportedOperationException unsupportedOperationException = assertThrows(UnsupportedOperationException.class, () -> {
            new IsinStringGenerator().complement().generateAllValues();
        });
        assertThat(unsupportedOperationException.getMessage(), is("Can't generate all strings for a non-finite regex"));
    }

    @Test
    public void complementShouldProduceNoRandomValidIsins() {
        IStringGenerator target = new IsinStringGenerator().complement();

        final int NumberOfTests = 100;

        final Iterator<String> allIsins = target.generateRandomValues(new JavaUtilRandomNumberGenerator()).iterator();

        for (int ii = 0; ii < NumberOfTests; ++ii) {
            final String nextIsin = allIsins.next();
            assertThat(IsinUtils.isValidIsin(nextIsin), is(false));
        }
    }

}

package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.IsinUtils;
import com.scottlogic.deg.generator.utils.IterableAsStream;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IsinStringGeneratorTest {

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
    public void shouldUseSedolWhenCountryIsGB() {
        // this assumes that the first batch of values produced by the generator are GB-flavoured. If this changes in the future, this test might need to get more complicated

        AtomicInteger numberOfIsinsTested = new AtomicInteger(0);
        IterableAsStream.convert(new IsinStringGenerator().generateAllValues())
            .limit(100)
            .forEach(isinString -> {
                if (!isinString.substring(0, 2).equals("GB"))
                    throw new IllegalStateException("Test assumes that the first 100 ISINs will be GB-flavoured");

                assertThat(
                    IsinUtils.isValidSedolNsin(isinString.substring(2, 11)), is(true));

                numberOfIsinsTested.incrementAndGet();
            });

        // make sure we tested the number we expected
        assertThat(numberOfIsinsTested.get(), equalTo(100));
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
        StringGenerator target = new IsinStringGenerator().complement();

        final int NumberOfTests = 100;

        final Iterator<String> allIsins = target.generateRandomValues(new JavaUtilRandomNumberGenerator()).iterator();

        for (int ii = 0; ii < NumberOfTests; ++ii) {
            final String nextIsin = allIsins.next();
            assertThat(IsinUtils.isValidIsin(nextIsin), is(false));
        }
    }
}

package com.scottlogic.deg.generator.generation.field_value_sources;

import com.scottlogic.deg.generator.restrictions.NumericLimit;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import static org.hamcrest.CoreMatchers.equalTo;

class RealNumberFieldValueSourceTests {
    @ParameterizedTest
    @CsvSource({
        "-1,  1,    1, -1;-0.9;-0.8;-0.7;-0.6;-0.5;-0.4;-0.3;-0.2;-0.1;0.0;0.1;0.2;0.3;0.4;0.5;0.6;0.7;0.8;0.9;1",
        "0.65,1.1,  1, 0.7;0.8;0.9;1;1.1",
        "5,   35,  -1, 10;20;30",
        "10,  35,  -1, 10;20;30",
        "10,  40,  -1, 10;20;30;40",
        "5,   40,  -1, 10;20;30;40",
        "11,  40,  -1, 20;30;40",
        "10,  41,  -1, 10;20;30;40",
        "9,   41,  -1, 10;20;30;40",
        "11,  41,  -1, 20;30;40",
        "11,  49,  -1, 20;30;40",
        "-4,  5,    0, -4;-3;-2;-1;0;1;2;3;4;5",
        "0.9, 2.1,  0, 1;2",
        "0.1, 5.9,  0, 1;2;3;4;5",
    })
    void expectValuesInclusiveOfBounds(BigDecimal lowerBound, BigDecimal upperBound, int scale, String expectedResults) {
        givenLowerBound(lowerBound, true);
        givenUpperBound(upperBound, true);
        givenScale(scale);

        expectAllValues(expectedResults.split(";"));
    }

    @ParameterizedTest
    @CsvSource({
        "-1,   1,    1, -0.9;-0.8;-0.7;-0.6;-0.5;-0.4;-0.3;-0.2;-0.1;0.0;0.1;0.2;0.3;0.4;0.5;0.6;0.7;0.8;0.9",
        "0.65, 1.1,  1, 0.7;0.8;0.9;1",
        "5,    35,  -1, 10;20;30",
        "10,   35,  -1, 20;30",
        "10,   40,  -1, 20;30",
        "5,    40,  -1, 10;20;30",
        "11,   40,  -1, 20;30",
        "10,   41,  -1, 20;30;40",
        "9,    41,  -1, 10;20;30;40",
        "11,   41,  -1, 20;30;40",
        "11,   49,  -1, 20;30;40",
        "-4,   5,    0, -3;-2;-1;0;1;2;3;4",
        "0.9,  2.1,  0, 1;2",
        "0.1,  5.9,  0, 1;2;3;4;5",
    })
    void expectValuesExclusiveOfBounds(BigDecimal lowerBound, BigDecimal upperBound, int scale, String expectedResults) {
        givenLowerBound(lowerBound, false);
        givenUpperBound(upperBound, false);
        givenScale(scale);

        expectAllValues(expectedResults.split(";"));
    }

    @Test
    void whenBlacklistHasNoValuesInRange() {
        givenLowerBound("3", true);
        givenUpperBound("5", true);

        givenBlacklist(1);

        expectAllValues("3", "4", "5");
    }

    @Test
    void whenBlacklistContainsNonIntegralValues() {
        givenLowerBound("3", true);
        givenUpperBound("6", true);

        givenBlacklist("hello", 4, new BigDecimal(5));

        expectAllValues("3", "6");
    }

    @Test
    void whenBlacklistContainsAllValuesInRange() {
        givenLowerBound("3", true);
        givenUpperBound("5", true);

        givenBlacklist(3, 4, 5);

        expectNoValues();
    }

    @Test
    void whenBlacklistRounded() {
        givenLowerBound("0", true);
        givenUpperBound("100", true);
        givenScale(-1);

        givenBlacklist(8, 31, 56, 64);
        // should filter out 10, 30, 60 (twice)

        expectAllValues("0", "20", "40", "50", "70", "80", "90", "100");
    }

    @Test
    void whenSmall() {
        givenLowerBound("-0.05", false);
        givenUpperBound("0.05", false);
        givenScale(2);

        givenBlacklist("-0.03", "-0", "0.021", 4);

        expectAllValues("-0.04", "-0.02", "-0.01", "0.01", "0.03", "0.04");
    }

//    @Test
//    void whenUpperBoundNotSpecified() {
//        givenLowerBound(0, true);
//
//        expectFinite();
//        expectValueCount(Integer.MAX_VALUE);
//    }
//
//    @Test
//    void whenBlacklistContainsAllValuesInRange() {
//        givenLowerBound(3, true);
//        givenUpperBound(5, true);
//
//        givenBlacklist(3, 4, 5);
//
//        expectNoValues();
//    }
//
//    @Test
//    void shouldSupplyInterestingValues() {
//        givenLowerBound(-100, true);
//        givenUpperBound(100, true);
//
//        expectInterestingValues(-100, 0, 100);
//    }
//
//    @Test
//    void shouldSupplyExclusiveInterestingValues() {
//        givenLowerBound(0, false);
//        givenUpperBound(100, false);
//
//        expectInterestingValues(1, 99);
//    }
//
//    @Test
//    void shouldAvoidBlacklistedInterestingValues() {
//        givenLowerBound(-100, false);
//        givenUpperBound(100, true);
//
//        givenBlacklist(-100, 0, 100);
//
//        expectInterestingValues(-99, 99);
//    }
//
//    @Test
//    void shouldSupplyToUpperBoundary() {
//        givenLowerBound(4, true);
//
//        expectInterestingValues(4, Integer.MAX_VALUE - 1);
//    }
//
//    @Test
//    void shouldSupplyToLowerBoundary() {
//        givenUpperBound(4, true);
//
//        expectInterestingValues(Integer.MIN_VALUE, 0, 4);
//    }
//
//    @Test
//    void shouldSupplyToBoundary() {
//        expectInterestingValues(Integer.MIN_VALUE, 0, Integer.MAX_VALUE - 1);
//    }

    private NumericLimit<BigDecimal> upperLimit;
    private NumericLimit<BigDecimal> lowerLimit;
    private int scale;
    private Set<Object> blacklist;
    private RealNumberFieldValueSource objectUnderTest;

    private void givenLowerBound(String limit, boolean isInclusive) {
        givenLowerBound(new BigDecimal(limit), isInclusive);
    }
    private void givenLowerBound(BigDecimal limit, boolean isInclusive) {
        this.lowerLimit = new NumericLimit<>(limit, isInclusive);
    }

    private void givenUpperBound(String limit, boolean isInclusive) {
        givenUpperBound(new BigDecimal(limit), isInclusive);
    }
    private void givenUpperBound(BigDecimal limit, boolean isInclusive) {
        this.upperLimit = new NumericLimit<>(limit, isInclusive);
    }

    private void givenScale(int scale) {
        this.scale = scale;
    }

    private void givenBlacklist(Object... values) {
        blacklist = new HashSet<>(Arrays.asList(values));
    }
    private void expectAllValues(String... expectedValuesArray) {
        expectValues(getObjectUnderTest().generateAllValues(), true, expectedValuesArray);
    }

    private void expectInterestingValues(String... expectedValuesArray) {
        expectValues(getObjectUnderTest().generateInterestingValues(), false, expectedValuesArray);
    }

    private void expectValues(Iterable<Object> values, boolean assertCount, String... expectedValuesArray) {
        Collection<Matcher<? super BigDecimal>> expectedValuesMatchers = Stream.of(expectedValuesArray)
            .map(BigDecimal::new)
            .map(Matchers::comparesEqualTo) // we have to use compare otherwise it fails if the scale is different
            .collect(Collectors.toList());

        BigDecimal[] actualValues = StreamSupport
            .stream(values.spliterator(), false)
            .toArray(BigDecimal[]::new);

        // ASSERT
        if (assertCount) {
            expectValueCount(expectedValuesArray.length);
            expectFinite();
        }

        Assert.assertThat(actualValues, arrayContainingInAnyOrder(expectedValuesMatchers));
    }

    private void expectNoValues() {
        expectAllValues();
    }

    private IFieldValueSource getObjectUnderTest() {
        if (objectUnderTest == null) {
            objectUnderTest = new RealNumberFieldValueSource(upperLimit, lowerLimit, blacklist, scale);
        }

        return objectUnderTest;
    }

    private void expectFinite() {
        Assert.assertTrue(getObjectUnderTest().isFinite());
    }

    private void expectValueCount(int expectedCount) {
        long cound = getObjectUnderTest().getValueCount();

        Assert.assertThat(
            getObjectUnderTest().getValueCount(),
            equalTo((long)expectedCount));
    }

    @BeforeEach
    void beforeEach() {
        this.upperLimit = null;
        this.lowerLimit = null;
        this.scale = 0;
        this.blacklist = Collections.emptySet();
        this.objectUnderTest = null;
    }
}


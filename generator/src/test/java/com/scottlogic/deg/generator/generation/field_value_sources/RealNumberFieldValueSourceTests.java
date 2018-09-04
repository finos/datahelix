package com.scottlogic.deg.generator.generation.field_value_sources;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import java.math.BigDecimal;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.hamcrest.CoreMatchers.equalTo;

class RealNumberFieldValueSourceTests {
    @Test
    void withSameInclusiveLowerAndUpperBounds() {
        givenLowerBound("-1");
        givenUpperBound("1");
        givenScale(1);

        expectAllValues("-1.0", "-0.9", "-0.8", "-0.7", "-0.6", "-0.5", "-0.4", "-0.3", "-0.2", "-0.1",
            "0.0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0");
    }

//    @Test
//    void whenBlacklistHasNoValuesInRange() {
//        givenLowerBound(3);
//        givenUpperBound(5, true);
//
//        expectAllValues(3, 4, 5);
//    }
//
//    @Test
//    void exclusiveRangesShouldExcludeTheirOwnValue() {
//        givenLowerBound(3, false);
//        givenUpperBound(5, false);
//
//        expectAllValues(4);
//    }
//
//    @Test
//    void whenBlacklistContainsNonIntegralValues() {
//        givenLowerBound(3, true);
//        givenUpperBound(5, true);
//
//        givenBlacklist("hello", 4, new BigDecimal(5));
//
//        expectAllValues(3);
//    }
//
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

    private BigDecimal upperLimit;
    private BigDecimal lowerLimit;
    private int scale;
    private RealNumberFieldValueSource objectUnderTest;

    private void givenLowerBound(String lowerLimit) {
        this.lowerLimit = new BigDecimal(lowerLimit);
    }

    private void givenUpperBound(String upperLimit) {
        this.upperLimit = new BigDecimal(upperLimit);
    }

    private void givenScale(int scale) {
        this.scale = scale;
    }

    private void expectAllValues(String... expectedValuesArray) {
        expectValues(getObjectUnderTest().generateAllValues(), true, expectedValuesArray);
    }

    private void expectInterestingValues(String... expectedValuesArray) {
        expectValues(getObjectUnderTest().generateInterestingValues(), false, expectedValuesArray);
    }

    private void expectValues(Iterable<Object> values, boolean assertCount, String... expectedValuesArray) {
        BigDecimal[] expectedValues = Stream.of(expectedValuesArray)
            .map(BigDecimal::new)
            .toArray(BigDecimal[]::new);

        BigDecimal[] actualValues = StreamSupport
            .stream(values.spliterator(), false)
            .toArray(BigDecimal[]::new);

        // ASSERT
        if (assertCount) {
            expectValueCount(expectedValuesArray.length);
            expectFinite();
        }

        Assert.assertThat(actualValues, arrayContainingInAnyOrder(expectedValues));
    }

    private void expectNoValues() {
        expectAllValues();
    }

    private IFieldValueSource getObjectUnderTest() {
        if (objectUnderTest == null) {
            objectUnderTest = new RealNumberFieldValueSource(upperLimit, lowerLimit, scale);
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
        this.objectUnderTest = null;
    }
}


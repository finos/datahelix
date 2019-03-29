package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import com.scottlogic.deg.generator.restrictions.DateTimeRestrictions;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class DateTimeFieldValueSourceTests {

    private DateTimeRestrictions.DateTimeLimit lowerLimit = null;
    private DateTimeRestrictions.DateTimeLimit upperLimit = null;
    private Set<Object> blackList = new HashSet<>();
    private DateTimeFieldValueSource fieldSource;

    @Test
    public void whenGeneratingUnboundSet() {
        expectInterestingValues(
                createDate(1900, 1, 1),
                createDate(2100, 1, 1));
    }

    @Test
    public void whenGeneratingUnboundSetWithBlacklist() {
        givenBlacklist(createDate(2100, 1, 1));
        expectInterestingValues(
                createDate(1900, 1, 1));
    }

    @Test
    public void whenGivenUpperBound() {
        givenUpperBound(createDate(2018, 01, 01), true);
        expectInterestingValues(
                createDate(1900, 1, 1),
                createDate(2018, 1, 1));
    }

    @Test
    public void whenGivenLowerBound() {
        givenLowerBound(createDate(2018, 01, 01), true);
        expectInterestingValues(
                createDate(2018, 1, 1),
                createDate(2100, 1, 1));
    }

    @Test
    public void whenGivenMultiHourRange() {
        LocalDate date = LocalDate.of(2018, 1, 10);
        givenLowerBound(OffsetDateTime.of(date.atTime(LocalTime.of(12, 0, 0, 0)), ZoneOffset.UTC), true);
        givenUpperBound(OffsetDateTime.of(date.atTime(LocalTime.of(12, 0, 0, 3000000)), ZoneOffset.UTC), false);
        expectAllValues(
            OffsetDateTime.of(date.atTime(LocalTime.of(12, 0, 0, 0)), ZoneOffset.UTC),
            OffsetDateTime.of(date.atTime(LocalTime.of(12, 0, 0, 1000000)), ZoneOffset.UTC),
            OffsetDateTime.of(date.atTime(LocalTime.of(12, 0, 0, 2000000)), ZoneOffset.UTC));
    }


    @Test
    public void getRandomValues_withExclusiveUpperBound_shouldGenerateCorrectValues() {
        LocalDate date = LocalDate.of(2018, 1, 10);
        givenLowerBound(OffsetDateTime.of(date.atTime(LocalTime.of(12, 0, 0)), ZoneOffset.UTC), true);
        givenUpperBound(OffsetDateTime.of(date.atTime(LocalTime.of(18, 0, 0)), ZoneOffset.UTC), false);

        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = lowerLimit;
        restrictions.max = upperLimit;

        fieldSource = new DateTimeFieldValueSource(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0);

        Iterator<Object> iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(date.atTime(LocalTime.of(12, 0, 0)), ZoneOffset.UTC)));

        rng.setNextDouble(1);

        // Because internally the filteringIterator pre-generates the first value before we can set
        // the new "random" value we have re-create the iterator
        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(date.atTime(LocalTime.of(17, 59, 59, 999_000_000)), ZoneOffset.UTC)));

        rng.setNextDouble(0.5);

        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(date.atTime(LocalTime.of(14, 59, 59, 999_000_000)), ZoneOffset.UTC)));

    }

    @Test
    public void getRandomValues_withInclusiveUpperBound_shouldGenerateCorrectValues() {
        LocalDate date = LocalDate.of(2018, 1, 10);
        givenLowerBound(OffsetDateTime.of(date.atTime(LocalTime.of(12, 0, 0)), ZoneOffset.UTC), true);
        givenUpperBound(OffsetDateTime.of(date.atTime(LocalTime.of(18, 0, 0)), ZoneOffset.UTC), true);

        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = lowerLimit;
        restrictions.max = upperLimit;

        fieldSource = new DateTimeFieldValueSource(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0);

        Iterator<Object> iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(date.atTime(LocalTime.of(12, 0, 0)), ZoneOffset.UTC)));

        rng.setNextDouble(1);

        // Because internally the filteringIterator pre-generates the first value before we can set
        // the new "random" value we have re-create the iterator
        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(date.atTime(LocalTime.of(18, 0, 0)), ZoneOffset.UTC)));

        rng.setNextDouble(0.5);

        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(date.atTime(LocalTime.of(15, 0, 0)), ZoneOffset.UTC)));
    }

    @Test
    public void getRandomValues_withExclusiveLowerBound_shouldGenerateCorrectValues() {
        LocalDate date = LocalDate.of(2018, 1, 10);
        givenLowerBound(OffsetDateTime.of(date.atTime(LocalTime.of(12, 0, 0)), ZoneOffset.UTC), false);
        givenUpperBound(OffsetDateTime.of(date.atTime(LocalTime.of(18, 0, 0)), ZoneOffset.UTC), true);

        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = lowerLimit;
        restrictions.max = upperLimit;

        fieldSource = new DateTimeFieldValueSource(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0);

        Iterator<Object> iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(date.atTime(LocalTime.of(12, 0, 0, 1_000_000)), ZoneOffset.UTC)));

        rng.setNextDouble(1);

        // Because internally the filteringIterator pre-generates the first value before we can set
        // the new "random" value we have re-create the iterator
        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(date.atTime(LocalTime.of(18, 0, 0)), ZoneOffset.UTC)));

        rng.setNextDouble(0.5);

        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(date.atTime(LocalTime.of(15, 0, 0)), ZoneOffset.UTC)));
    }

    @Test
    public void getRandomValues_withSmallPermittedRangeAtEndOfLegalRange_shouldGenerateCorrectValues() {
        givenLowerBound(OffsetDateTime.of(9999, 12, 31, 23, 0, 0,0, ZoneOffset.UTC), false);

        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = lowerLimit;
        restrictions.max = upperLimit;

        fieldSource = new DateTimeFieldValueSource(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0.99);

        Iterator<Object> iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(9999, 12, 31, 23, 59, 23, 999_000_000, ZoneOffset.UTC)));
    }

    @Test
    public void getRandomValues_withNoExplicitBounds_shouldGenerateCorrectValues() {
        DateTimeRestrictions restrictions = new DateTimeRestrictions();

        fieldSource = new DateTimeFieldValueSource(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0);

        Iterator<Object> iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(DateTimeFieldValueSource.ISO_MIN_DATE));

        rng.setNextDouble(1);

        // Because internally the filteringIterator pre-generates the first value before we can set
        // the new "random" value we have re-create the iterator
        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(9999, 12, 31, 23, 59, 59, 999_000_000, ZoneOffset.UTC)));

        rng.setNextDouble(0.5);

        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(5000, 07, 02, 11, 59, 59, 999_000_000, ZoneOffset.UTC)));
    }

    @Test
    public void shouldBeEqualWhenAllPropertiesMatch(){
        DateTimeFieldValueSource a = new DateTimeFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        DateTimeFieldValueSource b = new DateTimeFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeEqualWhenAllPropertiesMatchWhenBlacklistInDifferentOrder(){
        DateTimeFieldValueSource a = new DateTimeFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        DateTimeFieldValueSource b = new DateTimeFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MAX, OffsetDateTime.MIN)));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeEqualWhenAllPropertiesMatchWhenBlacklistEmpty(){
        DateTimeFieldValueSource a = new DateTimeFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            Collections.emptySet());
        DateTimeFieldValueSource b = new DateTimeFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            Collections.emptySet());

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeUnequalWhenAllPropertiesMatchExceptMin(){
        DateTimeFieldValueSource a = new DateTimeFieldValueSource(
            restrictions("2001-02-28", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        DateTimeFieldValueSource b = new DateTimeFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalWhenAllPropertiesMatchExceptMax(){
        DateTimeFieldValueSource a = new DateTimeFieldValueSource(
            restrictions("2001-02-03", "2010-11-30"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        DateTimeFieldValueSource b = new DateTimeFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalWhenAllPropertiesMatchExceptBlacklist(){
        DateTimeFieldValueSource a = new DateTimeFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        DateTimeFieldValueSource b = new DateTimeFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Collections.singletonList(OffsetDateTime.MIN)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalWhenOnlyBlacklistMatches(){
        DateTimeFieldValueSource a = new DateTimeFieldValueSource(
            restrictions("2001-02-28", "2010-11-30"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        DateTimeFieldValueSource b = new DateTimeFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalWhenAllPropertiesDontMatch(){
        DateTimeFieldValueSource a = new DateTimeFieldValueSource(
            restrictions("2001-02-28", "2010-11-30"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        DateTimeFieldValueSource b = new DateTimeFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Collections.singletonList(OffsetDateTime.MIN)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void datetimeGenerateAllValues_withNoMin_startsAtOffsetDateTimeMin(){
        //Arrange
        DateTimeRestrictions max = new DateTimeRestrictions();
        max.max = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MAX, false);
        DateTimeFieldValueSource noMin = new DateTimeFieldValueSource(max, Collections.emptySet());
        //Act
        OffsetDateTime firstValue = (OffsetDateTime) noMin.generateAllValues().iterator().next();
        //Assert
        Assert.assertThat(firstValue, equalTo(DateTimeFieldValueSource.ISO_MIN_DATE));
    }

    @Test
    public void datetimeGenerateAllValues_withMinSetToMaxDate_emitsNoValues(){
        //Arrange
        DateTimeRestrictions min = new DateTimeRestrictions();
        min.min = new DateTimeRestrictions.DateTimeLimit(DateTimeFieldValueSource.ISO_MAX_DATE, false);
        DateTimeFieldValueSource datesAfterLastPermittedDate = new DateTimeFieldValueSource(min, Collections.emptySet());

        //Act
        Iterator iterator = datesAfterLastPermittedDate.generateAllValues().iterator();
        //Assert
        Assert.assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void datetimeGenerateAllValues_withMaxSetToMinDate_emitsNoValues(){
        //Arrange
        DateTimeRestrictions max = new DateTimeRestrictions();
        max.max = new DateTimeRestrictions.DateTimeLimit(DateTimeFieldValueSource.ISO_MIN_DATE, false);
        DateTimeFieldValueSource datesBeforeFirstPermittedDate = new DateTimeFieldValueSource(max, Collections.emptySet());

        //Act
        Iterator iterator = datesBeforeFirstPermittedDate.generateAllValues().iterator();
        //Assert
        Assert.assertThat(iterator.hasNext(), is(false));
    }

    private DateTimeRestrictions restrictions(String min, String max){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = min == null ? null : getTimeLimit(min);
        restrictions.max = max == null ? null : getTimeLimit(max);
        return restrictions;
    }

    private DateTimeRestrictions.DateTimeLimit getTimeLimit(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return new DateTimeRestrictions.DateTimeLimit(
            LocalDate.parse(dateString, formatter).atStartOfDay().atOffset(ZoneOffset.UTC),
            true);
    }

    private void givenLowerBound(OffsetDateTime value, boolean inclusive) {
        lowerLimit = new DateTimeRestrictions.DateTimeLimit(value, inclusive);
    }

    private void givenUpperBound(OffsetDateTime value, boolean inclusive) {
        upperLimit = new DateTimeRestrictions.DateTimeLimit(value, inclusive);
    }

    private void givenBlacklist(Object... list) {
        blackList = new HashSet<>(Arrays.asList(list));
    }

    private void expectAllValues(Object... expectedValuesArray) {
        List<Object> expectedValues = Arrays.asList(expectedValuesArray);
        List<Object> actualValues = new ArrayList<>();

        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = lowerLimit;
        restrictions.max = upperLimit;

        fieldSource = new DateTimeFieldValueSource(restrictions, blackList);

        fieldSource.generateAllValues().forEach(actualValues::add);

        Assert.assertThat(actualValues, equalTo(expectedValues));
    }

    private void expectInterestingValues(Object... expectedValuesArray) {
        List<Object> expectedValues = Arrays.asList(expectedValuesArray);
        List<Object> actualValues = new ArrayList<>();

        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = lowerLimit;
        restrictions.max = upperLimit;

        fieldSource = new DateTimeFieldValueSource(restrictions, blackList);

        fieldSource.generateInterestingValues().forEach(actualValues::add);

        Assert.assertThat(actualValues, equalTo(expectedValues));
    }

    private OffsetDateTime createDate(int year, int month, int day) {
        return OffsetDateTime.of(year, month, day, 0, 0, 0, 0, ZoneOffset.UTC);
    }

    private class TestRandomNumberGenerator implements RandomNumberGenerator {

        private double nextDoubleValue = 0;

        public void setNextDouble(double value) {
            nextDoubleValue = value;
        }

        @Override
        public int nextInt() {
            return 0;
        }

        @Override
        public int nextInt(int bound) {
            return 0;
        }

        @Override
        public int nextInt(int lowerInclusive, int upperExclusive) {
            return 0;
        }

        @Override
        public double nextDouble(double lower, double upper) {
            return nextDoubleValue * (upper - lower) + lower;
        }
    }
}

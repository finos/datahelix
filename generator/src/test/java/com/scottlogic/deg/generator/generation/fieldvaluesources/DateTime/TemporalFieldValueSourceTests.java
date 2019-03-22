package com.scottlogic.deg.generator.generation.fieldvaluesources.DateTime;

import com.scottlogic.deg.generator.restrictions.DateTimeRestrictions;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class TemporalFieldValueSourceTests {

    private DateTimeRestrictions.DateTimeLimit lowerLimit = null;
    private DateTimeRestrictions.DateTimeLimit upperLimit = null;
    private Set<Object> blackList = new HashSet<>();
    private TemporalFieldValueSource fieldSource;

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
        givenLowerBound(LocalDateTime.of(date, LocalTime.of(12, 0, 0, 0)), true);
        givenUpperBound(LocalDateTime.of(date, LocalTime.of(12, 0, 0, 3000000)), false);
        expectAllValues(
            LocalDateTime.of(date, LocalTime.of(12, 0, 0, 0)),
            LocalDateTime.of(date, LocalTime.of(12, 0, 0, 1000000)),
            LocalDateTime.of(date, LocalTime.of(12, 0, 0, 2000000)));
    }


    @Test
    public void getRandomValues_withExclusiveUpperBound_shouldGenerateCorrectValues() {
        LocalDate date = LocalDate.of(2018, 1, 10);
        givenLowerBound(LocalDateTime.of(date, LocalTime.of(12, 0, 0)), true);
        givenUpperBound(LocalDateTime.of(date, LocalTime.of(18, 0, 0)), false);

        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = lowerLimit;
        restrictions.max = upperLimit;

        fieldSource = new TemporalFieldValueSource(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0);

        Iterator<Object> iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(LocalDateTime.of(date, LocalTime.of(12, 0, 0))));

        rng.setNextDouble(1);

        // Because internally the filteringIterator pre-generates the first value before we can set
        // the new "random" value we have re-create the iterator
        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(LocalDateTime.of(date, LocalTime.of(17, 59, 59, 999_000_000))));

        rng.setNextDouble(0.5);

        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(LocalDateTime.of(date, LocalTime.of(14, 59, 59, 999_000_000))));

    }

    @Test
    public void getRandomValues_withInclusiveUpperBound_shouldGenerateCorrectValues() {
        LocalDate date = LocalDate.of(2018, 1, 10);
        givenLowerBound(LocalDateTime.of(date, LocalTime.of(12, 0, 0)), true);
        givenUpperBound(LocalDateTime.of(date, LocalTime.of(18, 0, 0)), true);

        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = lowerLimit;
        restrictions.max = upperLimit;

        fieldSource = new TemporalFieldValueSource(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0);

        Iterator<Object> iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(LocalDateTime.of(date, LocalTime.of(12, 0, 0))));

        rng.setNextDouble(1);

        // Because internally the filteringIterator pre-generates the first value before we can set
        // the new "random" value we have re-create the iterator
        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(LocalDateTime.of(date, LocalTime.of(18, 0, 0))));

        rng.setNextDouble(0.5);

        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(LocalDateTime.of(date, LocalTime.of(15, 0, 0))));
    }

    @Test
    public void getRandomValues_withExclusiveLowerBound_shouldGenerateCorrectValues() {
        LocalDate date = LocalDate.of(2018, 1, 10);
        givenLowerBound(LocalDateTime.of(date, LocalTime.of(12, 0, 0)), false);
        givenUpperBound(LocalDateTime.of(date, LocalTime.of(18, 0, 0)), true);

        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = lowerLimit;
        restrictions.max = upperLimit;

        fieldSource = new TemporalFieldValueSource(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0);

        Iterator<Object> iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(LocalDateTime.of(date, LocalTime.of(12, 0, 0, 1_000_000))));

        rng.setNextDouble(1);

        // Because internally the filteringIterator pre-generates the first value before we can set
        // the new "random" value we have re-create the iterator
        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(LocalDateTime.of(date, LocalTime.of(18, 0, 0))));

        rng.setNextDouble(0.5);

        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(LocalDateTime.of(date, LocalTime.of(15, 0, 0))));
    }

    @Test
    public void getRandomValues_withSmallPermittedRangeAtEndOfLegalRange_shouldGenerateCorrectValues() {
        LocalDate date = LocalDate.of(9999, 12, 31);
        givenLowerBound(LocalDateTime.of(date, LocalTime.of(23, 0, 0)), false);

        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = lowerLimit;
        restrictions.max = upperLimit;

        fieldSource = new TemporalFieldValueSource(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0.99);

        Iterator<Object> iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(LocalDateTime.of(date, LocalTime.of(23, 59, 23, 999_000_000))));
    }

    @Test
    public void getRandomValues_withNoExplicitBounds_shouldGenerateCorrectValues() {
        DateTimeRestrictions restrictions = new DateTimeRestrictions();

        fieldSource = new TemporalFieldValueSource(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0);

        Iterator<Object> iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(TemporalFieldValueSource.ISO_MIN_DATE));

        rng.setNextDouble(1);

        // Because internally the filteringIterator pre-generates the first value before we can set
        // the new "random" value we have re-create the iterator
        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(LocalDateTime.of(9999, 12, 31, 23, 59, 59, 999_000_000)));

        rng.setNextDouble(0.5);

        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(LocalDateTime.of(5000, 07, 02, 11, 59, 59, 999_000_000)));
    }

    @Test
    public void shouldBeEqualWhenAllPropertiesMatch(){
        TemporalFieldValueSource a = new TemporalFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(LocalDateTime.MIN, LocalDateTime.MAX)));
        TemporalFieldValueSource b = new TemporalFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(LocalDateTime.MIN, LocalDateTime.MAX)));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeEqualWhenAllPropertiesMatchWhenBlacklistInDifferentOrder(){
        TemporalFieldValueSource a = new TemporalFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(LocalDateTime.MIN, LocalDateTime.MAX)));
        TemporalFieldValueSource b = new TemporalFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(LocalDateTime.MAX, LocalDateTime.MIN)));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeEqualWhenAllPropertiesMatchWhenBlacklistEmpty(){
        TemporalFieldValueSource a = new TemporalFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            Collections.emptySet());
        TemporalFieldValueSource b = new TemporalFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            Collections.emptySet());

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeUnequalWhenAllPropertiesMatchExceptMin(){
        TemporalFieldValueSource a = new TemporalFieldValueSource(
            restrictions("2001-02-28", "2010-11-12"),
            new HashSet<>(Arrays.asList(LocalDateTime.MIN, LocalDateTime.MAX)));
        TemporalFieldValueSource b = new TemporalFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(LocalDateTime.MIN, LocalDateTime.MAX)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalWhenAllPropertiesMatchExceptMax(){
        TemporalFieldValueSource a = new TemporalFieldValueSource(
            restrictions("2001-02-03", "2010-11-30"),
            new HashSet<>(Arrays.asList(LocalDateTime.MIN, LocalDateTime.MAX)));
        TemporalFieldValueSource b = new TemporalFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(LocalDateTime.MIN, LocalDateTime.MAX)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalWhenAllPropertiesMatchExceptBlacklist(){
        TemporalFieldValueSource a = new TemporalFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(LocalDateTime.MIN, LocalDateTime.MAX)));
        TemporalFieldValueSource b = new TemporalFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Collections.singletonList(LocalDateTime.MIN)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalWhenOnlyBlacklistMatches(){
        TemporalFieldValueSource a = new TemporalFieldValueSource(
            restrictions("2001-02-28", "2010-11-30"),
            new HashSet<>(Arrays.asList(LocalDateTime.MIN, LocalDateTime.MAX)));
        TemporalFieldValueSource b = new TemporalFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(LocalDateTime.MIN, LocalDateTime.MAX)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalWhenAllPropertiesDontMatch(){
        TemporalFieldValueSource a = new TemporalFieldValueSource(
            restrictions("2001-02-28", "2010-11-30"),
            new HashSet<>(Arrays.asList(LocalDateTime.MIN, LocalDateTime.MAX)));
        TemporalFieldValueSource b = new TemporalFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Collections.singletonList(LocalDateTime.MIN)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void temporalGenerateAllValues_withNoMin_startsAtLocalDateTimeMin(){
        //Arrange
        DateTimeRestrictions max = new DateTimeRestrictions();
        max.max = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MAX, false);
        TemporalFieldValueSource noMin = new TemporalFieldValueSource(max, Collections.emptySet());
        //Act
        LocalDateTime firstValue = (LocalDateTime) noMin.generateAllValues().iterator().next();
        //Assert
        Assert.assertThat(firstValue, equalTo(TemporalFieldValueSource.ISO_MIN_DATE));
    }

    @Test
    public void temporalGenerateAllValues_withMinSetToMaxDate_emitsNoValues(){
        //Arrange
        DateTimeRestrictions min = new DateTimeRestrictions();
        min.min = new DateTimeRestrictions.DateTimeLimit(TemporalFieldValueSource.ISO_MAX_DATE, false);
        TemporalFieldValueSource datesAfterLastPermittedDate = new TemporalFieldValueSource(min, Collections.emptySet());

        //Act
        Iterator iterator = datesAfterLastPermittedDate.generateAllValues().iterator();
        //Assert
        Assert.assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void temporalGenerateAllValues_withMaxSetToMinDate_emitsNoValues(){
        //Arrange
        DateTimeRestrictions max = new DateTimeRestrictions();
        max.max = new DateTimeRestrictions.DateTimeLimit(TemporalFieldValueSource.ISO_MIN_DATE, false);
        TemporalFieldValueSource datesBeforeFirstPermittedDate = new TemporalFieldValueSource(max, Collections.emptySet());

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
            LocalDate.parse(dateString, formatter).atStartOfDay(),
            true);
    }

    private void givenLowerBound(LocalDateTime value, boolean inclusive) {
        lowerLimit = new DateTimeRestrictions.DateTimeLimit(value, inclusive);
    }

    private void givenUpperBound(LocalDateTime value, boolean inclusive) {
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

        fieldSource = new TemporalFieldValueSource(restrictions, blackList);

        fieldSource.generateAllValues().forEach(actualValues::add);

        Assert.assertThat(actualValues, equalTo(expectedValues));
    }

    private void expectInterestingValues(Object... expectedValuesArray) {
        List<Object> expectedValues = Arrays.asList(expectedValuesArray);
        List<Object> actualValues = new ArrayList<>();

        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = lowerLimit;
        restrictions.max = upperLimit;

        fieldSource = new TemporalFieldValueSource(restrictions, blackList);

        fieldSource.generateInterestingValues().forEach(actualValues::add);

        Assert.assertThat(actualValues, equalTo(expectedValues));
    }

    private LocalDateTime createDate(int year, int month, int day) {
        return LocalDateTime.of(year, month, day, 0, 0, 0);
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

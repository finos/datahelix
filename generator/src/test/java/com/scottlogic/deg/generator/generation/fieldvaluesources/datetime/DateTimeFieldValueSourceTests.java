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

package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.generator.generation.fieldvaluesources.LinearFieldValueSource;
import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.scottlogic.deg.common.util.Defaults.ISO_MAX_DATE;
import static com.scottlogic.deg.common.util.Defaults.ISO_MIN_DATE;
import static com.scottlogic.deg.generator.utils.Defaults.DATETIME_MAX_LIMIT;
import static com.scottlogic.deg.generator.utils.Defaults.DATETIME_MIN_LIMIT;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class DateTimeFieldValueSourceTests {

    private Limit<OffsetDateTime> lowerLimit = DATETIME_MIN_LIMIT;
    private Limit<OffsetDateTime> upperLimit = DATETIME_MAX_LIMIT;
    private Set<Object> blackList = new HashSet<>();
    private LinearFieldValueSource fieldSource;

    @Test
    public void whenGeneratingUnboundSet() {
        expectInterestingValues(
                ISO_MIN_DATE,
                ISO_MAX_DATE);
    }

    @Test
    public void whenGeneratingUnboundSetWithBlacklist() {
        givenBlacklist(ISO_MAX_DATE);
        expectInterestingValues(
                ISO_MIN_DATE);
    }

    @Test
    public void whenGivenUpperBound() {
        givenUpperBound(createDate(2018, 01, 01), true);
        expectInterestingValues(
                ISO_MIN_DATE,
                createDate(2018, 1, 1));
    }

    @Test
    public void whenGivenLowerBound() {
        givenLowerBound(createDate(2018, 01, 01), true);
        expectInterestingValues(
                createDate(2018, 1, 1),
                ISO_MAX_DATE);
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

        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(lowerLimit, upperLimit);
        fieldSource = new LinearFieldValueSource(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0);

        Iterator<OffsetDateTime> iterator = fieldSource.generateRandomValues(rng).iterator();

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

        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(lowerLimit, upperLimit);


        fieldSource = new LinearFieldValueSource(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0);

        Iterator<OffsetDateTime> iterator = fieldSource.generateRandomValues(rng).iterator();

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

        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(lowerLimit, upperLimit);

        fieldSource = new LinearFieldValueSource(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0);

        Iterator<OffsetDateTime> iterator = fieldSource.generateRandomValues(rng).iterator();

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

        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(lowerLimit, upperLimit);

        fieldSource = new LinearFieldValueSource(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0.99);

        Iterator<OffsetDateTime> iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(9999, 12, 31, 23, 59, 23, 999_000_000, ZoneOffset.UTC)));
    }

    @Test
    public void shouldBeEqualWhenAllPropertiesMatch(){
        LinearFieldValueSource a = new LinearFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        LinearFieldValueSource b = new LinearFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeEqualWhenAllPropertiesMatchWhenBlacklistInDifferentOrder(){
        LinearFieldValueSource a = new LinearFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        LinearFieldValueSource b = new LinearFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MAX, OffsetDateTime.MIN)));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeEqualWhenAllPropertiesMatchWhenBlacklistEmpty(){
        LinearFieldValueSource a = new LinearFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            Collections.emptySet());
        LinearFieldValueSource b = new LinearFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            Collections.emptySet());

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeUnequalWhenAllPropertiesMatchExceptMin(){
        LinearFieldValueSource a = new LinearFieldValueSource(
            restrictions("2001-02-28", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        LinearFieldValueSource b = new LinearFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalWhenAllPropertiesMatchExceptMax(){
        LinearFieldValueSource a = new LinearFieldValueSource(
            restrictions("2001-02-03", "2010-11-30"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        LinearFieldValueSource b = new LinearFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalWhenAllPropertiesMatchExceptBlacklist(){
        LinearFieldValueSource a = new LinearFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        LinearFieldValueSource b = new LinearFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Collections.singletonList(OffsetDateTime.MIN)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalWhenOnlyBlacklistMatches(){
        LinearFieldValueSource a = new LinearFieldValueSource(
            restrictions("2001-02-28", "2010-11-30"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        LinearFieldValueSource b = new LinearFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalWhenAllPropertiesDontMatch(){
        LinearFieldValueSource a = new LinearFieldValueSource(
            restrictions("2001-02-28", "2010-11-30"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        LinearFieldValueSource b = new LinearFieldValueSource(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Collections.singletonList(OffsetDateTime.MIN)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void datetimeGenerateAllValues_withMinSetToMaxDate_emitsNoValues(){
        //Arrange
        LinearRestrictions<OffsetDateTime> min = LinearRestrictionsFactory.createDateTimeRestrictions(
            new Limit<>(Defaults.ISO_MAX_DATE, false),
            DATETIME_MAX_LIMIT
        );

        LinearFieldValueSource datesAfterLastPermittedDate = new LinearFieldValueSource(min, Collections.emptySet());

        //Act
        Iterator iterator = datesAfterLastPermittedDate.generateAllValues().iterator();
        //Assert
        Assert.assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void datetimeGenerateAllValues_withMaxSetToMinDate_emitsNoValues(){
        //Arrange
        LinearRestrictions<OffsetDateTime> max = LinearRestrictionsFactory.createDateTimeRestrictions(
            DATETIME_MIN_LIMIT,
            new Limit<>(ISO_MIN_DATE, false)
        );
        LinearFieldValueSource datesBeforeFirstPermittedDate = new LinearFieldValueSource(max, Collections.emptySet());

        //Act
        Iterator iterator = datesBeforeFirstPermittedDate.generateAllValues().iterator();
        //Assert
        Assert.assertThat(iterator.hasNext(), is(false));
    }

    private LinearRestrictions<OffsetDateTime> restrictions(String min, String max){
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(
            min == null ? null : getTimeLimit(min),
            max == null ? null : getTimeLimit(max)
        );
        return restrictions;
    }

    private Limit<OffsetDateTime> getTimeLimit(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return new Limit<>(
            LocalDate.parse(dateString, formatter).atStartOfDay().atOffset(ZoneOffset.UTC),
            true);
    }

    private void givenLowerBound(OffsetDateTime value, boolean inclusive) {
        lowerLimit = new Limit<>(value, inclusive);
    }

    private void givenUpperBound(OffsetDateTime value, boolean inclusive) {
        upperLimit = new Limit<>(value, inclusive);
    }

    private void givenBlacklist(Object... list) {
        blackList = new HashSet<>(Arrays.asList(list));
    }

    private void expectAllValues(Object... expectedValuesArray) {
        List<Object> expectedValues = Arrays.asList(expectedValuesArray);
        List<Object> actualValues = new ArrayList<>();

        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(lowerLimit, upperLimit);
        fieldSource = new LinearFieldValueSource(restrictions, blackList);

        fieldSource.generateAllValues().forEach(actualValues::add);

        Assert.assertThat(actualValues, equalTo(expectedValues));
    }

    private void expectInterestingValues(Object... expectedValuesArray) {
        List<Object> expectedValues = Arrays.asList(expectedValuesArray);
        List<Object> actualValues = new ArrayList<>();

        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(lowerLimit, upperLimit);

        fieldSource = new LinearFieldValueSource(restrictions, blackList);

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

        @Override
        public BigDecimal nextBigDecimal(BigDecimal lowerInclusive, BigDecimal upperExclusive) {
            return new BigDecimal(nextDouble(lowerInclusive.doubleValue(), upperExclusive.doubleValue()));
        }
    }
}

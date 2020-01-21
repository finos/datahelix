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

package com.scottlogic.datahelix.generator.core.generation.fieldvaluesources.datetime;

import com.scottlogic.datahelix.generator.common.TestRandomNumberGenerator;
import com.scottlogic.datahelix.generator.common.util.Defaults;
import com.scottlogic.datahelix.generator.core.generation.fieldvaluesources.LinearFieldValueSource;
import com.scottlogic.datahelix.generator.core.restrictions.linear.Limit;
import com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictions;
import com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictionsFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.scottlogic.datahelix.generator.common.util.Defaults.ISO_MIN_DATE;
import static com.scottlogic.datahelix.generator.core.utils.GeneratorDefaults.DATETIME_MAX_LIMIT;
import static com.scottlogic.datahelix.generator.core.utils.GeneratorDefaults.DATETIME_MIN_LIMIT;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class DateTimeFieldValueSourceTests {
    // Approximately one, but not definitively proven to be the closest possible float to one
    // This is used due to the random range being [0,1), not [0,1]
    private static final double ALMOST_ONE = 0.9999999996504378;

    private Limit<OffsetDateTime> lowerLimit = DATETIME_MIN_LIMIT;
    private Limit<OffsetDateTime> upperLimit = DATETIME_MAX_LIMIT;
    private Set<OffsetDateTime> blackList = new HashSet<>();
    private LinearFieldValueSource<OffsetDateTime> fieldSource;

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
        fieldSource = new LinearFieldValueSource<>(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0);

        Iterator<OffsetDateTime> iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(date.atTime(LocalTime.of(12, 0, 0)), ZoneOffset.UTC)));

        rng.setNextDouble(ALMOST_ONE);

        // Because internally the filteringIterator pre-generates the first value before we can set
        // the new "random" value we have re-create the iterator
        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(date.atTime(LocalTime.of(17, 59, 59, 999_000_000)), ZoneOffset.UTC)));

        rng.setNextDouble(0.5);

        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(date.atTime(LocalTime.of(15, 0, 0)), ZoneOffset.UTC)));

    }

    @Test
    public void getRandomValues_withInclusiveUpperBound_shouldGenerateCorrectValues() {
        LocalDate date = LocalDate.of(2018, 1, 10);
        givenLowerBound(OffsetDateTime.of(date.atTime(LocalTime.of(12, 0, 0)), ZoneOffset.UTC), true);
        givenUpperBound(OffsetDateTime.of(date.atTime(LocalTime.of(18, 0, 0)), ZoneOffset.UTC), true);

        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(lowerLimit, upperLimit);


        fieldSource = new LinearFieldValueSource<>(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0);

        Iterator<OffsetDateTime> iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(date.atTime(LocalTime.of(12, 0, 0)), ZoneOffset.UTC)));

        rng.setNextDouble(ALMOST_ONE);

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

        fieldSource = new LinearFieldValueSource<>(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(0);

        Iterator<OffsetDateTime> iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(date.atTime(LocalTime.of(12, 0, 0, 1_000_000)), ZoneOffset.UTC)));

        rng.setNextDouble(ALMOST_ONE);

        // Because internally the filteringIterator pre-generates the first value before we can set
        // the new "random" value we have re-create the iterator
        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(date.atTime(LocalTime.of(18, 0, 0)), ZoneOffset.UTC)));

        rng.setNextDouble(0.5);

        iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(date.atTime(LocalTime.of(15, 0, 0, 1_000_000)), ZoneOffset.UTC)));
    }

    @Test
    public void getRandomValues_withSmallPermittedRangeAtEndOfLegalRange_shouldGenerateCorrectValues() {
        givenLowerBound(OffsetDateTime.of(9999, 12, 31, 23, 0, 0,0, ZoneOffset.UTC), false);

        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(lowerLimit, upperLimit);

        fieldSource = new LinearFieldValueSource<>(restrictions, blackList);

        TestRandomNumberGenerator rng = new TestRandomNumberGenerator();
        rng.setNextDouble(ALMOST_ONE);

        Iterator<OffsetDateTime> iterator = fieldSource.generateRandomValues(rng).iterator();

        Assert.assertThat(iterator.next(),
            equalTo(OffsetDateTime.of(9999, 12, 31, 23, 59, 59, 999_000_000, ZoneOffset.UTC)));
    }

    @Test
    public void shouldBeEqualWhenAllPropertiesMatch(){
        LinearFieldValueSource<OffsetDateTime> a = new LinearFieldValueSource<>(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        LinearFieldValueSource<OffsetDateTime> b = new LinearFieldValueSource<>(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeEqualWhenAllPropertiesMatchWhenBlacklistInDifferentOrder(){
        LinearFieldValueSource<OffsetDateTime> a = new LinearFieldValueSource<>(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        LinearFieldValueSource<OffsetDateTime> b = new LinearFieldValueSource<>(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MAX, OffsetDateTime.MIN)));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeEqualWhenAllPropertiesMatchWhenBlacklistEmpty(){
        LinearFieldValueSource<OffsetDateTime> a = new LinearFieldValueSource<>(
            restrictions("2001-02-03", "2010-11-12"),
            Collections.emptySet());
        LinearFieldValueSource<OffsetDateTime> b = new LinearFieldValueSource<>(
            restrictions("2001-02-03", "2010-11-12"),
            Collections.emptySet());

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeUnequalWhenAllPropertiesMatchExceptMin(){
        LinearFieldValueSource<OffsetDateTime> a = new LinearFieldValueSource<>(
            restrictions("2001-02-28", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        LinearFieldValueSource<OffsetDateTime> b = new LinearFieldValueSource<>(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalWhenAllPropertiesMatchExceptMax(){
        LinearFieldValueSource<OffsetDateTime> a = new LinearFieldValueSource<>(
            restrictions("2001-02-03", "2010-11-30"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        LinearFieldValueSource<OffsetDateTime> b = new LinearFieldValueSource<>(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalWhenAllPropertiesMatchExceptBlacklist(){
        LinearFieldValueSource<OffsetDateTime> a = new LinearFieldValueSource<>(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        LinearFieldValueSource<OffsetDateTime> b = new LinearFieldValueSource<>(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Collections.singletonList(OffsetDateTime.MIN)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalWhenOnlyBlacklistMatches(){
        LinearFieldValueSource<OffsetDateTime> a = new LinearFieldValueSource<>(
            restrictions("2001-02-28", "2010-11-30"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        LinearFieldValueSource<OffsetDateTime> b = new LinearFieldValueSource<>(
            restrictions("2001-02-03", "2010-11-12"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalWhenAllPropertiesDontMatch(){
        LinearFieldValueSource<OffsetDateTime> a = new LinearFieldValueSource<>(
            restrictions("2001-02-28", "2010-11-30"),
            new HashSet<>(Arrays.asList(OffsetDateTime.MIN, OffsetDateTime.MAX)));
        LinearFieldValueSource<OffsetDateTime> b = new LinearFieldValueSource<>(
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

        LinearFieldValueSource<OffsetDateTime> datesAfterLastPermittedDate = new LinearFieldValueSource<>(min, Collections.emptySet());

        //Act
        Iterator<OffsetDateTime> iterator = datesAfterLastPermittedDate.generateAllValues().iterator();
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
        LinearFieldValueSource<OffsetDateTime> datesBeforeFirstPermittedDate = new LinearFieldValueSource<>(max, Collections.emptySet());

        //Act
        Iterator<OffsetDateTime> iterator = datesBeforeFirstPermittedDate.generateAllValues().iterator();
        //Assert
        Assert.assertThat(iterator.hasNext(), is(false));
    }

    private LinearRestrictions<OffsetDateTime> restrictions(String min, String max){
        return LinearRestrictionsFactory.createDateTimeRestrictions(
            min == null ? null : getTimeLimit(min),
            max == null ? null : getTimeLimit(max)
        );
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

    private void expectAllValues(Object... expectedValuesArray) {
        List<Object> expectedValues = Arrays.asList(expectedValuesArray);
        List<Object> actualValues = new ArrayList<>();

        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(lowerLimit, upperLimit);
        fieldSource = new LinearFieldValueSource<>(restrictions, blackList);

        fieldSource.generateAllValues().forEach(actualValues::add);

        Assert.assertThat(actualValues, equalTo(expectedValues));
    }
}

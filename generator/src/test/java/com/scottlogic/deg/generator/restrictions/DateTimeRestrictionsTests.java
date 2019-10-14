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

package com.scottlogic.deg.generator.restrictions;
import com.scottlogic.deg.common.profile.DateTimeGranularity;
import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.scottlogic.deg.common.util.Defaults.ISO_MAX_DATE;
import static com.scottlogic.deg.common.util.Defaults.ISO_MIN_DATE;
import static com.scottlogic.deg.generator.utils.Defaults.DATETIME_MAX_LIMIT;
import static com.scottlogic.deg.generator.utils.Defaults.DATETIME_MIN_LIMIT;
import static java.time.temporal.ChronoUnit.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

class DateTimeRestrictionsTests {

    private static final OffsetDateTime MIN = granularToMillis(Defaults.ISO_MIN_DATE);

    private static final OffsetDateTime MAX = granularToMillis(Defaults.ISO_MAX_DATE);

    private static OffsetDateTime granularToMillis(OffsetDateTime date) {
        return new DateTimeGranularity(MILLIS).trimToGranularity(date);
    }

    @Test
    public void shouldBeEqualIfMinAndMaxMatchOther(){
        LinearRestrictions<OffsetDateTime> a = restrictions(new MockDateTimeLimit(true), new MockDateTimeLimit(true));
        LinearRestrictions<OffsetDateTime> b = restrictions(new MockDateTimeLimit(true), new MockDateTimeLimit(true));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsGreaterThanMin(){
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(
            new Limit<>(OffsetDateTime.MIN, true),
            DATETIME_MAX_LIMIT
        );

        boolean result = restrictions.match(MAX);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsLessThanMax(){
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(
            DATETIME_MIN_LIMIT,
            new Limit<>(OffsetDateTime.MAX, true)
        );

        boolean result = restrictions.match(MIN);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsEqualToMinWhenInclusive(){
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(
            new Limit<>(OffsetDateTime.MIN, true),
            DATETIME_MAX_LIMIT
        );
        boolean result = restrictions.match(MIN);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsEqualToMaxWhenInclusive(){
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(
            DATETIME_MIN_LIMIT,
            new Limit<>(OffsetDateTime.MAX, true)
        );

        boolean result = restrictions.match(MAX);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsEqualToMaxWhenExclusive(){
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(
            DATETIME_MIN_LIMIT,
            new Limit<>(OffsetDateTime.MAX, false)
        );

        boolean result = restrictions.match(OffsetDateTime.MAX);

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsBeforeMin(){
        OffsetDateTime limit = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(
            new Limit<>(limit, false),
            DATETIME_MAX_LIMIT
        );

        boolean result = restrictions.match(limit.minusSeconds(1));

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsAfterMax(){

        OffsetDateTime limit = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(
            DATETIME_MAX_LIMIT,
            new Limit<>(limit, false)
        );

        boolean result = restrictions.match(limit.plusSeconds(1));

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsAfterMinExclusiveAndBeforeMaxExclusive(){
        OffsetDateTime min = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime max = min.plusHours(1);
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(
            new Limit<>(min, false),
            new Limit<>(max, false)
        );

        boolean result = restrictions.match(min.plusMinutes(1));

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsEqualToMinAndMaxInclusive(){

        OffsetDateTime min = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(
            new Limit<>(min, true),
            new Limit<>(min, true)
        );

        boolean result = restrictions.match(min);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsEqualToMinAndMaxExclusive(){

        OffsetDateTime min = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(
            new Limit<>(min, false),
            new Limit<>(min, false)
        );

        boolean result = restrictions.match(min);

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnFalseIfGranularityIsCoarserThanResult() {
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT, new DateTimeGranularity(MINUTES));
        OffsetDateTime time = OffsetDateTime.of(2000, 01, 03, 04, 05, 06, 0, ZoneOffset.UTC);

        Assert.assertFalse(restrictions.match(time));
    }

    @Test
    public void matchShouldReturnTrueIfGranularityIsTheSameAsResult() {
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT, new DateTimeGranularity(MINUTES));
        OffsetDateTime time = OffsetDateTime.of(2000, 01, 03, 04, 05, 00, 0, ZoneOffset.UTC);

        Assert.assertTrue(restrictions.match(time));
    }

    @Test
    public void matchShouldReturnTrueIfGranularityIsTheSameAsResultMonths() {
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT,  new DateTimeGranularity(MONTHS));
        OffsetDateTime time = OffsetDateTime.of(2000, 03, 01, 00, 00, 00, 0, ZoneOffset.UTC);

        Assert.assertTrue(restrictions.match(time));
    }

    @Test
    public void matchShouldReturnTrueIfGranularityIsTheSameAsResultYears() {
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT,  new DateTimeGranularity(YEARS));
        OffsetDateTime time = OffsetDateTime.of(2005, 01, 01, 00, 00, 00, 0, ZoneOffset.UTC);

        Assert.assertTrue(restrictions.match(time));
    }

    @Test
    public void limitsShouldBeCappedAtTheMaximumValueAllowedForDateTime() {
       Limit<OffsetDateTime>limit = new Limit<>(OffsetDateTime.MAX,true);
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(DATETIME_MIN_LIMIT, limit,  new DateTimeGranularity(YEARS));

        Assert.assertEquals(restrictions.getMax(), ISO_MAX_DATE);

    }

    @Test
    public void limitsShouldBeCappedAtTheMinimumValueAllowedForDateTime() {
       Limit<OffsetDateTime>limit = new Limit<>(OffsetDateTime.MIN,true);
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(limit, DATETIME_MAX_LIMIT,  new DateTimeGranularity(YEARS));

        Assert.assertEquals(restrictions.getMin(), ISO_MIN_DATE);
    }

    private LinearRestrictions<OffsetDateTime> restrictions(MockDateTimeLimit min, MockDateTimeLimit max){
        return LinearRestrictionsFactory.createDateTimeRestrictions(min, max);
    }

    private class MockDateTimeLimit extends Limit<OffsetDateTime>{

        private final boolean equalToOther;

        public MockDateTimeLimit(boolean equalToOther) {
            super(Defaults.ISO_MAX_DATE, true);
            this.equalToOther = equalToOther;
        }

        @Override
        public boolean equals(Object o) {
            return equalToOther;
        }

        @Override
        public int hashCode() {
            return 1234;
        }
    }
}
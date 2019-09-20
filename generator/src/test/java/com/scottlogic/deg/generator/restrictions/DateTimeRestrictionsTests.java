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

import com.scottlogic.deg.common.profile.constraintdetail.Timescale;
import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.generator.restrictions.linear.DateTimeLimit;
import com.scottlogic.deg.generator.restrictions.linear.DateTimeRestrictions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.scottlogic.deg.generator.restrictions.linear.DateTimeRestrictions.DATETIME_MAX_LIMIT;
import static com.scottlogic.deg.generator.restrictions.linear.DateTimeRestrictions.DATETIME_MIN_LIMIT;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

class DateTimeRestrictionsTests {

    private static final OffsetDateTime MIN = granularToMillis(Defaults.ISO_MIN_DATE);

    private static final OffsetDateTime MAX = granularToMillis(Defaults.ISO_MAX_DATE);

    private static OffsetDateTime granularToMillis(OffsetDateTime date) {
        return Timescale.MILLIS.getGranularityFunction().apply(date);
    }

    @Test
    public void shouldBeEqualIfMinAndMaxMatchOther(){
        DateTimeRestrictions a = restrictions(new MockDateTimeLimit(true), new MockDateTimeLimit(true));
        DateTimeRestrictions b = restrictions(new MockDateTimeLimit(true), new MockDateTimeLimit(true));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeUnequalIfOnlyMinMatches(){
        DateTimeRestrictions a = restrictions(new MockDateTimeLimit(true), new MockDateTimeLimit(false));
        DateTimeRestrictions b = restrictions(new MockDateTimeLimit(true), new MockDateTimeLimit(false));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalIfOnlyMaxMatches(){
        DateTimeRestrictions a = restrictions(new MockDateTimeLimit(false), new MockDateTimeLimit(true));
        DateTimeRestrictions b = restrictions(new MockDateTimeLimit(false), new MockDateTimeLimit(true));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalIfNeitherMinNorMaxMatch(){
        DateTimeRestrictions a = restrictions(new MockDateTimeLimit(false), new MockDateTimeLimit(false));
        DateTimeRestrictions b = restrictions(new MockDateTimeLimit(false), new MockDateTimeLimit(false));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void DateTimeLimitShouldBeEqualIfInclusiveAndLimitMatch(){
        DateTimeLimit a = new DateTimeLimit(OffsetDateTime.MIN, true);
        DateTimeLimit b = new DateTimeLimit(OffsetDateTime.MIN, true);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void DateTimeLimitShouldBeUnequalIfOnlyInclusiveMatches(){
        DateTimeLimit a = new DateTimeLimit(OffsetDateTime.MIN, true);
        DateTimeLimit b = new DateTimeLimit(OffsetDateTime.MAX, true);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void DateTimeLimitShouldBeUnequalIfOnlyLimitMatches(){
        DateTimeLimit a = new DateTimeLimit(OffsetDateTime.MIN, true);
        DateTimeLimit b = new DateTimeLimit(OffsetDateTime.MIN, false);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void DateTimeLimitShouldBeUnequalIfNeitherLimitNotInclusiveMatch(){
        DateTimeLimit a = new DateTimeLimit(OffsetDateTime.MIN, true);
        DateTimeLimit b = new DateTimeLimit(OffsetDateTime.MAX, false);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void matchShouldReturnFalseIfGivenSomethingThatIsntADateTime(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions(
            new DateTimeLimit(OffsetDateTime.MIN, true),
            new DateTimeLimit(OffsetDateTime.MIN, true)
        );

        boolean result = restrictions.match(123);

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsGreaterThanMin(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions(
            new DateTimeLimit(OffsetDateTime.MIN, true),
            DATETIME_MAX_LIMIT
        );

        boolean result = restrictions.match(MAX);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsLessThanMax(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions(
            DATETIME_MIN_LIMIT,
            new DateTimeLimit(OffsetDateTime.MAX, true)
        );

        boolean result = restrictions.match(MIN);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsEqualToMinWhenInclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions(
            new DateTimeLimit(OffsetDateTime.MIN, true),
            DATETIME_MAX_LIMIT
        );
        boolean result = restrictions.match(MIN);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsEqualToMaxWhenInclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions(
            DATETIME_MIN_LIMIT,
            new DateTimeLimit(OffsetDateTime.MAX, true)
        );

        boolean result = restrictions.match(MAX);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsEqualToMinWhenExclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions(
            DATETIME_MIN_LIMIT,
            new DateTimeLimit(OffsetDateTime.MIN, false)
        );
        boolean result = restrictions.match(OffsetDateTime.MIN);

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsEqualToMaxWhenExclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions(
            DATETIME_MIN_LIMIT,
            new DateTimeLimit(OffsetDateTime.MAX, false)
        );

        boolean result = restrictions.match(OffsetDateTime.MAX);

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsBeforeMin(){
        OffsetDateTime limit = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        DateTimeRestrictions restrictions = new DateTimeRestrictions(
            new DateTimeLimit(limit, false),
            DATETIME_MAX_LIMIT
        );

        boolean result = restrictions.match(limit.minusSeconds(1));

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsAfterMax(){

        OffsetDateTime limit = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        DateTimeRestrictions restrictions = new DateTimeRestrictions(
            DATETIME_MAX_LIMIT,
            new DateTimeLimit(limit, false)
        );

        boolean result = restrictions.match(limit.plusSeconds(1));

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsAfterMinExclusiveAndBeforeMaxExclusive(){
        OffsetDateTime min = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime max = min.plusHours(1);
        DateTimeRestrictions restrictions = new DateTimeRestrictions(
            new DateTimeLimit(min, false),
            new DateTimeLimit(max, false)
        );

        boolean result = restrictions.match(min.plusMinutes(1));

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsEqualToMinAndMaxInclusive(){

        OffsetDateTime min = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        DateTimeRestrictions restrictions = new DateTimeRestrictions(
            new DateTimeLimit(min, true),
            new DateTimeLimit(min, true)
        );

        boolean result = restrictions.match(min);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsEqualToMinAndMaxExclusive(){

        OffsetDateTime min = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        DateTimeRestrictions restrictions = new DateTimeRestrictions(
            new DateTimeLimit(min, false),
            new DateTimeLimit(min, false)
        );

        boolean result = restrictions.match(min);

        Assert.assertFalse(result);
    }

    @Test
    public void isAfter_whenSameDateAndBothInclusive_shouldBeTrue(){
        OffsetDateTime value = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        DateTimeLimit self = new DateTimeLimit(value, true);
        DateTimeLimit other = new DateTimeLimit(value, true);

        boolean result = other.isAfter(self.getValue());

        Assert.assertThat(result, is(true));
    }

    @Test
    public void isAfter_whenOtherIsBeforeByOneNanoSecondInclusive_shouldBeFalse(){
        OffsetDateTime selfValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime otherValue = selfValue.plusNanos(-1);
        DateTimeLimit self = new DateTimeLimit(selfValue, true);
        DateTimeLimit other = new DateTimeLimit(otherValue, true);

        boolean result = other.isAfter(self.getValue());

        Assert.assertThat(result, is(false));
    }

    @Test
    public void isAfter_whenOtherIsBeforeByOneNanoSecondExclusive_shouldBeFalse(){
        OffsetDateTime selfValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime otherValue = selfValue.plusNanos(-1);
        DateTimeLimit self = new DateTimeLimit(selfValue, true);
        DateTimeLimit other = new DateTimeLimit(otherValue, false);

        boolean result = other.isAfter(self.getValue());

        Assert.assertThat(result, is(false));
    }

    @Test
    public void isAfter_whenOtherIsBeforeInclusive_shouldBeFalse(){
        OffsetDateTime selfValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime otherValue = OffsetDateTime.of(2000, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        DateTimeLimit self = new DateTimeLimit(selfValue, true);
        DateTimeLimit other = new DateTimeLimit(otherValue, true);

        boolean result = other.isAfter(self.getValue());

        Assert.assertThat(result, is(false));
    }

    @Test
    public void isAfter_whenOtherIsBeforeExclusive_shouldBeFalse(){
        OffsetDateTime selfValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime otherValue = OffsetDateTime.of(2000, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        DateTimeLimit self = new DateTimeLimit(selfValue, true);
        DateTimeLimit other = new DateTimeLimit(otherValue, false);

        boolean result = other.isAfter(self.getValue());

        Assert.assertThat(result, is(false));
    }

    @Test
    public void isAfter_whenOtherIsAfterInclusive_shouldBeTrue(){
        OffsetDateTime selfValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime otherValue = OffsetDateTime.of(2002, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        DateTimeLimit self = new DateTimeLimit(selfValue, true);
        DateTimeLimit other = new DateTimeLimit(otherValue, true);

        boolean result = other.isAfter(self.getValue());

        Assert.assertThat(result, is(true));
    }

    @Test
    public void isAfter_whenOtherIsAfterExclusive_shouldBeTrue(){
        OffsetDateTime selfValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime otherValue = OffsetDateTime.of(2002, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        DateTimeLimit self = new DateTimeLimit(selfValue, true);
        DateTimeLimit other = new DateTimeLimit(otherValue, false);

        boolean result = other.isAfter(self.getValue());

        Assert.assertThat(result, is(true));
    }

    @Test
    public void isAfter_whenOtherIsAfterByOneNanoSecondInclusive_shouldBeTrue(){
        OffsetDateTime selfValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime otherValue = selfValue.plusNanos(1);
        DateTimeLimit self = new DateTimeLimit(selfValue, true);
        DateTimeLimit other = new DateTimeLimit(otherValue, true);

        boolean result = other.isAfter(self.getValue());

        Assert.assertThat(result, is(true));
    }

    @Test
    public void isAfter_whenOtherIsAfterByOneNanoSecondExclusive_shouldBeTrue(){
        OffsetDateTime selfValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime otherValue = selfValue.plusNanos(1);
        DateTimeLimit self = new DateTimeLimit(selfValue, false);
        DateTimeLimit other = new DateTimeLimit(otherValue, false);

        boolean result = other.isAfter(self.getValue());

        Assert.assertThat(result, is(true));
    }

    @Test
    public void matchShouldReturnFalseIfGranularityIsCoarserThanResult() {
        DateTimeRestrictions restrictions = new DateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT, Timescale.MINUTES);
        OffsetDateTime time = OffsetDateTime.of(2000, 01, 03, 04, 05, 06, 0, ZoneOffset.UTC);

        Assert.assertFalse(restrictions.match(time));
    }

    @Test
    public void matchShouldReturnTrueIfGranularityIsTheSameAsResult() {
        DateTimeRestrictions restrictions = new DateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT, Timescale.MINUTES);
        OffsetDateTime time = OffsetDateTime.of(2000, 01, 03, 04, 05, 00, 0, ZoneOffset.UTC);

        Assert.assertTrue(restrictions.match(time));
    }

    @Test
    public void matchShouldReturnTrueIfGranularityIsTheSameAsResultMonths() {
        DateTimeRestrictions restrictions = new DateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT,  Timescale.MONTHS);
        OffsetDateTime time = OffsetDateTime.of(2000, 03, 01, 00, 00, 00, 0, ZoneOffset.UTC);

        Assert.assertTrue(restrictions.match(time));
    }

    @Test
    public void matchShouldReturnTrueIfGranularityIsTheSameAsResultYears() {
        DateTimeRestrictions restrictions = new DateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT,  Timescale.YEARS);
        OffsetDateTime time = OffsetDateTime.of(2005, 01, 01, 00, 00, 00, 0, ZoneOffset.UTC);

        Assert.assertTrue(restrictions.match(time));
    }

    @Test
    public void limitsShouldBeCappedAtTheMaximumValueAllowedForDateTime() {
        DateTimeLimit limit = new DateTimeLimit(OffsetDateTime.MAX,true);
        DateTimeRestrictions restrictions = new DateTimeRestrictions(DATETIME_MIN_LIMIT, limit,  Timescale.YEARS);

        Assert.assertFalse(restrictions.getMax().getValue().isAfter(DATETIME_MAX_LIMIT.getValue()));

    }

    @Test
    public void limitsShouldBeCappedAtTheMinimumValueAllowedForDateTime() {
        DateTimeLimit limit = new DateTimeLimit(OffsetDateTime.MIN,true);
        DateTimeRestrictions restrictions = new DateTimeRestrictions(limit, DATETIME_MAX_LIMIT,  Timescale.YEARS);

        Assert.assertFalse(restrictions.getMax().getValue().isBefore(DATETIME_MIN_LIMIT.getValue()));

    }

    private DateTimeRestrictions restrictions(MockDateTimeLimit min, MockDateTimeLimit max){
        return new DateTimeRestrictions(min, max);
    }

    private class MockDateTimeLimit extends DateTimeLimit{

        private final boolean equalToOther;

        public MockDateTimeLimit(boolean equalToOther) {
            super(OffsetDateTime.MIN, true);
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
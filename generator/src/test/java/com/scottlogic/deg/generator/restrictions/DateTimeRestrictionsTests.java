package com.scottlogic.deg.generator.restrictions;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

class DateTimeRestrictionsTests {
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
        DateTimeRestrictions.DateTimeLimit a = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MIN, true);
        DateTimeRestrictions.DateTimeLimit b = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MIN, true);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void DateTimeLimitShouldBeUnequalIfOnlyInclusiveMatches(){
        DateTimeRestrictions.DateTimeLimit a = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MIN, true);
        DateTimeRestrictions.DateTimeLimit b = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MAX, true);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void DateTimeLimitShouldBeUnequalIfOnlyLimitMatches(){
        DateTimeRestrictions.DateTimeLimit a = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MIN, true);
        DateTimeRestrictions.DateTimeLimit b = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MIN, false);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void DateTimeLimitShouldBeUnequalIfNeitherLimitNotInclusiveMatch(){
        DateTimeRestrictions.DateTimeLimit a = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MIN, true);
        DateTimeRestrictions.DateTimeLimit b = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MAX, false);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void matchShouldReturnFalseIfGivenSomethingThatIsntADateTime(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MIN, true);
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MIN, true);

        boolean result = restrictions.match(123);

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnTrueIfMinAndMaxAreNull(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();

        boolean result = restrictions.match(OffsetDateTime.MIN);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsGreaterThanMin(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MIN, true);

        boolean result = restrictions.match(OffsetDateTime.MAX);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsLessThanMax(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MAX, true);

        boolean result = restrictions.match(OffsetDateTime.MIN);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsEqualToMinWhenInclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MIN, true);

        boolean result = restrictions.match(OffsetDateTime.MIN);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsEqualToMaxWhenInclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MAX, true);

        boolean result = restrictions.match(OffsetDateTime.MAX);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsEqualToMinWhenExclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MIN, false);

        boolean result = restrictions.match(OffsetDateTime.MIN);

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsEqualToMaxWhenExclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(OffsetDateTime.MAX, false);

        boolean result = restrictions.match(OffsetDateTime.MAX);

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsBeforeMin(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        OffsetDateTime limit = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        restrictions.min = new DateTimeRestrictions.DateTimeLimit(limit, false);

        boolean result = restrictions.match(limit.minusSeconds(1));

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsAfterMax(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        OffsetDateTime limit = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(limit, false);

        boolean result = restrictions.match(limit.plusSeconds(1));

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsAfterMinExclusiveAndBeforeMaxExclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        OffsetDateTime min = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime max = min.plusHours(1);
        restrictions.min = new DateTimeRestrictions.DateTimeLimit(min, false);
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(max, false);

        boolean result = restrictions.match(min.plusMinutes(1));

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsEqualToMinAndMaxInclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        OffsetDateTime min = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        restrictions.min = new DateTimeRestrictions.DateTimeLimit(min, true);
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(min, true);

        boolean result = restrictions.match(min);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsEqualToMinAndMaxExclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        OffsetDateTime min = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        restrictions.min = new DateTimeRestrictions.DateTimeLimit(min, false);
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(min, false);

        boolean result = restrictions.match(min);

        Assert.assertFalse(result);
    }

    @Test
    public void isAfter_whenSameDateAndBothInclusive_shouldBeFalse(){
        OffsetDateTime value = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        DateTimeRestrictions.DateTimeLimit self = new DateTimeRestrictions.DateTimeLimit(value, true);
        DateTimeRestrictions.DateTimeLimit other = new DateTimeRestrictions.DateTimeLimit(value, true);

        boolean result = other.isAfter(self);

        Assert.assertThat(result, is(false));
    }

    @Test
    public void isAfter_whenSameDateAndOtherIsExclusive_shouldBeTrue(){
        OffsetDateTime value = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        DateTimeRestrictions.DateTimeLimit self = new DateTimeRestrictions.DateTimeLimit(value, true);
        DateTimeRestrictions.DateTimeLimit other = new DateTimeRestrictions.DateTimeLimit(value, false);

        boolean result = other.isAfter(self);

        Assert.assertThat(result, is(true));
    }

    @Test
    public void isAfter_whenOtherIsBeforeByOneNanoSecondInclusive_shouldBeFalse(){
        OffsetDateTime selfValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime otherValue = selfValue.plusNanos(-1);
        DateTimeRestrictions.DateTimeLimit self = new DateTimeRestrictions.DateTimeLimit(selfValue, true);
        DateTimeRestrictions.DateTimeLimit other = new DateTimeRestrictions.DateTimeLimit(otherValue, true);

        boolean result = other.isAfter(self);

        Assert.assertThat(result, is(false));
    }

    @Test
    public void isAfter_whenOtherIsBeforeByOneNanoSecondExclusive_shouldBeFalse(){
        OffsetDateTime selfValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime otherValue = selfValue.plusNanos(-1);
        DateTimeRestrictions.DateTimeLimit self = new DateTimeRestrictions.DateTimeLimit(selfValue, true);
        DateTimeRestrictions.DateTimeLimit other = new DateTimeRestrictions.DateTimeLimit(otherValue, false);

        boolean result = other.isAfter(self);

        Assert.assertThat(result, is(false));
    }

    @Test
    public void isAfter_whenOtherIsBeforeInclusive_shouldBeFalse(){
        OffsetDateTime selfValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime otherValue = OffsetDateTime.of(2000, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        DateTimeRestrictions.DateTimeLimit self = new DateTimeRestrictions.DateTimeLimit(selfValue, true);
        DateTimeRestrictions.DateTimeLimit other = new DateTimeRestrictions.DateTimeLimit(otherValue, true);

        boolean result = other.isAfter(self);

        Assert.assertThat(result, is(false));
    }

    @Test
    public void isAfter_whenOtherIsBeforeExclusive_shouldBeFalse(){
        OffsetDateTime selfValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime otherValue = OffsetDateTime.of(2000, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        DateTimeRestrictions.DateTimeLimit self = new DateTimeRestrictions.DateTimeLimit(selfValue, true);
        DateTimeRestrictions.DateTimeLimit other = new DateTimeRestrictions.DateTimeLimit(otherValue, false);

        boolean result = other.isAfter(self);

        Assert.assertThat(result, is(false));
    }

    @Test
    public void isAfter_whenOtherIsAfterInclusive_shouldBeTrue(){
        OffsetDateTime selfValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime otherValue = OffsetDateTime.of(2002, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        DateTimeRestrictions.DateTimeLimit self = new DateTimeRestrictions.DateTimeLimit(selfValue, true);
        DateTimeRestrictions.DateTimeLimit other = new DateTimeRestrictions.DateTimeLimit(otherValue, true);

        boolean result = other.isAfter(self);

        Assert.assertThat(result, is(true));
    }

    @Test
    public void isAfter_whenOtherIsAfterExclusive_shouldBeTrue(){
        OffsetDateTime selfValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime otherValue = OffsetDateTime.of(2002, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        DateTimeRestrictions.DateTimeLimit self = new DateTimeRestrictions.DateTimeLimit(selfValue, true);
        DateTimeRestrictions.DateTimeLimit other = new DateTimeRestrictions.DateTimeLimit(otherValue, false);

        boolean result = other.isAfter(self);

        Assert.assertThat(result, is(true));
    }

    @Test
    public void isAfter_whenOtherIsAfterByOneNanoSecondInclusive_shouldBeTrue(){
        OffsetDateTime selfValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime otherValue = selfValue.plusNanos(1);
        DateTimeRestrictions.DateTimeLimit self = new DateTimeRestrictions.DateTimeLimit(selfValue, true);
        DateTimeRestrictions.DateTimeLimit other = new DateTimeRestrictions.DateTimeLimit(otherValue, true);

        boolean result = other.isAfter(self);

        Assert.assertThat(result, is(true));
    }

    @Test
    public void isAfter_whenOtherIsAfterByOneNanoSecondExclusive_shouldBeTrue(){
        OffsetDateTime selfValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime otherValue = selfValue.plusNanos(1);
        DateTimeRestrictions.DateTimeLimit self = new DateTimeRestrictions.DateTimeLimit(selfValue, false);
        DateTimeRestrictions.DateTimeLimit other = new DateTimeRestrictions.DateTimeLimit(otherValue, false);

        boolean result = other.isAfter(self);

        Assert.assertThat(result, is(true));
    }

    private DateTimeRestrictions restrictions(MockDateTimeLimit min, MockDateTimeLimit max){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = min;
        restrictions.max = max;
        return restrictions;
    }

    private class MockDateTimeLimit extends DateTimeRestrictions.DateTimeLimit{

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
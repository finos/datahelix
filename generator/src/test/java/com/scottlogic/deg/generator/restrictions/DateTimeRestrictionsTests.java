package com.scottlogic.deg.generator.restrictions;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;

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
        DateTimeRestrictions.DateTimeLimit a = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MIN, true);
        DateTimeRestrictions.DateTimeLimit b = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MIN, true);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void DateTimeLimitShouldBeUnequalIfOnlyInclusiveMatches(){
        DateTimeRestrictions.DateTimeLimit a = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MIN, true);
        DateTimeRestrictions.DateTimeLimit b = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MAX, true);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void DateTimeLimitShouldBeUnequalIfOnlyLimitMatches(){
        DateTimeRestrictions.DateTimeLimit a = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MIN, true);
        DateTimeRestrictions.DateTimeLimit b = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MIN, false);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void DateTimeLimitShouldBeUnequalIfNeitherLimitNotInclusiveMatch(){
        DateTimeRestrictions.DateTimeLimit a = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MIN, true);
        DateTimeRestrictions.DateTimeLimit b = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MAX, false);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void matchShouldReturnFalseIfGivenSomethingThatIsntADateTime(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MIN, true);
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MIN, true);

        boolean result = restrictions.match(123);

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnTrueIfMinAndMaxAreNull(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();

        boolean result = restrictions.match(LocalDateTime.MIN);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsGreaterThanMin(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MIN, true);

        boolean result = restrictions.match(LocalDateTime.MAX);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsLessThanMax(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MAX, true);

        boolean result = restrictions.match(LocalDateTime.MIN);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsEqualToMinWhenInclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MIN, true);

        boolean result = restrictions.match(LocalDateTime.MIN);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsEqualToMaxWhenInclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MAX, true);

        boolean result = restrictions.match(LocalDateTime.MAX);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsEqualToMinWhenExclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MIN, false);

        boolean result = restrictions.match(LocalDateTime.MIN);

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsEqualToMaxWhenExclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(LocalDateTime.MAX, false);

        boolean result = restrictions.match(LocalDateTime.MAX);

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsBeforeMin(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        LocalDateTime limit = LocalDateTime.of(2001, 02, 03, 04, 05, 06);
        restrictions.min = new DateTimeRestrictions.DateTimeLimit(limit, false);

        boolean result = restrictions.match(limit.minusSeconds(1));

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsAfterMax(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        LocalDateTime limit = LocalDateTime.of(2001, 02, 03, 04, 05, 06);
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(limit, false);

        boolean result = restrictions.match(limit.plusSeconds(1));

        Assert.assertFalse(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsAfterMinExclusiveAndBeforeMaxExclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        LocalDateTime min = LocalDateTime.of(2001, 02, 03, 04, 05, 06);
        LocalDateTime max = min.plusHours(1);
        restrictions.min = new DateTimeRestrictions.DateTimeLimit(min, false);
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(max, false);

        boolean result = restrictions.match(min.plusMinutes(1));

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnTrueIfGivenDateIsEqualToMinAndMaxInclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        LocalDateTime min = LocalDateTime.of(2001, 02, 03, 04, 05, 06);
        restrictions.min = new DateTimeRestrictions.DateTimeLimit(min, true);
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(min, true);

        boolean result = restrictions.match(min);

        Assert.assertTrue(result);
    }

    @Test
    public void matchShouldReturnFalseIfGivenDateIsEqualToMinAndMaxExclusive(){
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        LocalDateTime min = LocalDateTime.of(2001, 02, 03, 04, 05, 06);
        restrictions.min = new DateTimeRestrictions.DateTimeLimit(min, false);
        restrictions.max = new DateTimeRestrictions.DateTimeLimit(min, false);

        boolean result = restrictions.match(min);

        Assert.assertFalse(result);
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
            super(LocalDateTime.MIN, true);
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
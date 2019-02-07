package com.scottlogic.deg.generator.restrictions;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

class DateTimeRestrictionsEqualityTests {
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
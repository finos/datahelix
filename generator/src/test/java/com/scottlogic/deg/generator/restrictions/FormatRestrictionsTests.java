package com.scottlogic.deg.generator.restrictions;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

class FormatRestrictionsTests {
    @Test
    public void shouldBeEqualIfFormatStringsAreTheSame(){
        FormatRestrictions a = restriction("abcd");
        FormatRestrictions b = restriction("abcd");

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeUnequalIfFormatStringsAreTheSameButDifferentCase(){
        FormatRestrictions a = restriction("abcd");
        FormatRestrictions b = restriction("ABCD");

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalIfFormatStringsAreDifferent(){
        FormatRestrictions a = restriction("abcd");
        FormatRestrictions b = restriction("efgh");

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalIfOneFormatStringIsNull(){
        FormatRestrictions a = restriction("abcd");
        FormatRestrictions b = restriction(null);

        Assert.assertThat(a, not(equalTo(b)));
        Assert.assertThat(b, not(equalTo(a)));
    }

    private FormatRestrictions restriction(String formatString) {
        FormatRestrictions restrictions = new FormatRestrictions();
        restrictions.formatString = formatString;
        return restrictions;
    }
}
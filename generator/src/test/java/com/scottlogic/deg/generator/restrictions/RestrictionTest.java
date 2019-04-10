package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.MatchesRegexConstraint;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.regex.Pattern;

public class RestrictionTest {

    @Test
    public void shouldFilterNumeric() {
        NumericRestrictions restriction = new NumericRestrictions();
        restriction.min = new NumericLimit<>(new BigDecimal("5"), true);
        restriction.max = new NumericLimit<>(new BigDecimal("10"), false);

        Assert.assertThat(restriction.match(4), Is.is(false));
        Assert.assertThat(restriction.match(5), Is.is(true));
        Assert.assertThat(restriction.match(9), Is.is(true));
        Assert.assertThat(restriction.match(10), Is.is(false));

        Assert.assertThat(restriction.match("lorem ipsum"), Is.is(false));
        Assert.assertThat(restriction.match("5"), Is.is(false));
    }

    @Test
    public void shouldFilterString() {
        StringRestrictions restriction = TextualRestrictions.withStringMatching(Pattern.compile("H(i|ello) World"), false);

        Assert.assertThat(restriction.match("Hello World"), Is.is(true));
        Assert.assertThat(restriction.match("Hi World"), Is.is(true));
        Assert.assertThat(restriction.match("Goodbye"), Is.is(false));

        Assert.assertThat(restriction.match(5), Is.is(false));
    }

}

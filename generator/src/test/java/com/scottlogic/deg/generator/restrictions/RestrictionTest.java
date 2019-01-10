package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.generation.RegexStringGenerator;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;

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
        StringRestrictions restriction = new StringRestrictions(new StringConstraints(Collections.emptySet()));

        restriction.stringGenerator = new RegexStringGenerator("H(i|ello) World", true);

        Assert.assertThat(restriction.match("Hello World"), Is.is(true));
        Assert.assertThat(restriction.match("Hi World"), Is.is(true));
        Assert.assertThat(restriction.match("Goodbye"), Is.is(false));

        Assert.assertThat(restriction.match(5), Is.is(false));
    }

}

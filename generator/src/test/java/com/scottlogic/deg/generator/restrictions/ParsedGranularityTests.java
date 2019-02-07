package com.scottlogic.deg.generator.restrictions;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.core.IsEqual.equalTo;

class ParsedGranularityTests {
    @Test
    public void shouldBeAbleToParseBigDecimalGranularity(){
        ParsedGranularity parsed = ParsedGranularity.parse(BigDecimal.valueOf(0.1));

        Assert.assertThat(parsed.getNumericGranularity(), equalTo(BigDecimal.valueOf(0.1)));
    }

    @Test
    public void shouldPermitAGranularityOf1(){
        ParsedGranularity parsed = ParsedGranularity.parse(BigDecimal.valueOf(1));

        Assert.assertThat(parsed.getNumericGranularity(), equalTo(BigDecimal.valueOf(1)));
    }

    @Test
    public void shouldBeAbleToParseBigIntegerGranularity(){
        ParsedGranularity parsed = ParsedGranularity.parse(BigInteger.ONE);

        Assert.assertThat(parsed.getNumericGranularity(), equalTo(BigDecimal.valueOf(1)));
    }

    @Test
    public void shouldBeAbleToParseIntegerGranularity(){
        ParsedGranularity parsed = ParsedGranularity.parse(1);

        Assert.assertThat(parsed.getNumericGranularity(), equalTo(BigDecimal.valueOf(1)));
    }

    @Test
    public void shouldBeAbleToParseLongGranularity(){
        ParsedGranularity parsed = ParsedGranularity.parse(1L);

        Assert.assertThat(parsed.getNumericGranularity(), equalTo(BigDecimal.valueOf(1)));
    }

    @Ignore("Converting 0.01f (or other fractions) to a BigDecimal results in rounding errors, BigDecimal holds the value 0.009999999776482582, which isn't a fractional power of ten.")
    @Test
    public void shouldBeAbleToParseFloatGranularity(){
        ParsedGranularity parsed = ParsedGranularity.parse(0.01f);

        Assert.assertThat(parsed.getNumericGranularity(), equalTo(BigDecimal.valueOf(0.01)));
    }

    @Test
    public void shouldBeAbleToParseDoubleGranularity(){
        ParsedGranularity parsed = ParsedGranularity.parse(0.1d);

        Assert.assertThat(parsed.getNumericGranularity(), equalTo(BigDecimal.valueOf(0.1)));
    }

    @Test
    public void shouldThrowIfGivenNumberThatIsNotSupported(){
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> ParsedGranularity.parse(new AtomicInteger()));
    }

    @Test
    public void shouldThrowIfGivenNull(){
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> ParsedGranularity.parse(null));
    }

    @Test
    public void shouldThrowIfGivenSomethingOtherThanANumber(){
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> ParsedGranularity.parse("0.1"));
    }

    @Test
    public void shouldThrowIfGivenNumberGreaterThan1(){
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> ParsedGranularity.parse(BigDecimal.valueOf(2)));
    }

    @Test
    public void shouldThrowIfGivenNumberThatIsNotAFractionalPowerOfTen(){
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> ParsedGranularity.parse(BigDecimal.valueOf(0.2)));
    }
}
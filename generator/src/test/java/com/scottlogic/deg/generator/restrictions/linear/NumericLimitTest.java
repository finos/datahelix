package com.scottlogic.deg.generator.restrictions.linear;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;
import static org.junit.jupiter.api.Assertions.*;

class NumericLimitTest {

    @Test
    public void isBefore_lower(){
        NumericLimit one = new NumericLimit(ONE, false);

        assertFalse(one.isBefore(ZERO));
    }

    @Test
    public void isBefore_higher(){
        NumericLimit one = new NumericLimit(ONE, false);

        assertTrue(one.isBefore(TEN));
    }

    @Test
    public void isBefore_lower_inclusive(){
        NumericLimit oneInclusive = new NumericLimit(ONE, true);

        assertFalse(oneInclusive.isBefore(ZERO));
    }

    @Test
    public void isBefore_higher_inclusive(){
        NumericLimit oneInclusive = new NumericLimit(ONE, true);

        assertTrue(oneInclusive.isBefore(TEN));
    }

    @Test
    public void isBefore_same_inclusive(){
        NumericLimit oneInclusive = new NumericLimit(ONE, true);

        assertTrue(oneInclusive.isBefore(ONE));
    }

    @Test
    public void isBefore_same_notInclusive(){
        NumericLimit one = new NumericLimit(ONE, false);

        assertFalse(one.isBefore(ONE));
    }

    @Test
    public void isAfter_lower(){
        NumericLimit one = new NumericLimit(ONE, false);

        assertTrue(one.isAfter(ZERO));
    }

    @Test
    public void isAfter_higher(){
        NumericLimit one = new NumericLimit(ONE, false);

        assertFalse(one.isAfter(TEN));
    }

    @Test
    public void isAfter_lower_inclusive(){
        NumericLimit oneInclusive = new NumericLimit(ONE, true);

        assertTrue(oneInclusive.isAfter(ZERO));
    }

    @Test
    public void isAfter_higher_inclusive(){
        NumericLimit oneInclusive = new NumericLimit(ONE, true);

        assertFalse(oneInclusive.isAfter(TEN));
    }

    @Test
    public void isAfter_same_inclusive(){
        NumericLimit oneInclusive = new NumericLimit(ONE, true);

        assertTrue(oneInclusive.isAfter(ONE));
    }

    @Test
    public void isAfter_same_notInclusive(){
        NumericLimit one = new NumericLimit(ONE, false);

        assertFalse(one.isAfter(ONE));
    }
}
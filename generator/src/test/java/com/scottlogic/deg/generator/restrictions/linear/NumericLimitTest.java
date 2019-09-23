package com.scottlogic.deg.generator.restrictions.linear;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;
import static org.junit.jupiter.api.Assertions.*;

class LimitTest {

    @Test
    public void isBefore_lower(){
        Limit one = new Limit<>(ONE, false);

        assertFalse(one.isBefore(ZERO));
    }

    @Test
    public void isBefore_higher(){
        Limit one = new Limit<>(ONE, false);

        assertTrue(one.isBefore(TEN));
    }

    @Test
    public void isBefore_lower_inclusive(){
        Limit oneInclusive = new Limit<>(ONE, true);

        assertFalse(oneInclusive.isBefore(ZERO));
    }

    @Test
    public void isBefore_higher_inclusive(){
        Limit oneInclusive = new Limit<>(ONE, true);

        assertTrue(oneInclusive.isBefore(TEN));
    }

    @Test
    public void isBefore_same_inclusive(){
        Limit oneInclusive = new Limit<>(ONE, true);

        assertTrue(oneInclusive.isBefore(ONE));
    }

    @Test
    public void isBefore_same_notInclusive(){
        Limit one = new Limit<>(ONE, false);

        assertFalse(one.isBefore(ONE));
    }

    @Test
    public void isAfter_lower(){
        Limit one = new Limit<>(ONE, false);

        assertTrue(one.isAfter(ZERO));
    }

    @Test
    public void isAfter_higher(){
        Limit one = new Limit<>(ONE, false);

        assertFalse(one.isAfter(TEN));
    }

    @Test
    public void isAfter_lower_inclusive(){
        Limit oneInclusive = new Limit<>(ONE, true);

        assertTrue(oneInclusive.isAfter(ZERO));
    }

    @Test
    public void isAfter_higher_inclusive(){
        Limit oneInclusive = new Limit<>(ONE, true);

        assertFalse(oneInclusive.isAfter(TEN));
    }

    @Test
    public void isAfter_same_inclusive(){
        Limit oneInclusive = new Limit<>(ONE, true);

        assertTrue(oneInclusive.isAfter(ONE));
    }
    @Test
    public void isAfter_same_notInclusive(){
        Limit one = new Limit<>(ONE, false);

        assertFalse(one.isAfter(ONE));
    }
}
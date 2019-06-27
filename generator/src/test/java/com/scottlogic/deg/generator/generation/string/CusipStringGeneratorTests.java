package com.scottlogic.deg.generator.generation.string;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CusipStringGeneratorTests {
    @Test
    public void shouldMatchAValidCusipCodeWhenNotNegated(){
        StringGenerator cusipGenerator = new CusipStringGenerator();

        boolean matches = cusipGenerator.match("38259P508");

        assertTrue(matches);
    }

    @Test
    public void shouldNotMatchAnInvalidCusipCodeWhenNotNegated(){
        StringGenerator cusipGenerator = new CusipStringGenerator();

        boolean matches = cusipGenerator.match("not a cusip");

        assertFalse(matches);
    }

    @Test
    public void shouldNotMatchAValidCusipCodeWhenNegated(){
        StringGenerator cusipGenerator = new CusipStringGenerator().complement();

        boolean matches = cusipGenerator.match("38259P508");

        assertFalse(matches);
    }

    @Test
    public void shouldMatchAnInvalidCusipCodeWhenNegated(){
        StringGenerator cusipGenerator = new CusipStringGenerator().complement();

        boolean matches = cusipGenerator.match("not a cusip");

        assertTrue(matches);
    }
}

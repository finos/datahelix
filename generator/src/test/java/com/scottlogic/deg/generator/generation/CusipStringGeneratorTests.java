package com.scottlogic.deg.generator.generation;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class CusipStringGeneratorTests {
    @Test
    public void shouldMatchAValidCusipCodeWhenNotNegated(){
        StringGenerator cusipGenerator = new CusipStringGenerator();

        boolean matches = cusipGenerator.match("38259P508");

        Assert.assertTrue(matches);
    }

    @Test
    public void shouldNotMatchAnInvalidCusipCodeWhenNotNegated(){
        StringGenerator cusipGenerator = new CusipStringGenerator();

        boolean matches = cusipGenerator.match("not a cusip");

        Assert.assertFalse(matches);
    }

    @Test
    public void shouldNotMatchAValidCusipCodeWhenNegated(){
        StringGenerator cusipGenerator = new CusipStringGenerator().complement();

        boolean matches = cusipGenerator.match("38259P508");

        Assert.assertFalse(matches);
    }

    @Test
    public void shouldMatchAnInvalidCusipCodeWhenNegated(){
        StringGenerator cusipGenerator = new CusipStringGenerator().complement();

        boolean matches = cusipGenerator.match("not a cusip");

        Assert.assertTrue(matches);
    }
}

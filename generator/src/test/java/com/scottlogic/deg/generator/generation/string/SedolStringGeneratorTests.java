package com.scottlogic.deg.generator.generation.string;

import com.scottlogic.deg.generator.generation.string.SedolStringGenerator;
import com.scottlogic.deg.generator.generation.string.StringGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class SedolStringGeneratorTests {
    @Test
    public void shouldMatchAValidSedolCodeWhenNotNegated(){
        StringGenerator SedolGenerator = new SedolStringGenerator();

        boolean matches = SedolGenerator.match("2634946");

        Assert.assertTrue(matches);
    }

    @Test
    public void shouldNotMatchAnInvalidSedolCodeWhenNotNegated(){
        StringGenerator SedolGenerator = new SedolStringGenerator();

        boolean matches = SedolGenerator.match("not a sedol");

        Assert.assertFalse(matches);
    }

    @Test
    public void shouldNotMatchAValidSedolCodeWhenNegated(){
        StringGenerator SedolGenerator = new SedolStringGenerator().complement();

        boolean matches = SedolGenerator.match("2634946");

        Assert.assertFalse(matches);
    }

    @Test
    public void shouldMatchAnInvalidSedolCodeWhenNegated(){
        StringGenerator SedolGenerator = new SedolStringGenerator().complement();

        boolean matches = SedolGenerator.match("not a sedol");

        Assert.assertTrue(matches);
    }
}

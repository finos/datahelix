package com.scottlogic.deg.generator.generation.string;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SedolStringGeneratorTests {
    @Test
    public void shouldMatchAValidSedolCodeWhenNotNegated(){
        StringGenerator SedolGenerator = new SedolStringGenerator();

        boolean matches = SedolGenerator.match("2634946");

        assertTrue(matches);
    }

    @Test
    public void shouldNotMatchAnInvalidSedolCodeWhenNotNegated(){
        StringGenerator SedolGenerator = new SedolStringGenerator();

        boolean matches = SedolGenerator.match("not a sedol");

        assertFalse(matches);
    }

    @Test
    public void shouldNotMatchAValidSedolCodeWhenNegated(){
        StringGenerator SedolGenerator = new SedolStringGenerator().complement();

        boolean matches = SedolGenerator.match("2634946");

        assertFalse(matches);
    }

    @Test
    public void shouldMatchAnInvalidSedolCodeWhenNegated(){
        StringGenerator SedolGenerator = new SedolStringGenerator().complement();

        boolean matches = SedolGenerator.match("not a sedol");

        assertTrue(matches);
    }
}

package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.common.profile.NumericGranularity;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LinearFieldValueSourceTest {

    @Test
    public void testGenerateRandomValues() {
        LinearRestrictions<BigDecimal> restrictions = new LinearRestrictions<>(
            BigDecimal.ZERO,
            BigDecimal.valueOf(4),
            new NumericGranularity(0));
        LinearFieldValueSource<BigDecimal> source = new LinearFieldValueSource<>(restrictions, Collections.emptySet());

        Stream<BigDecimal> results = source.generateRandomValues(new JavaUtilRandomNumberGenerator());

        assertTrue(results.limit(100).allMatch(x -> x.intValue() >= 0 & x.intValue() < 5));
    }

}
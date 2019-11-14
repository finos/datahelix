/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scottlogic.datahelix.generator.core.generation.fieldvaluesources;

import com.scottlogic.datahelix.generator.common.profile.NumericGranularity;
import com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictions;
import com.scottlogic.datahelix.generator.core.utils.JavaUtilRandomNumberGenerator;
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
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

package com.scottlogic.datahelix.generator.core.generation.string;

import com.scottlogic.datahelix.generator.core.generation.string.generators.StringGenerator;
import com.scottlogic.datahelix.generator.core.utils.FinancialCodeUtils;
import com.scottlogic.datahelix.generator.core.utils.JavaUtilRandomNumberGenerator;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static com.scottlogic.datahelix.generator.core.generation.string.generators.ChecksumStringGeneratorFactory.createSedolGenerator;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SedolStringGeneratorTests {
    @Test
    public void shouldEndAllSedolsWithValidCheckDigit() {
        StringGenerator target = createSedolGenerator();
        final int NumberOfTests = 100;

        final Iterator<String> allSedols = target.generateAllValues().iterator();

        for (int i = 0; i < NumberOfTests; ++i) {
            final String nextSedol = allSedols.next();
            final char checkDigit = FinancialCodeUtils.calculateSedolCheckDigit(nextSedol.substring(0, 6));
            assertThat(nextSedol.charAt(6), equalTo(checkDigit));
        }
    }

    @Test
    public void shouldEndAllRandomCusipsWithValidCheckDigit() {
        StringGenerator target = createSedolGenerator();

        final int NumberOfTests = 100;

        final Iterator<String> allSedols = target.generateRandomValues(new JavaUtilRandomNumberGenerator()).iterator();

        for (int i = 0; i < NumberOfTests; ++i) {
            final String nextSedol = allSedols.next();
            final char checkDigit = FinancialCodeUtils.calculateSedolCheckDigit(nextSedol.substring(0, 6));
            assertThat(nextSedol.charAt(6), equalTo(checkDigit));
        }
    }
    
    @Test
    public void shouldMatchAValidSedolCodeWhenNotNegated(){
        StringGenerator SedolGenerator = createSedolGenerator();

        boolean matches = SedolGenerator.matches("2634946");

        assertTrue(matches);
    }

    @Test
    public void shouldNotMatchAnInvalidSedolCodeWhenNotNegated(){
        StringGenerator SedolGenerator = createSedolGenerator();

        boolean matches = SedolGenerator.matches("not a sedol");

        assertFalse(matches);
    }
}

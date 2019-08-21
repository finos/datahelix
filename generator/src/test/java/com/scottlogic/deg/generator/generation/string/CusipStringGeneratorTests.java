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

package com.scottlogic.deg.generator.generation.string;

import com.scottlogic.deg.generator.generation.string.generators.StringGenerator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.scottlogic.deg.generator.generation.string.generators.ChecksumStringGeneratorFactory.createCusipGenerator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CusipStringGeneratorTests {
    @Test
    public void shouldMatchAValidCusipCodeWhenNotNegated(){
        StringGenerator cusipGenerator = createCusipGenerator();

        boolean matches = cusipGenerator.matches("38259P508");

        assertTrue(matches);
    }

    @Test
    public void shouldNotMatchAnInvalidCusipCodeWhenNotNegated(){
        StringGenerator cusipGenerator = createCusipGenerator();

        boolean matches = cusipGenerator.matches("not a cusip");

        assertFalse(matches);
    }

    @Test
    @Disabled("Standard constraints e.g. ISINs currently cannot be negated")
    public void shouldNotMatchAValidCusipCodeWhenNegated(){
        StringGenerator cusipGenerator = createCusipGenerator().complement();

        boolean matches = cusipGenerator.matches("38259P508");

        assertFalse(matches);
    }

    @Test
    @Disabled("Standard constraints e.g. ISINs currently cannot be negated")
    public void shouldMatchAnInvalidCusipCodeWhenNegated(){
        StringGenerator cusipGenerator = createCusipGenerator().complement();

        boolean matches = cusipGenerator.matches("not a cusip");

        assertTrue(matches);
    }
}

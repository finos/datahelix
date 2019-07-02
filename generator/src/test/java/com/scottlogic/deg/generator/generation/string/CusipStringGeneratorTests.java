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

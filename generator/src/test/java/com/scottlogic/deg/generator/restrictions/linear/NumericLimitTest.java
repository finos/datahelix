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

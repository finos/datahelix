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

package com.scottlogic.datahelix.generator.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberUtilsTests {

    private static long SQRT_MAX = Math.round(Math.sqrt(Long.MAX_VALUE) - 0.5);
    private static long HALF_MAX = Long.MAX_VALUE / 2;

    @Test
    void multiplyingNonNegativesIsSafe_negativeParametersFail() {
        assertThrows(IllegalArgumentException.class, () -> NumberUtils.multiplyingNonNegativesIsSafe(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> NumberUtils.multiplyingNonNegativesIsSafe(0, -1));
        assertThrows(IllegalArgumentException.class, () -> NumberUtils.multiplyingNonNegativesIsSafe(-1, -1));
        assertThrows(IllegalArgumentException.class, () -> NumberUtils.multiplyingNonNegativesIsSafe(1, -1));
        assertThrows(IllegalArgumentException.class, () -> NumberUtils.multiplyingNonNegativesIsSafe(-1, 1));
    }

    @Test
    void multiplyingNonNegativesIsSafe_zeroParameterAlwaysReturnsTrue() {
        assertTrue(NumberUtils.multiplyingNonNegativesIsSafe(Long.MAX_VALUE, 0));
        assertTrue(NumberUtils.multiplyingNonNegativesIsSafe(0, Long.MAX_VALUE));
    }

    @Test
    void multiplyingNonNegativesIsSafe_withinMaxValueResultsReturnTrue() {
        assertTrue(NumberUtils.multiplyingNonNegativesIsSafe(SQRT_MAX, SQRT_MAX));
        assertTrue(NumberUtils.multiplyingNonNegativesIsSafe(SQRT_MAX, 3));

        assertTrue(NumberUtils.multiplyingNonNegativesIsSafe(1, Long.MAX_VALUE));
        assertTrue(NumberUtils.multiplyingNonNegativesIsSafe(Long.MAX_VALUE, 1));

        assertTrue(NumberUtils.multiplyingNonNegativesIsSafe(HALF_MAX, 2));
        assertTrue(NumberUtils.multiplyingNonNegativesIsSafe(2, HALF_MAX));
        assertTrue(NumberUtils.multiplyingNonNegativesIsSafe(HALF_MAX - 1, 2));
        assertTrue(NumberUtils.multiplyingNonNegativesIsSafe(2, HALF_MAX - 1));
    }

    @Test
    void multiplyingNonNegativesIsSafe_aboveMaxValueResultsReturnFalse() {
        assertFalse(NumberUtils.multiplyingNonNegativesIsSafe(SQRT_MAX +1, SQRT_MAX +1));

        assertFalse(NumberUtils.multiplyingNonNegativesIsSafe(2, Long.MAX_VALUE));
        assertFalse(NumberUtils.multiplyingNonNegativesIsSafe(Long.MAX_VALUE, 2));

        assertFalse(NumberUtils.multiplyingNonNegativesIsSafe(HALF_MAX, 3));
        assertFalse(NumberUtils.multiplyingNonNegativesIsSafe(3, HALF_MAX));
        assertFalse(NumberUtils.multiplyingNonNegativesIsSafe(HALF_MAX + 1, 2));
        assertFalse(NumberUtils.multiplyingNonNegativesIsSafe(2, HALF_MAX + 1));
    }

    @Test
    void addingNonNegativesIsSafe_negativeParametersFail() {
        assertThrows(IllegalArgumentException.class, () -> NumberUtils.addingNonNegativesIsSafe(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> NumberUtils.addingNonNegativesIsSafe(0, -1));
        assertThrows(IllegalArgumentException.class, () -> NumberUtils.addingNonNegativesIsSafe(-1, -1));
        assertThrows(IllegalArgumentException.class, () -> NumberUtils.addingNonNegativesIsSafe(1, -1));
        assertThrows(IllegalArgumentException.class, () -> NumberUtils.addingNonNegativesIsSafe(-1, 1));
    }

    @Test
    void addingNonNegativesIsSafe_zeroParameterAlwaysReturnsTrue() {
        assertTrue(NumberUtils.addingNonNegativesIsSafe(Long.MAX_VALUE, 0));
        assertTrue(NumberUtils.addingNonNegativesIsSafe(0, Long.MAX_VALUE));
    }

    @Test
    void addingNonNegativesIsSafe_withinMaxValueResultsReturnTrue() {
        assertTrue(NumberUtils.addingNonNegativesIsSafe(HALF_MAX, HALF_MAX));

        assertTrue(NumberUtils.addingNonNegativesIsSafe(Long.MAX_VALUE -3, 3));
        assertTrue(NumberUtils.addingNonNegativesIsSafe(3, Long.MAX_VALUE -3));

        assertTrue(NumberUtils.addingNonNegativesIsSafe(1, Long.MAX_VALUE - 1));
        assertTrue(NumberUtils.addingNonNegativesIsSafe(Long.MAX_VALUE - 1, 1));
    }

    @Test
    void addingNonNegativesIsSafe_aboveMaxValueResultsReturnFalse() {
        assertFalse(NumberUtils.addingNonNegativesIsSafe(HALF_MAX + 2, HALF_MAX));
        assertFalse(NumberUtils.addingNonNegativesIsSafe(HALF_MAX, HALF_MAX + 2));

        assertFalse(NumberUtils.addingNonNegativesIsSafe(Long.MAX_VALUE -3, 4));
        assertFalse(NumberUtils.addingNonNegativesIsSafe(4, Long.MAX_VALUE -3));

        assertFalse(NumberUtils.addingNonNegativesIsSafe(2, Long.MAX_VALUE - 1));
        assertFalse(NumberUtils.addingNonNegativesIsSafe(Long.MAX_VALUE - 1, 2));
    }
}

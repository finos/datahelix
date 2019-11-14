package com.scottlogic.datahelix.generator.core.generation.string.generators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
class ChecksumlessIsinGeneratorTests {
    @Test
    void matches_withInvalidCountryCode_returnsFalse() {
        String checksumlessIsin = "A000000KPAL";
        ChecksumlessIsinGenerator generator = new ChecksumlessIsinGenerator();

        assertFalse(generator.matches(checksumlessIsin));
    }

    @Test
    void matches_withInvalidLength_returnsFalse() {
        String checksumlessIsin = "ADKPAL";
        ChecksumlessIsinGenerator generator = new ChecksumlessIsinGenerator();

        assertFalse(generator.matches(checksumlessIsin));
    }

    @Test
    void matches_withInvalidGbCode_returnsFalse() {
        String checksumlessIsin = "GB11RJ6BYL2";
        ChecksumlessIsinGenerator generator = new ChecksumlessIsinGenerator();

        assertFalse(generator.matches(checksumlessIsin));
    }

    @Test
    void matches_withValidString_returnsTrue() {
        String checksumlessIsin = "AD00000KPAL";
        ChecksumlessIsinGenerator generator = new ChecksumlessIsinGenerator();

        assertTrue(generator.matches(checksumlessIsin));
    }
}

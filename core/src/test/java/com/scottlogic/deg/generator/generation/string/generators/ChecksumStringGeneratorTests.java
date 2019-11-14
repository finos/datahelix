package com.scottlogic.deg.generator.generation.string.generators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
class ChecksumStringGeneratorTests {
    @Test
    void matches_withInvalidPreChecksum_returnsFalse() {
        String preChecksum = "abc";
        Character checksum = 'a';
        StringGenerator mockGenerator = mock(StringGenerator.class);
        when(mockGenerator.matches(preChecksum)).thenReturn(false);
        ChecksumMaker mockChecksumMaker = input -> input.charAt(0);

        ChecksumStringGenerator checksumStringGenerator = new ChecksumStringGenerator(mockGenerator, mockChecksumMaker);

        assertFalse(checksumStringGenerator.matches(preChecksum + checksum));
    }

    @Test
    void matches_withInvalidChecksum_returnsFalse() {
        String preChecksum = "abc";
        Character checksum = 'b';
        StringGenerator mockGenerator = mock(StringGenerator.class);
        when(mockGenerator.matches(preChecksum)).thenReturn(true);
        ChecksumMaker mockChecksumMaker = input -> input.charAt(0);

        ChecksumStringGenerator checksumStringGenerator = new ChecksumStringGenerator(mockGenerator, mockChecksumMaker);

        assertFalse(checksumStringGenerator.matches(preChecksum + checksum));
    }

    @Test
    void matches_withValidString_returnsTrue() {
        String preChecksum = "abc";
        Character checksum = 'a';
        StringGenerator mockGenerator = mock(StringGenerator.class);
        when(mockGenerator.matches(preChecksum)).thenReturn(true);
        ChecksumMaker mockChecksumMaker = input -> input.charAt(0);

        ChecksumStringGenerator checksumStringGenerator = new ChecksumStringGenerator(mockGenerator, mockChecksumMaker);

        assertTrue(checksumStringGenerator.matches(preChecksum + checksum));
    }
}

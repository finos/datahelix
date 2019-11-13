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

package com.scottlogic.deg.generator.restrictions.string;

import java.util.Collections;
import java.util.regex.Pattern;

import static com.scottlogic.deg.common.util.Defaults.*;

public class StringRestrictionsFactory {
    public static StringRestrictions forStringMatching(Pattern pattern, boolean negate) {
        return new StringRestrictions(
            0,
            MAX_STRING_LENGTH,
            negate
                ? Collections.emptySet()
                : Collections.singleton(pattern),
            Collections.emptySet(),
            Collections.emptySet(),
            negate
                ? Collections.singleton(pattern)
                : Collections.emptySet(),
            Collections.emptySet()
        );
    }

    public static StringRestrictions forStringContaining(Pattern pattern, boolean negate) {
        return new StringRestrictions(
            0,
            MAX_STRING_LENGTH,
            Collections.emptySet(),
            negate
                ? Collections.emptySet()
                : Collections.singleton(pattern),
            Collections.emptySet(),
            Collections.emptySet(),
            negate
                ? Collections.singleton(pattern)
                : Collections.emptySet()
        );
    }

    public static StringRestrictions forLength(int length, boolean negate) {
        return new StringRestrictions(
            negate ? 0 : length,
            negate ? MAX_STRING_LENGTH : length,
            Collections.emptySet(),
            Collections.emptySet(),
            negate
                ? Collections.singleton(length)
                : Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet()
        );
    }

    public static StringRestrictions forMinLength(int length){
        return new StringRestrictions(
            length,
            MAX_STRING_LENGTH,
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet()
        );
    }

    public static StringRestrictions forMaxLength(int length){
        return new StringRestrictions(
            0,
            length,
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet()
        );
    }
}

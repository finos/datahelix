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

package com.scottlogic.datahelix.generator.core.generation.string.generators;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class SingleRegexPattern implements RegexPattern {
    private final String regex;
    private final Predicate<String> predicate;

    public SingleRegexPattern(String regex) {
        this.regex = getPattern(regex);
        this.predicate = Pattern.compile(this.regex).asPredicate();
    }

    @Override
    public String getRepresentation() {
        return regex;
    }

    @Override
    public boolean matches(String input) {
        return predicate.test(input);
    }

    private static String getPattern(String regex){
        String firstStripped = regex.charAt(0) == '/'
            ? regex.substring(1)
            : regex;
        return firstStripped.charAt(firstStripped.length() - 1) == '/'
            ? firstStripped.substring(0, firstStripped.length() - 1)
            : firstStripped;
    }

    @Override
    public RegexPattern complement() {
        return new NegatedRegexPattern(this);
    }
}

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

import java.util.List;
import java.util.stream.Collectors;

public class AnyRegexPatterns implements RegexPattern {
    private final List<RegexPattern> anyPatterns;

    public AnyRegexPatterns(List<RegexPattern> anyPatterns) {
        this.anyPatterns = anyPatterns;
    }

    @Override
    public String getRepresentation() {
        return anyPatterns.stream().map(RegexPattern::getRepresentation).collect(Collectors.joining(" ∪ "));
    }

    @Override
    public boolean matches(String input) {
        return anyPatterns.stream().anyMatch(p -> p.matches(input));
    }

    @Override
    public RegexPattern complement() {
        return new AllRegexPatterns(
            anyPatterns.stream().map(RegexPattern::complement).collect(Collectors.toList())
        );
    }
}

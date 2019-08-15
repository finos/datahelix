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
package com.scottlogic.deg.generator.generation.string.streamy;

import com.scottlogic.deg.generator.generation.string.RegexStringGenerator;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RegexStreamStringGenerator implements StreamStringGenerator{
    private final RegexStringGenerator regexStringGenerator;

    public RegexStreamStringGenerator(String regex) {
        this.regexStringGenerator = new RegexStringGenerator(regex, true);
    }

    @Override
    public Stream<String> generateAllValues() {
        return StreamSupport.stream(regexStringGenerator.generateAllValues().spliterator(), false);
    }

    @Override
    public Stream<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return StreamSupport.stream(regexStringGenerator.generateRandomValues(randomNumberGenerator).spliterator(), false);
    }

    @Override
    public Stream<String> generateInterestingValues() {
        return StreamSupport.stream(regexStringGenerator.generateInterestingValues().spliterator(), false);
    }
}

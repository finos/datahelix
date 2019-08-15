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

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.stream.Stream;

public class PrefixingStreamStringGenerator implements StreamStringGenerator {

    private final String prefix;
    private final StreamStringGenerator innerGenerator;

    public PrefixingStreamStringGenerator(String prefix, StreamStringGenerator innerGenerator) {
        this.prefix = prefix;
        this.innerGenerator = innerGenerator;
    }

    @Override
    public Stream<String> generateAllValues() {
        return innerGenerator.generateAllValues()
            .map(string -> prefix + string);
    }

    @Override
    public Stream<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return innerGenerator.generateRandomValues(randomNumberGenerator)
            .map(string -> prefix + string);
    }

    @Override
    public Stream<String> generateInterestingValues() {
        return innerGenerator.generateInterestingValues()
            .map(string -> prefix + string);
    }
}

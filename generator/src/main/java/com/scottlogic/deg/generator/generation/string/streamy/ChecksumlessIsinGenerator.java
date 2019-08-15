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

import java.util.Arrays;
import java.util.stream.Stream;

import static com.scottlogic.deg.common.util.FlatMappingSpliterator.flatMap;

public class ChecksumlessIsinGenerator implements StreamStringGenerator {

    @Override
    public Stream<String> generateAllValues() {
        Stream<StreamStringGenerator> isinStringGenerators =
            Arrays.stream(IsinCountryCode.values())
                .map(IsinCountryCode::getChecksumlessStringGenerator);

        return flatMap(
            isinStringGenerators,
            StreamStringGenerator::generateAllValues);
    }

    @Override
    public Stream<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return Stream.generate(() ->
            getRandomCountryCode(randomNumberGenerator)
                .getChecksumlessStringGenerator()
                .generateRandomValues(randomNumberGenerator)
                .findFirst().get());
    }

    private IsinCountryCode getRandomCountryCode(RandomNumberGenerator randomNumberGenerator) {
        int random = randomNumberGenerator.nextInt(IsinCountryCode.values().length);
        return IsinCountryCode.values()[random];
    }

    @Override
    public Stream<String> generateInterestingValues() {
        return generateAllValues().limit(2);
    }
}

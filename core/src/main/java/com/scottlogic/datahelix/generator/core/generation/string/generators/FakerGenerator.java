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

import com.github.javafaker.Faker;
import com.scottlogic.datahelix.generator.common.RandomNumberGenerator;
import com.scottlogic.datahelix.generator.common.util.OrderedRandom;

import java.util.function.Function;
import java.util.stream.Stream;

public class FakerGenerator implements StringGenerator {

    private final StringGenerator underlyingRegexGenerator;
    private final Function<Faker, String> fakerFunction;
    private final Faker randomFaker;
    private final Faker orderedFaker;

    public FakerGenerator(StringGenerator underlyingRegexGenerator, Function<Faker, String> fakerFunction) {
        this.underlyingRegexGenerator = underlyingRegexGenerator;
        this.fakerFunction = fakerFunction;
        randomFaker = new Faker();
        orderedFaker = new Faker(new OrderedRandom());
    }

    @Override
    public boolean matches(String string) {
        return false;
    }

    @Override
    public StringGenerator intersect(StringGenerator stringGenerator) {
        if (!(stringGenerator instanceof FakerGenerator)) {
            throw new UnsupportedOperationException("Cannot intersect a faker and non-faker field");
        }
        FakerGenerator other = (FakerGenerator) stringGenerator;
        if (!fakerFunction.equals(other.fakerFunction)) {
            throw new UnsupportedOperationException("Cannot merge two separate faker calls");
        }
        return new FakerGenerator(stringGenerator.intersect(underlyingRegexGenerator), fakerFunction);
    }

    @Override
    public boolean validate(String string) {
        throw new IllegalArgumentException("Cannot validate against Faker results.");
    }

    @Override
    public Stream<String> generateInterestingValues() {
        throw new IllegalArgumentException("Cannot deterministically generate from faker");
    }

    @Override
    public Stream<String> generateAllValues() {
        return Stream.generate(() -> fakerFunction.apply(orderedFaker))
            .filter(underlyingRegexGenerator::validate);
    }

    @Override
    public Stream<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return Stream.generate(() -> fakerFunction.apply(randomFaker))
            .filter(underlyingRegexGenerator::validate);
    }
}

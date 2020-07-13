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

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

public class FakerGenerator implements StringGenerator {

    private final StringGenerator underlyingRegexGenerator;
    private final String fakerSpec;
    private final Faker randomFaker;
    private final Faker orderedFaker;

    public FakerGenerator(StringGenerator underlyingRegexGenerator, String fakerSpec) {
        this.underlyingRegexGenerator = underlyingRegexGenerator;
        this.fakerSpec = fakerSpec;
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
        if (!fakerSpec.equals(other.fakerSpec)) {
            throw new UnsupportedOperationException("Cannot merge two separate faker calls");
        }
        return new FakerGenerator(stringGenerator.intersect(underlyingRegexGenerator), fakerSpec);
    }

    @Override
    public boolean validate(String string) {
        return matches(string);
    }

    @Override
    public Stream<String> generateAllValues() {
        return Stream.generate(() -> getFakerValue(orderedFaker))
            .filter(underlyingRegexGenerator::validate);
    }

    @Override
    public Stream<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return Stream.generate(() -> getFakerValue(randomFaker))
            .filter(underlyingRegexGenerator::validate);
    }

    private String getFakerValue(Faker faker) {
        try {
            return faker.expression("#{" + this.fakerSpec + "}");
        } catch (Exception e) {
            return getFakerValueWorkaround(faker);
        }
    }

    private String getFakerValueWorkaround(Faker faker) {
        String elements[] = this.fakerSpec.split("\\.");
        Object obj = faker;
        for (String element : elements) {
            try {
                obj = obj.getClass().getMethod(element).invoke(obj);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return (String) obj;
    }
}

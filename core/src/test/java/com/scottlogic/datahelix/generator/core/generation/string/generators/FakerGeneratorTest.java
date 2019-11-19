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
import com.scottlogic.datahelix.generator.common.util.Defaults;
import com.scottlogic.datahelix.generator.core.restrictions.string.StringRestrictions;
import com.scottlogic.datahelix.generator.core.restrictions.string.StringRestrictionsFactory;
import com.scottlogic.datahelix.generator.core.utils.JavaUtilRandomNumberGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FakerGeneratorTest {

    @Test
    void generateRandomValuesUnrestricted() {
        StringRestrictions restrictions = StringRestrictionsFactory.forMaxLength(Defaults.MAX_STRING_LENGTH);
        RegexStringGenerator regex = (RegexStringGenerator) restrictions.createGenerator();
        Function<Faker, String> function = f -> f.name().firstName();
        FakerGenerator generator = new FakerGenerator(regex, function);

        final int size = 10;

        List<String> results = generator.generateRandomValues(new JavaUtilRandomNumberGenerator())
            .limit(size)
            .collect(Collectors.toList());

        assertEquals(size, results.size());
    }

    @Test
    void generateRandomValuesRestrictedMin() {
        final int length = 9;
        StringRestrictions restrictions = StringRestrictionsFactory.forMinLength(length);
        RegexStringGenerator regex = (RegexStringGenerator) restrictions.createGenerator();
        Function<Faker, String> function = f -> f.name().firstName();
        FakerGenerator generator = new FakerGenerator(regex, function);

        final int size = 10;

        Stream<String> results = generator.generateRandomValues(new JavaUtilRandomNumberGenerator())
            .limit(size);

        assertTrue(results.allMatch(str -> str.length() >= length));
    }

    @Test
    void generateRandomValuesRestrictedMax() {
        final int length = 4;
        StringRestrictions restrictions = StringRestrictionsFactory.forMaxLength(length);
        RegexStringGenerator regex = (RegexStringGenerator) restrictions.createGenerator();
        Function<Faker, String> function = f -> f.name().firstName();
        FakerGenerator generator = new FakerGenerator(regex, function);

        final int size = 10;

        Stream<String> results = generator.generateRandomValues(new JavaUtilRandomNumberGenerator())
            .limit(size);

        assertTrue(results.allMatch(str -> str.length() <= length));
    }
}

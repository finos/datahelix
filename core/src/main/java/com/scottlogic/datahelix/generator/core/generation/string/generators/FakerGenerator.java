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

import com.scottlogic.datahelix.generator.common.RandomNumberGenerator;
import com.scottlogic.datahelix.generator.core.generation.string.generators.faker.FakeValueProvider;
import com.scottlogic.datahelix.generator.core.generation.string.generators.faker.FakerSpecFakeValueProvider;
import com.scottlogic.datahelix.generator.core.generation.string.generators.faker.IntersectingFakeValueProvider;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FakerGenerator implements StringGenerator {

    private final StringGenerator underlyingRegexGenerator;
    private final FakeValueProvider fakeValuesService;

    public FakerGenerator(StringGenerator underlyingRegexGenerator, String fakerSpec) {
        this(underlyingRegexGenerator, new FakerSpecFakeValueProvider(Locale.getDefault(), fakerSpec));
    }

    public FakerGenerator(StringGenerator underlyingRegexGenerator, FakeValueProvider fakeValuesService) {
        this.underlyingRegexGenerator = underlyingRegexGenerator;
        this.fakeValuesService = fakeValuesService;
    }

    @Override
    public boolean matches(String string) {
        return underlyingRegexGenerator.matches(string)
            && fakeValuesService.hasValue(string, underlyingRegexGenerator::validate);
    }

    @Override
    public StringGenerator intersect(StringGenerator stringGenerator) {
        if (!(stringGenerator instanceof FakerGenerator)) {
            return new FakerGenerator(
                stringGenerator.intersect(underlyingRegexGenerator),
                fakeValuesService);
        }

        FakerGenerator other = (FakerGenerator) stringGenerator;
        return new FakerGenerator(
            stringGenerator.intersect(underlyingRegexGenerator),
            new IntersectingFakeValueProvider(fakeValuesService, other.fakeValuesService));
    }

    @Override
    public boolean validate(String string) {
        return matches(string);
    }

    @Override
    public Stream<String> generateInterestingValues() {
        throw new IllegalArgumentException("Cannot deterministically generate from faker");
    }

    @Override
    public Stream<String> generateAllValues() {
        return fakeValuesService.getAllValues(underlyingRegexGenerator::validate);
    }

    @Override
    public Stream<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        List<String> allFakeValues = fakeValuesService.getAllValues(underlyingRegexGenerator::validate).collect(Collectors.toList());

        return Stream.generate(() -> allFakeValues.get(randomNumberGenerator.nextInt(allFakeValues.size() - 1)));
    }
}

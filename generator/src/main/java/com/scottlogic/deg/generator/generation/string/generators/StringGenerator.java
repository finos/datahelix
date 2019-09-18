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
package com.scottlogic.deg.generator.generation.string.generators;

import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import com.scottlogic.deg.generator.utils.UpCastingIterator;

import java.util.function.Function;
import java.util.stream.Stream;

public interface StringGenerator {
    Stream<String> generateAllValues();

    Stream<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator);

    Stream<String> generateInterestingValues();

    boolean matches(String string);

    StringGenerator intersect(StringGenerator stringGenerator);

    default StringGenerator complement() {
        throw new UnsupportedOperationException();
    }

    default FieldValueSource asFieldValueSource() {
        return new StringGeneratorAsFieldValueSource(this);
    }

    // Adapter
    class StringGeneratorAsFieldValueSource implements FieldValueSource {
        private final StringGenerator underlyingGenerator;

        StringGeneratorAsFieldValueSource(StringGenerator underlyingGenerator) {
            this.underlyingGenerator = underlyingGenerator;
        }

        @Override
        public Iterable<Object> generateInterestingValues() {
            return () -> new UpCastingIterator<>(
                underlyingGenerator.generateInterestingValues().iterator());
        }

        @Override
        public Stream<Object> generateAllValues() {
            return underlyingGenerator.generateAllValues()
                .map(Function.identity());
        }

        @Override
        public Stream<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
            return underlyingGenerator.generateRandomValues(randomNumberGenerator)
                .map(Function.identity());
        }
    }
}

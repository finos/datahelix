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

package com.scottlogic.deg.generator.generation.string;

import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.generation.string.streamy.StreamStringGenerator;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import com.scottlogic.deg.generator.utils.UpCastingIterator;

import java.util.stream.StreamSupport;

public interface StringGenerator {
    StringGenerator intersect(StringGenerator stringGenerator);
    StringGenerator complement();

    boolean match(String subject);

    Iterable<String> generateInterestingValues();

    Iterable<String> generateAllValues();

    Iterable<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator);

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
        public Iterable<Object> generateAllValues() {
            return () -> new UpCastingIterator<>(
                underlyingGenerator.generateAllValues().iterator());
        }

        @Override
        public Iterable<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
            return () -> new UpCastingIterator<>(
                underlyingGenerator.generateRandomValues(randomNumberGenerator).iterator());
        }
    }

    class StreamStringGeneratorAsFieldValueSource implements FieldValueSource {

        private final StreamStringGenerator underlyingGenerator;

        public StreamStringGeneratorAsFieldValueSource(StreamStringGenerator underlyingGenerator) {
            this.underlyingGenerator = underlyingGenerator;
        }

        @Override
        public Iterable<Object> generateInterestingValues() {
            return () -> new UpCastingIterator<>(
                underlyingGenerator.generateInterestingValues().iterator());
        }

        @Override
        public Iterable<Object> generateAllValues() {
            return () -> new UpCastingIterator<>(
                underlyingGenerator.generateAllValues().iterator());
        }

        @Override
        public Iterable<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
            return () -> new UpCastingIterator<>(
                underlyingGenerator.generateRandomValues(randomNumberGenerator).iterator());
        }
    }
}

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

package com.scottlogic.datahelix.generator.profile.custom;

import com.scottlogic.datahelix.generator.custom.CustomGenerator;
import com.scottlogic.datahelix.generator.core.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.datahelix.generator.common.RandomNumberGenerator;

import java.util.stream.Stream;

public class CustomFieldValueSource<T> implements FieldValueSource<T> {
    private final CustomGenerator customGenerator;
    private final boolean negated;

    public CustomFieldValueSource(CustomGenerator customGenerator, boolean negated) {
        this.customGenerator = customGenerator;
        this.negated = negated;
    }

    @Override
    public Stream<T> generateInterestingValues() {
        return generateAllValues().limit(2);
    }

    @Override
    public Stream<T> generateAllValues() {
        if (negated){
            return customGenerator.generateNegatedSequential();
        }
        return customGenerator.generateSequential();
    }

    @Override
    public Stream<T> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        if (negated){
            return customGenerator.generateNegatedRandom();
        }
        return customGenerator.generateRandom();
    }
}

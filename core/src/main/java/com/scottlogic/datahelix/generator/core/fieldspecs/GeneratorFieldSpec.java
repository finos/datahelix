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
package com.scottlogic.datahelix.generator.core.fieldspecs;

import com.scottlogic.datahelix.generator.core.generation.fieldvaluesources.FieldValueSource;

import java.util.Optional;
import java.util.function.Predicate;

public class GeneratorFieldSpec extends FieldSpec {
    private final FieldValueSource fieldValueSource;
    private final Predicate<Object> acceptLegalValueFunction;

    public GeneratorFieldSpec(FieldValueSource fieldValueSource, Predicate<Object> acceptLegalValueFunction, boolean nullable) {
        super(nullable);
        this.fieldValueSource = fieldValueSource;
        this.acceptLegalValueFunction = acceptLegalValueFunction;
    }

    @Override
    public boolean canCombineWithLegalValue(Object value) {
        return acceptLegalValueFunction.test(value);
    }

    @Override
    public FieldValueSource getFieldValueSource() {
        return fieldValueSource;
    }

    @Override
    public Optional<FieldSpec> merge(FieldSpec other, boolean useFinestGranularityAvailable) {
        final boolean notNullable = !isNullable() || !other.isNullable();

        if (other instanceof GeneratorFieldSpec) {
            throw new UnsupportedOperationException("generators cannot be combined");
        }

        return Optional.of(notNullable ? withNotNull() : this);
    }

    @Override
    public FieldSpec withNotNull() {
        return new GeneratorFieldSpec(fieldValueSource, acceptLegalValueFunction, false);
    }
}

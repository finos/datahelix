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

package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.config.detail.DataGenerationType;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.generation.fieldvaluesources.CombiningFieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FieldSpecValueGenerator {
    private final DataGenerationType dataType;
    private final FieldValueSourceEvaluator sourceFactory;
    private final JavaUtilRandomNumberGenerator randomNumberGenerator;

    @Inject
    public FieldSpecValueGenerator(DataGenerationType dataGenerationType,
                                   FieldValueSourceEvaluator sourceEvaluator,
                                   JavaUtilRandomNumberGenerator randomNumberGenerator) {
        this.dataType = dataGenerationType;
        this.sourceFactory = sourceEvaluator;
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public boolean isRandom() {
        return dataType == DataGenerationType.RANDOM;
    }

    public Stream<DataBagValue> generate(Field field, Set<FieldSpec> specs) {
        List<FieldValueSource> fieldValueSources = specs.stream()
            .map(sourceFactory::getFieldValueSources)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());

        return createValuesFromSources(field, specs.stream().findFirst().orElse(FieldSpec.Empty), fieldValueSources);
    }

    public Stream<DataBagValue> generate(Field field, FieldSpec spec) {
        List<FieldValueSource> fieldValueSources = sourceFactory.getFieldValueSources(spec);

        return createValuesFromSources(field, spec, fieldValueSources);
    }

    private Stream<DataBagValue> createValuesFromSources(Field field, FieldSpec spec, List<FieldValueSource> fieldValueSources) {
        FieldValueSource combinedFieldValueSource = new CombiningFieldValueSource(fieldValueSources);

        Iterable<Object> iterable = getDataValues(combinedFieldValueSource, field.isUnique());

        return StreamSupport.stream(iterable.spliterator(), false)
            .map(value -> new DataBagValue(value, spec.getFormatting()));
    }

    private Iterable<Object> getDataValues(FieldValueSource source, boolean unique) {
        if (unique) {
            return source.generateAllValues();
        }
        switch (dataType) {
            case FULL_SEQUENTIAL:
                return source.generateAllValues();
            case INTERESTING:
                return source.generateInterestingValues();
            case RANDOM:
                return source.generateRandomValues(randomNumberGenerator);
            default:
                throw new UnsupportedOperationException("No data generation type set.");
        }
    }
}


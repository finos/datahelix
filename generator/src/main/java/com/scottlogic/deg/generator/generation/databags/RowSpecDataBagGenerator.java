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

package com.scottlogic.deg.generator.generation.databags;
import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RowSpecDataBagGenerator {
    private final FieldSpecValueGenerator generator;
    private final CombinationStrategy combinationStrategy;

    @Inject
    public RowSpecDataBagGenerator(
        FieldSpecValueGenerator generator,
        CombinationStrategy combinationStrategy)
    {
        this.generator = generator;
        this.combinationStrategy = combinationStrategy;
    }

    public Stream<DataBag> createDataBags(RowSpec rowSpec) {
        Supplier<Stream<Stream<DataBag>>> dataBagsForFields = () ->
            rowSpec.getFields().stream()
                .map(field -> generateDataForField(rowSpec, field));

        return combinationStrategy.permute(dataBagsForFields);
    }

    private Stream<DataBag> generateDataForField(RowSpec rowSpec, Field field) {
        FieldSpec fieldSpec = rowSpec.getSpecForField(field);
        Stream<DataBagValue> stream = generator.generate(fieldSpec);
        return stream.map(value -> toDataBag(field, value));
    }

    private DataBag toDataBag(Field field, DataBagValue value) {
        Map<Field, DataBagValue> map = new HashMap<>();
        map.put(field, value);
        return new DataBag(map);
    }
}

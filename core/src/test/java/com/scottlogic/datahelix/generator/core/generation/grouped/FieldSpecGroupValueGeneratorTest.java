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
package com.scottlogic.datahelix.generator.core.generation.grouped;

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.core.config.detail.CombinationStrategyType;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpec;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpecFactory;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpecGroup;
import com.scottlogic.datahelix.generator.core.generation.FieldSpecValueGenerator;
import com.scottlogic.datahelix.generator.core.generation.databags.DataBag;
import com.scottlogic.datahelix.generator.core.generation.databags.DataBagValue;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static com.scottlogic.datahelix.generator.common.profile.FieldBuilder.createField;

class FieldSpecGroupValueGeneratorTest {
    @Test
    public void generate_withGroupOfSingleField_returnsCorrectStream() {
        Map<Field, FieldSpec> specMap = new HashMap<>();
        Field firstField = createField("first");
        FieldSpec firstSpec = FieldSpecFactory.fromType(firstField.getType());
        specMap.put(firstField, FieldSpecFactory.fromType(firstField.getType()));

        FieldSpecValueGenerator underlyingGenerator = mock(FieldSpecValueGenerator.class);
        String result = "result";
        DataBagValue firstValue = new DataBagValue(result);
        when(underlyingGenerator.generate(any(Field.class), eq(firstSpec))).thenReturn(Stream.of(firstValue));

        FieldSpecGroupValueGenerator generator = new FieldSpecGroupValueGenerator(underlyingGenerator, CombinationStrategyType.MINIMAL);

        FieldSpecGroup group = new FieldSpecGroup(specMap, Collections.emptyList());

        Stream<DataBag> stream = generator.generate(group);

        Map<Field, DataBagValue> dataBag = new HashMap<>();
        dataBag.put(firstField, firstValue);
        assertEquals(Collections.singleton(new DataBag(dataBag)), stream.collect(Collectors.toSet()));
    }

}
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

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.builders.DataBagBuilder;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.deg.generator.generation.combinationstrategies.ExhaustiveCombinationStrategy;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

class RowSpecDataBagGeneratorTests {

    private CombinationStrategy exhaustiveCombinationStrategy = new ExhaustiveCombinationStrategy();
    private FieldSpecValueGenerator mockGeneratorFactory = mock(FieldSpecValueGenerator.class);
    private CombinationStrategy mockCombinationStrategy = mock(CombinationStrategy.class);

    private Field field = new Field("Field1", false);
    Field field2 = new Field("field2", false);
    Field field3 = new Field("field3", false);
    private ProfileFields fields = new ProfileFields(Collections.singletonList(field));
    private FieldSpec fieldSpec = mock(FieldSpec.class);
    private FieldSpec fieldSpec2 = mock(FieldSpec.class);
    private FieldSpec fieldSpec3 = mock(FieldSpec.class);

    DataBagValue dataBagValue = new DataBagValue(field, "value");
    DataBagValue dataBagValue1 = new DataBagValue(field2, "value");
    DataBagValue dataBagValue2 = new DataBagValue(field3, "value");

    @Test
    void shouldCreateValuesForEachFieldSpecInRowSpec() {
        RowSpecDataBagGenerator factory =
            new RowSpecDataBagGenerator(mockGeneratorFactory, exhaustiveCombinationStrategy);
        Map<Field, FieldSpec> map = new HashMap<Field, FieldSpec>() {{ put(field, fieldSpec); }};
        RowSpec rowSpec = new RowSpec(fields, map);

        when(mockGeneratorFactory.generate(any(FieldSpec.class))).thenReturn(Stream.of(dataBagValue));

        List<DataBag> actual = factory.createDataBags(rowSpec).stream()
            .collect(Collectors.toList());

        verify(mockGeneratorFactory, times(1)).generate(fieldSpec);

        List<DataBag> expected = Arrays.asList(new DataBagBuilder().set(field, dataBagValue).build());

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    void factoryIsCalledForEachField() {
        RowSpecDataBagGenerator factory =
            new RowSpecDataBagGenerator(mockGeneratorFactory, exhaustiveCombinationStrategy);
        Map<Field, FieldSpec> map = new HashMap<Field, FieldSpec>() {{
            put(field, fieldSpec);
            put(field2, fieldSpec2);
            put(field3, fieldSpec3); }};
        RowSpec rowSpec = new RowSpec(new ProfileFields(Arrays.asList(field2, field, field3)), map);

        when(mockGeneratorFactory.generate(any(FieldSpec.class)))
            .thenReturn(Stream.of(dataBagValue), Stream.of(dataBagValue1), Stream.of(dataBagValue2));

        factory.createDataBags(rowSpec).stream()
            .collect(Collectors.toList());

        verify(mockGeneratorFactory, times(1)).generate(fieldSpec);
        verify(mockGeneratorFactory, times(1)).generate(fieldSpec);
        verify(mockGeneratorFactory, times(1)).generate(fieldSpec);
    }

    @Test
    void combinationStrategyIsCalled() {
        RowSpecDataBagGenerator factory =
            new RowSpecDataBagGenerator(mockGeneratorFactory, mockCombinationStrategy);
        Map<Field, FieldSpec> map = new HashMap<Field, FieldSpec>() {{ put(field, fieldSpec); }};
        RowSpec rowSpec = new RowSpec(fields, map);

        factory.createDataBags(rowSpec);

        verify(mockCombinationStrategy, times(1)).permute(any());
    }
}
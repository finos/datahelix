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
package com.scottlogic.deg.generator.fieldspecs;

import com.google.common.collect.ImmutableMap;
import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.common.profile.FieldType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static com.scottlogic.datahelix.generator.common.profile.FieldBuilder.createField;

class RowSpecMergerTest {
    RowSpecMerger rowSpecMerger = new RowSpecMerger(new FieldSpecMerger());

    FieldSpec isNull = FieldSpecFactory.nullOnly();
    FieldSpec notNull = FieldSpecFactory.fromType(FieldType.STRING).withNotNull();
    Field A = createField("A");
    Field B = createField("B");
    Fields fields = new Fields(Arrays.asList(A, B));

    @Test
    void merge_notContradictoryForField() {
        RowSpec left = new RowSpec(fields, ImmutableMap.of(A, isNull), Collections.emptyList());
        RowSpec right = new RowSpec(fields, ImmutableMap.of(A, isNull), Collections.emptyList());

        RowSpec merged = rowSpecMerger.merge(left, right).get();

        assertEquals(merged.getSpecForField(A), isNull);
    }

    @Test
    void merge_contradictoryForField() {
        RowSpec left = new RowSpec(fields, ImmutableMap.of(A, isNull), Collections.emptyList());
        RowSpec right = new RowSpec(fields, ImmutableMap.of(A, notNull), Collections.emptyList());

        Optional<RowSpec> merged = rowSpecMerger.merge(left, right);

        assertEquals(merged, Optional.empty());
    }


    @Test
    void merge_twoFields() {
        RowSpec left = new RowSpec(fields, ImmutableMap.of(A, isNull), Collections.emptyList());
        RowSpec right = new RowSpec(fields, ImmutableMap.of(B, notNull), Collections.emptyList());

        RowSpec merged = rowSpecMerger.merge(left, right).get();
        assertEquals(merged.getSpecForField(A), (isNull));
        assertEquals(merged.getSpecForField(B), (notNull));
    }
}
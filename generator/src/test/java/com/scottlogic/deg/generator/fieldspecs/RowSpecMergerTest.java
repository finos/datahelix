package com.scottlogic.deg.generator.fieldspecs;

import com.google.common.collect.ImmutableMap;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.FieldType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

class RowSpecMergerTest {
    RowSpecMerger rowSpecMerger = new RowSpecMerger(new FieldSpecMerger());

    FieldSpec isNull = FieldSpecFactory.nullOnly();
    FieldSpec notNull = FieldSpecFactory.fromType(FieldType.STRING).withNotNull();
    Field A = createField("A");
    Field B = createField("B");
    ProfileFields fields = new ProfileFields(Arrays.asList(A, B));

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
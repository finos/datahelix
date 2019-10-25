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


package com.scottlogic.deg.generator.generation.grouped;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecGroup;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

class RowSpecGrouperTest {

    @Test
    void createGroups_withTwoRelatedFields_givesOneGroupOfSizeOne() {
        Field first = createField("first");
        Field second = createField("second");
        ProfileFields fields = new ProfileFields(Arrays.asList(first, second));

        Map<Field, FieldSpec> fieldSpecMap = fieldSpecMapOf(first, second);

        FieldSpecRelations relation = link(first, second);
        List<FieldSpecRelations> relations = Collections.singletonList(relation);

        RowSpec spec = new RowSpec(fields, fieldSpecMap, relations);

        Set<FieldSpecGroup> groups = RowSpecGrouper.createGroups(spec);

        assertEquals(1, groups.size());
    }

    @Test
    void createGroups_withTwoAndOneFields_givesTwoGroups() {
        Field first = createField("first");
        Field second = createField("second");
        Field third = createField("third");
        ProfileFields fields = new ProfileFields(Arrays.asList(first, second, third));

        Map<Field, FieldSpec> fieldSpecMap = fieldSpecMapOf(first, second, third);

        FieldSpecRelations relation = link(first, second);
        List<FieldSpecRelations> relations = Collections.singletonList(relation);

        RowSpec spec = new RowSpec(fields, fieldSpecMap, relations);

        Set<FieldSpecGroup> groups = RowSpecGrouper.createGroups(spec);

        assertEquals(2, groups.size());
    }

    @Test
    void createGroups_withThreeIndependentFields_givesThreeGroups() {
        Field first = createField("first");
        Field second = createField("second");
        Field third = createField("third");
        ProfileFields fields = new ProfileFields(Arrays.asList(first, second, third));

        Map<Field, FieldSpec> fieldSpecMap = fieldSpecMapOf(first, second, third);

        List<FieldSpecRelations> relations = Collections.emptyList();

        RowSpec spec = new RowSpec(fields, fieldSpecMap, relations);

        Set<FieldSpecGroup> groups = RowSpecGrouper.createGroups(spec);

        assertEquals(3, groups.size());
    }

    @Test
    void createGroups_withThreeCodependentFields_givesOneGroup() {
        Field first = createField("first");
        Field second = createField("second");
        Field third = createField("third");
        ProfileFields fields = new ProfileFields(Arrays.asList(first, second, third));

        Map<Field, FieldSpec> fieldSpecMap = fieldSpecMapOf(first, second, third);

        List<FieldSpecRelations> relations = Arrays.asList(link(first, second), link(second, third));

        RowSpec spec = new RowSpec(fields, fieldSpecMap, relations);

        Set<FieldSpecGroup> groups = RowSpecGrouper.createGroups(spec);

        assertEquals(1, groups.size());
    }

    @Test
    void createGroups_withThreeRelatedFieldsWithACircularLink_givesOneGroup() {
        Field first = createField("first");
        Field second = createField("second");
        Field third = createField("third");
        ProfileFields fields = new ProfileFields(Arrays.asList(first, second, third));

        Map<Field, FieldSpec> fieldSpecMap = fieldSpecMapOf(first, second, third);

        List<FieldSpecRelations> relations = Arrays.asList(
            link(first, second),
            link(second, third),
            link(first, third));

        RowSpec spec = new RowSpec(fields, fieldSpecMap, relations);

        Set<FieldSpecGroup> groups = RowSpecGrouper.createGroups(spec);

        assertEquals(1, groups.size());
    }

    @Test
    void createGroups_withFiveFields_correctlyGroups() {
        Field first = createField("first");
        Field second = createField("second");
        Field third = createField("third");
        Field fourth = createField("fourth");
        Field fifth = createField("fifth");

        ProfileFields fields = new ProfileFields(Arrays.asList(first, second, third, fourth, fifth));

        Map<Field, FieldSpec> fieldSpecMap = fieldSpecMapOf(first, second, third, fourth, fifth);

        List<FieldSpecRelations> relations = Arrays.asList(
            link(first, second),
            link(first, third),
            link(second, fifth));

        RowSpec spec = new RowSpec(fields, fieldSpecMap, relations);

        Set<FieldSpecGroup> groups = RowSpecGrouper.createGroups(spec);

        assertEquals(2, groups.size());
    }

    @Test
    void createGroups_withMultipleLinksBetweenTwoFields_givesOneGroup() {
        Field first = createField("first");
        Field second = createField("second");

        ProfileFields fields = new ProfileFields(Arrays.asList(first, second));

        Map<Field, FieldSpec> fieldSpecMap = fieldSpecMapOf(first, second);

        List<FieldSpecRelations> relations = Arrays.asList(
            link(first, second),
            link(first, second));

        RowSpec spec = new RowSpec(fields, fieldSpecMap, relations);

        Set<FieldSpecGroup> groups = RowSpecGrouper.createGroups(spec);

        assertEquals(1, groups.size());
    }

    private static FieldSpecRelations link(Field main, Field other) {
        FieldSpecRelations relation = mock(FieldSpecRelations.class);
        when(relation.main()).thenReturn(main);
        when(relation.other()).thenReturn(other);
        return relation;
    }

    private static Map<Field, FieldSpec> fieldSpecMapOf(Field... fields) {
        return Arrays.stream(fields).collect(Collectors.toMap(Function.identity(), x -> FieldSpecFactory.fromType(x.getType())));
    }
}
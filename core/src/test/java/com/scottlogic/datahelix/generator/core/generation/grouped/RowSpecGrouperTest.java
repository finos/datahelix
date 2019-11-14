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
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpec;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpecFactory;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpecGroup;
import com.scottlogic.datahelix.generator.core.fieldspecs.RowSpec;
import com.scottlogic.datahelix.generator.core.fieldspecs.relations.FieldSpecRelation;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static com.scottlogic.datahelix.generator.common.profile.FieldBuilder.createField;

class RowSpecGrouperTest {

    @Test
    void createGroups_withTwoRelatedFields_givesOneGroupOfSizeOne() {
        Field first = createField("first");
        Field second = createField("second");
        Fields fields = new Fields(Arrays.asList(first, second));

        Map<Field, FieldSpec> fieldSpecMap = fieldSpecMapOf(first, second);

        FieldSpecRelation relation = link(first, second);
        List<FieldSpecRelation> relations = Collections.singletonList(relation);

        RowSpec spec = new RowSpec(fields, fieldSpecMap, relations);

        Set<FieldSpecGroup> groups = RowSpecGrouper.createGroups(spec);

        assertEquals(1, groups.size());
    }

    @Test
    void createGroups_withTwoAndOneFields_givesTwoGroups() {
        Field first = createField("first");
        Field second = createField("second");
        Field third = createField("third");
        Fields fields = new Fields(Arrays.asList(first, second, third));

        Map<Field, FieldSpec> fieldSpecMap = fieldSpecMapOf(first, second, third);

        FieldSpecRelation relation = link(first, second);
        List<FieldSpecRelation> relations = Collections.singletonList(relation);

        RowSpec spec = new RowSpec(fields, fieldSpecMap, relations);

        Set<FieldSpecGroup> groups = RowSpecGrouper.createGroups(spec);

        assertEquals(2, groups.size());
    }

    @Test
    void createGroups_withThreeIndependentFields_givesThreeGroups() {
        Field first = createField("first");
        Field second = createField("second");
        Field third = createField("third");
        Fields fields = new Fields(Arrays.asList(first, second, third));

        Map<Field, FieldSpec> fieldSpecMap = fieldSpecMapOf(first, second, third);

        List<FieldSpecRelation> relations = Collections.emptyList();

        RowSpec spec = new RowSpec(fields, fieldSpecMap, relations);

        Set<FieldSpecGroup> groups = RowSpecGrouper.createGroups(spec);

        assertEquals(3, groups.size());
    }

    @Test
    void createGroups_withThreeCodependentFields_givesOneGroup() {
        Field first = createField("first");
        Field second = createField("second");
        Field third = createField("third");
        Fields fields = new Fields(Arrays.asList(first, second, third));

        Map<Field, FieldSpec> fieldSpecMap = fieldSpecMapOf(first, second, third);

        List<FieldSpecRelation> relations = Arrays.asList(link(first, second), link(second, third));

        RowSpec spec = new RowSpec(fields, fieldSpecMap, relations);

        Set<FieldSpecGroup> groups = RowSpecGrouper.createGroups(spec);

        assertEquals(1, groups.size());
    }

    @Test
    void createGroups_withThreeRelatedFieldsWithACircularLink_givesOneGroup() {
        Field first = createField("first");
        Field second = createField("second");
        Field third = createField("third");
        Fields fields = new Fields(Arrays.asList(first, second, third));

        Map<Field, FieldSpec> fieldSpecMap = fieldSpecMapOf(first, second, third);

        List<FieldSpecRelation> relations = Arrays.asList(
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

        Fields fields = new Fields(Arrays.asList(first, second, third, fourth, fifth));

        Map<Field, FieldSpec> fieldSpecMap = fieldSpecMapOf(first, second, third, fourth, fifth);

        List<FieldSpecRelation> relations = Arrays.asList(
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

        Fields fields = new Fields(Arrays.asList(first, second));

        Map<Field, FieldSpec> fieldSpecMap = fieldSpecMapOf(first, second);

        List<FieldSpecRelation> relations = Arrays.asList(
            link(first, second),
            link(first, second));

        RowSpec spec = new RowSpec(fields, fieldSpecMap, relations);

        Set<FieldSpecGroup> groups = RowSpecGrouper.createGroups(spec);

        assertEquals(1, groups.size());
    }

    private static FieldSpecRelation link(Field main, Field other) {
        FieldSpecRelation relation = mock(FieldSpecRelation.class);
        when(relation.main()).thenReturn(main);
        when(relation.other()).thenReturn(other);
        return relation;
    }

    private static Map<Field, FieldSpec> fieldSpecMapOf(Field... fields) {
        return Arrays.stream(fields).collect(Collectors.toMap(Function.identity(), x -> FieldSpecFactory.fromType(x.getType())));
    }
}
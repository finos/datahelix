package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;
import com.scottlogic.deg.generator.generation.databags.FieldGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RowSpecGrouperTest {

    @Test
    void createGroups() {
        Field first = new Field("first");
        Field second = new Field("second");
        ProfileFields fields = new ProfileFields(Arrays.asList(first, second));

        Map<Field, FieldSpec> fieldSpecMap = new HashMap<>();
        fieldSpecMap.put(first, FieldSpec.Empty);
        fieldSpecMap.put(second, FieldSpec.Empty);

        FieldSpecRelations relation = mock(FieldSpecRelations.class);
        when(relation.main()).thenReturn(first);
        when(relation.other()).thenReturn(second);
        List<FieldSpecRelations> relations = Arrays.asList(relation);

        RowSpec spec = new RowSpec(fields, fieldSpecMap, relations);

        Set<FieldGroup> groups = RowSpecGrouper.createGroups(spec);

        assertEquals(1, groups.size());
    }
}
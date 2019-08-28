package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecGroup;
import com.scottlogic.deg.generator.fieldspecs.relations.EqualToDateRelation;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagStream;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

class FieldSpecGroupValueGeneratorTest {

    @Test
    public void generate_withGroupOfSingleField_returnsCorrectStream() {
        Map<Field, FieldSpec> specMap = new HashMap<>();
        FieldSpec firstSpec = FieldSpec.Empty;
        Field firstField = new Field("first", false);
        specMap.put(firstField, FieldSpec.Empty);

        FieldSpecValueGenerator underlyingGenerator = mock(FieldSpecValueGenerator.class);
        String result = "result";
        DataBagValue firstValue = new DataBagValue(result);
        when(underlyingGenerator.generate(any(Field.class), eq(firstSpec))).thenReturn(Stream.of(firstValue));

        FieldSpecGroupValueGenerator generator = new FieldSpecGroupValueGenerator(underlyingGenerator);

        FieldSpecGroup group = new FieldSpecGroup(specMap, Collections.emptyList());

        DataBagStream stream = generator.generate(group);

        Map<Field, DataBagValue> dataBag = new HashMap<>();
        dataBag.put(firstField, firstValue);
        assertEquals(Collections.singleton(new DataBag(dataBag)), stream.stream().collect(Collectors.toSet()));
    }

}
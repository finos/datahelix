package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecGroup;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FieldSpecGroupValueGeneratorTest {

    @Test
    public void generate_withGroupOfSingleField_returnsCorrectStream() {
        Map<Field, FieldSpec> specMap = new HashMap<>();
        FieldSpec firstSpec = FieldSpec.Empty;
        Field firstField = new Field("first");
        specMap.put(firstField, FieldSpec.Empty);

        FieldSpecValueGenerator underlyingGenerator = mock(FieldSpecValueGenerator.class);
        String result = "result";
        DataBagValue firstValue = new DataBagValue(result);
        when(underlyingGenerator.generate(firstSpec)).thenReturn(Stream.of(firstValue));

        FieldSpecGroupValueGenerator generator = new FieldSpecGroupValueGenerator(underlyingGenerator);

        FieldSpecGroup group = new FieldSpecGroup(specMap, Collections.emptyList());

        Stream<DataBag> stream = generator.generate(group);

        Map<Field, DataBagValue> dataBag = new HashMap<>();
        dataBag.put(firstField, firstValue);
        assertEquals(Collections.singleton(new DataBag(dataBag)), stream.collect(Collectors.toSet()));
    }

    @Test
    public void generate_withGroupOfTwoFields_returnsCorrectStream() {
        Map<Field, FieldSpec> specMap = new HashMap<>();
        FieldSpec firstSpec = FieldSpec.Empty;
        FieldSpec secondSpec = FieldSpec.Empty;

        Field firstField = new Field("first");
        Field secondField = new Field("second");

        specMap.put(firstField, firstSpec);
        specMap.put(secondField, secondSpec);

        FieldSpecValueGenerator underlyingGenerator = mock(FieldSpecValueGenerator.class);
        when(underlyingGenerator.isRandom()).thenReturn(true);
        String firstResult = "resultFirst";
        DataBagValue firstValue = new DataBagValue(firstResult, "");
        when(underlyingGenerator.generate(firstSpec)).thenReturn(Stream.of(firstValue));

        FieldSpec specificSpec = FieldSpec.Empty.withWhitelist(FrequencyDistributedSet.singleton("second"));
        FieldSpecRelations relation = mock(FieldSpecRelations.class);
        when(relation.main()).thenReturn(firstField);
        when(relation.other()).thenReturn(secondField);
        when(relation.inverse()).thenReturn(relation);
        when(relation.reduceToRelatedFieldSpec(firstSpec)).thenReturn(specificSpec);
        when(underlyingGenerator.generate(specificSpec)).thenReturn(Stream.of(firstValue));

        String secondResult1 = "resultSecond";
        DataBagValue secondValue1 = new DataBagValue(secondResult1, "");

        when(underlyingGenerator.generateOne(secondSpec)).thenReturn(secondValue1);

        FieldSpecGroup group = new FieldSpecGroup(specMap, Collections.singleton(relation));

        FieldSpecGroupValueGenerator generator = new FieldSpecGroupValueGenerator(underlyingGenerator);
        Stream<DataBag> stream = generator.generate(group);

        Map<Field, DataBagValue> dataBag = new HashMap<>();
        dataBag.put(firstField, firstValue);
        dataBag.put(secondField, secondValue1);
        assertEquals(Collections.singleton(new DataBag(dataBag)), stream.collect(Collectors.toSet()));

    }

}
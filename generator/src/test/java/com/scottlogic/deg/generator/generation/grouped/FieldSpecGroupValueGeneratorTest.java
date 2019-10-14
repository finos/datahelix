package com.scottlogic.deg.generator.generation.grouped;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.config.detail.CombinationStrategyType;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecGroup;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
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
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

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
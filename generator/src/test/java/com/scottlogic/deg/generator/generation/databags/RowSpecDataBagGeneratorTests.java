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

    private Field field = new Field("Field1");
    private ProfileFields fields = new ProfileFields(Collections.singletonList(field));
    private FieldSpec fieldSpec = FieldSpec.Empty;
    Field field2 = new Field("field2");
    Field field3 = new Field("field3");

    DataBag dataBag = new DataBagBuilder().set(field, "value", null).build();
    DataBag dataBag2 = new DataBagBuilder().set(field2, "value", null).build();
    DataBag dataBag3 = new DataBagBuilder().set(field3, "value", null).build();

    @Test
    void shouldCreateValuesForEachFieldSpecInRowSpec() {
        RowSpecDataBagGenerator factory = new RowSpecDataBagGenerator(mockGeneratorFactory, exhaustiveCombinationStrategy);
        Map<Field, FieldSpec> map = new HashMap<Field, FieldSpec>() {{ put(field, fieldSpec); }};
        RowSpec rowSpec = new RowSpec(fields, map);

        when(mockGeneratorFactory.generate(eq(field), any(FieldSpec.class))).thenReturn(Stream.of(dataBag));

        List<DataBag> actual = factory.createDataBags(rowSpec)
            .collect(Collectors.toList());

        verify(mockGeneratorFactory, times(1)).generate(field, fieldSpec);

        List<DataBag> expected = Arrays.asList(dataBag);

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    void factoryIsCalledForEachField() {
        RowSpecDataBagGenerator factory = new RowSpecDataBagGenerator(mockGeneratorFactory, exhaustiveCombinationStrategy);
        Map<Field, FieldSpec> map = new HashMap<Field, FieldSpec>() {{
            put(field, fieldSpec);
            put(field2, fieldSpec);
            put(field3, fieldSpec); }};
        RowSpec rowSpec = new RowSpec(new ProfileFields(Arrays.asList(field2, field, field3)), map);

        when(mockGeneratorFactory.generate(eq(field), any(FieldSpec.class))).thenReturn(Stream.of(dataBag));
        when(mockGeneratorFactory.generate(eq(field2), any(FieldSpec.class))).thenReturn(Stream.of(dataBag2));
        when(mockGeneratorFactory.generate(eq(field3), any(FieldSpec.class))).thenReturn(Stream.of(dataBag3));

        factory.createDataBags(rowSpec)
            .collect(Collectors.toList());

        verify(mockGeneratorFactory, times(1)).generate(field, fieldSpec);
        verify(mockGeneratorFactory, times(1)).generate(field2, fieldSpec);
        verify(mockGeneratorFactory, times(1)).generate(field3, fieldSpec);
    }

    @Test
    void combinationStrategyIsCalled() {
        RowSpecDataBagGenerator factory = new RowSpecDataBagGenerator(mockGeneratorFactory, mockCombinationStrategy);
        Map<Field, FieldSpec> map = new HashMap<Field, FieldSpec>() {{ put(field, fieldSpec); }};
        RowSpec rowSpec = new RowSpec(fields, map);

        factory.createDataBags(rowSpec);

        verify(mockCombinationStrategy, times(1)).permute(any());
    }
}
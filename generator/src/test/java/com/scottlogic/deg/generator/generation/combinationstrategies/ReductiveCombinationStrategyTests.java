package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.generator.Value;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.generation.databags.GeneratedObject;
import com.scottlogic.deg.generator.generation.databags.GeneratedObjectBuilder;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class ReductiveCombinationStrategyTests {
    final Field first_field = new Field("First Field");
    final Field second_field = new Field("Second Field");

    @Test
    void permute_dataBagSequencesContainsTwoFieldsWithMultipleValues_returnsExpectedValues() {
        List<GeneratedObject> firstFieldGeneratedObjects = new ArrayList<GeneratedObject>() {{
            add(
                GeneratedObjectBuilder.startBuilding().set(
                    first_field,
                    new Value(first_field, 10)
                ).build()
            );
            add(
                GeneratedObjectBuilder.startBuilding().set(
                    first_field,
                    new Value(first_field, 20)
                ).build()
            );
        }};
        List<GeneratedObject> secondFieldGeneratedObjects = new ArrayList<GeneratedObject>() {{
            add(
                GeneratedObjectBuilder.startBuilding().set(
                    second_field,
                    new Value(second_field, "A")
                ).build()
            );
            add(
                GeneratedObjectBuilder.startBuilding().set(
                    second_field,
                    new Value(second_field, "B")
                ).build()
            );
        }};
        ReductiveCombinationStrategy combinationStrategy = new ReductiveCombinationStrategy();

        ArrayList<List<GeneratedObject>> dataBagSequences = new ArrayList<List<GeneratedObject>>() {{
            add(firstFieldGeneratedObjects);
            add(secondFieldGeneratedObjects);
        }};
        final List<GeneratedObject> result = combinationStrategy.permute(dataBagSequences.stream().map(Collection::stream))
            .collect(Collectors.toList());

        List<GeneratedObject> expectedGeneratedObjects = new ArrayList<GeneratedObject>() {{
            add(
                GeneratedObjectBuilder.startBuilding().set(
                    first_field,
                    new Value(first_field, 10)
                ).set(
                    second_field,
                    new Value(second_field, "A")
                ).build()
            );
            add(
                GeneratedObjectBuilder.startBuilding().set(
                    first_field,
                    new Value(first_field, 10)
                ).set(
                    second_field,
                    new Value(second_field, "B")
                ).build()
            );
            add(
                GeneratedObjectBuilder.startBuilding().set(
                    first_field,
                    new Value(first_field, 20)
                ).set(
                    second_field,
                    new Value(second_field, "A")
                ).build()
            );
            add(
                GeneratedObjectBuilder.startBuilding().set(
                    first_field,
                    new Value(first_field, 20)
                ).set(
                    second_field,
                    new Value(second_field, "B")
                ).build()
            );
        }};
        Assert.assertEquals(expectedGeneratedObjects, result);
    }
}

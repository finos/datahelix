package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.DataBagValueSource;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.generation.databags.GeneratedObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class ReductiveCombinationStrategyTests {
    @Test
    void permute_dataBagSequencesContainsTwoFieldsWithMultipleValues_returnsExpectedValues() {
        List<GeneratedObject> firstFieldGeneratedObjects = new ArrayList<GeneratedObject>() {{
            add(
                GeneratedObject.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(10, new DataBagValueSource(null))
                ).build()
            );
            add(
                GeneratedObject.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(20, new DataBagValueSource(null))
                ).build()
            );
        }};
        List<GeneratedObject> secondFieldGeneratedObjects = new ArrayList<GeneratedObject>() {{
            add(
                GeneratedObject.startBuilding().set(
                    new Field("Second Field"),
                    new DataBagValue("A", new DataBagValueSource(null))
                ).build()
            );
            add(
                GeneratedObject.startBuilding().set(
                    new Field("Second Field"),
                    new DataBagValue("B", new DataBagValueSource(null))
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
                GeneratedObject.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(10, new DataBagValueSource(null))
                ).set(
                    new Field("Second Field"),
                    new DataBagValue("A", new DataBagValueSource(null))
                ).build()
            );
            add(
                GeneratedObject.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(10, new DataBagValueSource(null))
                ).set(
                    new Field("Second Field"),
                    new DataBagValue("B", new DataBagValueSource(null))
                ).build()
            );
            add(
                GeneratedObject.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(20, new DataBagValueSource(null))
                ).set(
                    new Field("Second Field"),
                    new DataBagValue("A", new DataBagValueSource(null))
                ).build()
            );
            add(
                GeneratedObject.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(20, new DataBagValueSource(null))
                ).set(
                    new Field("Second Field"),
                    new DataBagValue("B", new DataBagValueSource(null))
                ).build()
            );
        }};
        Assert.assertEquals(expectedGeneratedObjects, result);
    }
}

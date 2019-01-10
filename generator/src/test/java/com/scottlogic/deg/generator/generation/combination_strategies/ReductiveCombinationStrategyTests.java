package com.scottlogic.deg.generator.generation.combination_strategies;

import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.DataBagValueSource;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class ReductiveCombinationStrategyTests {
    @Test
    void permute_dataBagSequencesContainsTwoFieldsWithMultipleValues_returnsExpectedValues() {
        List<DataBag> firstFieldDataBags = new ArrayList<DataBag>() {{
            add(
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(10, new DataBagValueSource(null))
                ).build()
            );
            add(
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(20, new DataBagValueSource(null))
                ).build()
            );
        }};
        List<DataBag> secondFieldDataBags = new ArrayList<DataBag>() {{
            add(
                DataBag.startBuilding().set(
                    new Field("Second Field"),
                    new DataBagValue("A", new DataBagValueSource(null))
                ).build()
            );
            add(
                DataBag.startBuilding().set(
                    new Field("Second Field"),
                    new DataBagValue("B", new DataBagValueSource(null))
                ).build()
            );
        }};
        ReductiveCombinationStrategy combinationStrategy = new ReductiveCombinationStrategy();

        ArrayList<List<DataBag>> dataBagSequences = new ArrayList<List<DataBag>>() {{
            add(firstFieldDataBags);
            add(secondFieldDataBags);
        }};
        final List<DataBag> result = combinationStrategy.permute(dataBagSequences.stream().map(Collection::stream))
            .collect(Collectors.toList());

        List<DataBag> expectedDataBags = new ArrayList<DataBag>() {{
            add(
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(10, new DataBagValueSource(null))
                ).set(
                    new Field("Second Field"),
                    new DataBagValue("A", new DataBagValueSource(null))
                ).build()
            );
            add(
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(10, new DataBagValueSource(null))
                ).set(
                    new Field("Second Field"),
                    new DataBagValue("B", new DataBagValueSource(null))
                ).build()
            );
            add(
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(20, new DataBagValueSource(null))
                ).set(
                    new Field("Second Field"),
                    new DataBagValue("A", new DataBagValueSource(null))
                ).build()
            );
            add(
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(20, new DataBagValueSource(null))
                ).set(
                    new Field("Second Field"),
                    new DataBagValue("B", new DataBagValueSource(null))
                ).build()
            );
        }};
        Assert.assertEquals(expectedDataBags, result);
    }
}

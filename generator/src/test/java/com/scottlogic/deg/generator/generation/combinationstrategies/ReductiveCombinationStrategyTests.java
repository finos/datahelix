package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.generator.builders.DataBagBuilder;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.common.output.DataBagValueSource;
import com.scottlogic.deg.common.profile.Field;
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
                new DataBagBuilder().set(
                    new Field("First Field"),
                    new DataBagValue(10, DataBagValueSource.Empty)
                ).build()
            );
            add(
                new DataBagBuilder().set(
                    new Field("First Field"),
                    new DataBagValue(20, DataBagValueSource.Empty)
                ).build()
            );
        }};
        List<DataBag> secondFieldDataBags = new ArrayList<DataBag>() {{
            add(
                new DataBagBuilder().set(
                    new Field("Second Field"),
                    new DataBagValue("A", DataBagValueSource.Empty)
                ).build()
            );
            add(
                new DataBagBuilder().set(
                    new Field("Second Field"),
                    new DataBagValue("B", DataBagValueSource.Empty)
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
                new DataBagBuilder().set(
                    new Field("First Field"),
                    new DataBagValue(10, DataBagValueSource.Empty)
                ).set(
                    new Field("Second Field"),
                    new DataBagValue("A", DataBagValueSource.Empty)
                ).build()
            );
            add(
                new DataBagBuilder().set(
                    new Field("First Field"),
                    new DataBagValue(10, DataBagValueSource.Empty)
                ).set(
                    new Field("Second Field"),
                    new DataBagValue("B", DataBagValueSource.Empty)
                ).build()
            );
            add(
                new DataBagBuilder().set(
                    new Field("First Field"),
                    new DataBagValue(20, DataBagValueSource.Empty)
                ).set(
                    new Field("Second Field"),
                    new DataBagValue("A", DataBagValueSource.Empty)
                ).build()
            );
            add(
                new DataBagBuilder().set(
                    new Field("First Field"),
                    new DataBagValue(20, DataBagValueSource.Empty)
                ).set(
                    new Field("Second Field"),
                    new DataBagValue("B", DataBagValueSource.Empty)
                ).build()
            );
        }};
        Assert.assertEquals(expectedDataBags, result);
    }
}

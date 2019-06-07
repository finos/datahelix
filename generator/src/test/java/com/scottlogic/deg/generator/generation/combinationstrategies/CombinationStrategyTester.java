package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.builders.DataBagBuilder;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import org.hamcrest.collection.IsArrayContainingInAnyOrder;
import org.junit.Assert;

import java.util.stream.Stream;

class CombinationStrategyTester {
    private CombinationStrategy strategy;
    private Stream<Stream<DataBag>> dataBags;

    CombinationStrategyTester(CombinationStrategy combinationStrategy) {
        strategy = combinationStrategy;
    }

    @SafeVarargs
    final void given(Stream<DataBag>... bagSequences) {
        dataBags = Stream.of(bagSequences);
    }

    void expect(Stream<DataBag> bagSequence) {
        DataBag[] results = strategy.permute(dataBags).toArray(DataBag[]::new);
        DataBag[] bagArray = bagSequence.toArray(DataBag[]::new);

        Assert.assertThat(results, IsArrayContainingInAnyOrder.arrayContainingInAnyOrder(bagArray));
    }

    void expectEmpty() {
        Stream<DataBag> results = strategy.permute(dataBags);

        Assert.assertFalse(results.iterator().hasNext());
    }

    static DataBag bag(String... fieldNames) {
        DataBagBuilder builder = new DataBagBuilder();

        for (String fieldName : fieldNames) {
            builder.set(new Field(fieldName), "whatever");
        }

        return builder.build();
    }
}

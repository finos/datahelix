package com.scottlogic.deg.generator.generation.combination_strategies;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

class CombinationStrategyTester {
    private ICombinationStrategy strategy;
    private List<Iterable<DataBag>> dataBags;

    CombinationStrategyTester(ICombinationStrategy combinationStrategy) {
        strategy = combinationStrategy;
    }

    @SafeVarargs
    final void given(Iterable<DataBag>... bagSequences) {
        dataBags = Arrays.asList(bagSequences);
    }

    void expect(Iterable<DataBag> bagSequence) {
        Iterable<DataBag> results = strategy.permute(dataBags.stream());

        DataBag[] bagArray = StreamSupport.stream(bagSequence.spliterator(), false).toArray(DataBag[]::new);

        Assert.assertThat(results, IsIterableContainingInAnyOrder.containsInAnyOrder(bagArray));
    }

    void expectEmpty() {
        Iterable<DataBag> results = strategy.permute(dataBags.stream());

        Assert.assertFalse(results.iterator().hasNext());
    }

    static DataBag bag(String... fieldNames) {
        DataBag.DataBagBuilder builder = DataBag.startBuilding();

        for (String fieldName : fieldNames) {
            builder.set(new Field(fieldName), "whatever");
        }

        return builder.build();
    }

    static Iterable<DataBag> bagSequence(DataBag... bags) {
        return Arrays.asList(bags);
    }
}

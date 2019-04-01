package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.generator.DataBagValueSource;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.generation.databags.GeneratedObject;
import org.hamcrest.collection.IsArrayContainingInAnyOrder;
import org.junit.Assert;

import java.util.stream.Stream;

class CombinationStrategyTester {
    private CombinationStrategy strategy;
    private Stream<Stream<GeneratedObject>> dataBags;

    CombinationStrategyTester(CombinationStrategy combinationStrategy) {
        strategy = combinationStrategy;
    }

    @SafeVarargs
    final void given(Stream<GeneratedObject>... bagSequences) {
        dataBags = Stream.of(bagSequences);
    }

    void expect(Stream<GeneratedObject> bagSequence) {
        GeneratedObject[] results = strategy.permute(dataBags).toArray(GeneratedObject[]::new);
        GeneratedObject[] bagArray = bagSequence.toArray(GeneratedObject[]::new);

        Assert.assertThat(results, IsArrayContainingInAnyOrder.arrayContainingInAnyOrder(bagArray));
    }

    void expectEmpty() {
        Stream<GeneratedObject> results = strategy.permute(dataBags);

        Assert.assertFalse(results.iterator().hasNext());
    }

    static GeneratedObject bag(String... fieldNames) {
        GeneratedObject.DataBagBuilder builder = GeneratedObject.startBuilding();

        for (String fieldName : fieldNames) {
            builder.set(new Field(fieldName), "whatever", DataBagValueSource.Empty);
        }

        return builder.build();
    }
}

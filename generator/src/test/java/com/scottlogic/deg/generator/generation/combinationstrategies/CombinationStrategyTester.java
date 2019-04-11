package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;
import com.scottlogic.deg.generator.generation.rows.Row;
import com.scottlogic.deg.generator.generation.rows.RowBuilder;
import org.hamcrest.collection.IsArrayContainingInAnyOrder;
import org.junit.Assert;

import java.util.stream.Stream;

class CombinationStrategyTester {
    private CombinationStrategy strategy;
    private Stream<Stream<Row>> rows;

    CombinationStrategyTester(CombinationStrategy combinationStrategy) {
        strategy = combinationStrategy;
    }

    @SafeVarargs
    final void given(Stream<Row>... bagSequences) {
        rows = Stream.of(bagSequences);
    }

    void expect(Stream<Row> bagSequence) {
        Row[] results = strategy.permute(rows).toArray(Row[]::new);
        Row[] bagArray = bagSequence.toArray(Row[]::new);

        Assert.assertThat(results, IsArrayContainingInAnyOrder.arrayContainingInAnyOrder(bagArray));
    }

    void expectEmpty() {
        Stream<Row> results = strategy.permute(rows);

        Assert.assertFalse(results.iterator().hasNext());
    }

    static Row bag(String... fieldNames) {
        RowBuilder builder = RowBuilder.startBuilding();

        for (String fieldName : fieldNames) {
            builder.set(new Field(fieldName), "whatever", FieldSpecSource.Empty);
        }

        return builder.build();
    }
}

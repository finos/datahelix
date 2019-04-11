package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.generator.generation.rows.Value;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.generation.rows.Row;
import com.scottlogic.deg.generator.generation.rows.RowBuilder;
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
    void permute_rowSequencesContainsTwoFieldsWithMultipleValues_returnsExpectedValues() {
        List<Row> firstFieldRows = new ArrayList<Row>() {{
            add(
                RowBuilder.startBuilding().set(
                    first_field,
                    new Value(first_field, 10)
                ).build()
            );
            add(
                RowBuilder.startBuilding().set(
                    first_field,
                    new Value(first_field, 20)
                ).build()
            );
        }};
        List<Row> secondFieldRows = new ArrayList<Row>() {{
            add(
                RowBuilder.startBuilding().set(
                    second_field,
                    new Value(second_field, "A")
                ).build()
            );
            add(
                RowBuilder.startBuilding().set(
                    second_field,
                    new Value(second_field, "B")
                ).build()
            );
        }};
        ReductiveCombinationStrategy combinationStrategy = new ReductiveCombinationStrategy();

        ArrayList<List<Row>> rowSequences = new ArrayList<List<Row>>() {{
            add(firstFieldRows);
            add(secondFieldRows);
        }};
        final List<Row> result = combinationStrategy.permute(rowSequences.stream().map(Collection::stream))
            .collect(Collectors.toList());

        List<Row> expectedRows = new ArrayList<Row>() {{
            add(
                RowBuilder.startBuilding().set(
                    first_field,
                    new Value(first_field, 10)
                ).set(
                    second_field,
                    new Value(second_field, "A")
                ).build()
            );
            add(
                RowBuilder.startBuilding().set(
                    first_field,
                    new Value(first_field, 10)
                ).set(
                    second_field,
                    new Value(second_field, "B")
                ).build()
            );
            add(
                RowBuilder.startBuilding().set(
                    first_field,
                    new Value(first_field, 20)
                ).set(
                    second_field,
                    new Value(second_field, "A")
                ).build()
            );
            add(
                RowBuilder.startBuilding().set(
                    first_field,
                    new Value(first_field, 20)
                ).set(
                    second_field,
                    new Value(second_field, "B")
                ).build()
            );
        }};
        Assert.assertEquals(expectedRows, result);
    }
}

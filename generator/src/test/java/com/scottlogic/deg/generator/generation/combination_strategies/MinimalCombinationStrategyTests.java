package com.scottlogic.deg.generator.generation.combination_strategies;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

class MinimalCombinationStrategyTests {
    @Test
    void shouldCombineMinimally() {
        given(
            bagSequence(bag("A"), bag("B"), bag("C")),
            bagSequence(bag("1"), bag("2"), bag("3")));

        expect(
            bagSequence(bag("A","1"), bag("B","2"), bag("C","3")));
    }

    @Test
    void shouldCombineSequencesOfDifferentLengths() {
        given(
            bagSequence(bag("X")),
            bagSequence(bag("A"), bag("B"), bag("C")),
            bagSequence(bag("1"), bag("2"), bag("3"), bag("4"), bag("5")));

        expect(
            bagSequence(
                bag("X", "A", "1"),
                bag("X", "B", "2"),
                bag("X", "C", "3"),
                bag("X", "C", "4"),
                bag("X", "C", "5")));
    }

    @Test
    void shouldGiveNoResultsForSingleEmptySequence() {
        given(
            bagSequence(bag("A"), bag("B"), bag("C")),
            bagSequence());

        expectEmpty();
    }

    private DataBag bag(String... fieldNames) {
        DataBag.DataBagBuilder builder = DataBag.startBuilding();

        for (String fieldName : fieldNames) {
            builder.set(new Field(fieldName), "whatever");
        }

        return builder.build();
    }

    private Iterable<DataBag> bagSequence(DataBag... bags) {
        return Arrays.asList(bags);
    }

    private List<Iterable<DataBag>> dataBags;

    private void given(Iterable<DataBag>... bagSequences) {
        dataBags = Arrays.asList(bagSequences);
    }

    private void expect(Iterable<DataBag> bagSequence) {
        ICombinationStrategy combinationStrategy = new MinimalCombinationStrategy();

        Iterable<DataBag> results = combinationStrategy.permute(dataBags);

        DataBag[] bagArray = StreamSupport.stream(bagSequence.spliterator(), false).toArray(DataBag[]::new);

        Assert.assertThat(results, IsIterableContainingInOrder.contains(bagArray));
    }

    private void expectEmpty() {
        ICombinationStrategy combinationStrategy = new MinimalCombinationStrategy();

        Iterable<DataBag> results = combinationStrategy.permute(dataBags);

        Assert.assertFalse(results.iterator().hasNext());
    }
}

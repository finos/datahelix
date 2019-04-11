package com.scottlogic.deg.generator.generation.rows;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.TestGenerationConfigSource;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

class ConcatenatingRowSourceTests {
    private static final GenerationConfig arbitraryGenerationConfig = new GenerationConfig(
        new TestGenerationConfigSource(
            GenerationConfig.DataGenerationType.INTERESTING,
            GenerationConfig.TreeWalkerType.CARTESIAN_PRODUCT,
            GenerationConfig.CombinationStrategyType.PINNING)
    );

    @Test
    void whenMultiplePopulatedSourcesAreProvided() {
        // ARRANGE
        Row row1 = RowBuilder.startBuilding().build();
        Row row2 = RowBuilder.startBuilding().build();
        Row row3 = RowBuilder.startBuilding().build();

        RowSource rowSource1 = new DummyRowSource(row1, row2);
        RowSource rowSource2 = new DummyRowSource(row3);

        ConcatenatingRowSource objectUnderTest =
            new ConcatenatingRowSource(
                Stream.of(
                    rowSource1,
                    rowSource2));

        // ACT
        List<Row> output = objectUnderTest.generate(arbitraryGenerationConfig).collect(Collectors.toList());

        // ASSERT
        Assert.assertThat(
            output,
            contains(
                Arrays.asList(
                    sameInstance(row1),
                    sameInstance(row2),
                    sameInstance(row3)
                )));
    }

    @Test
    void whenOnePopulatedSourceIsProvided() {
        // ARRANGE
        Row row1 = RowBuilder.startBuilding().build();

        RowSource rowSource1 = new DummyRowSource(row1);

        ConcatenatingRowSource objectUnderTest =
            new ConcatenatingRowSource(
                Stream.of(rowSource1));

        // ACT
        List<Row> output = objectUnderTest.generate(arbitraryGenerationConfig).collect(Collectors.toList());

        // ASSERT
        Assert.assertThat(
            output,
            contains(
                Arrays.asList(
                    sameInstance(row1)
                )));
    }

    @Test
    void whenMiddleSourceIsEmpty() {
        // ARRANGE
        Row row1 = RowBuilder.startBuilding().build();
        Row row2 = RowBuilder.startBuilding().build();
        Row row3 = RowBuilder.startBuilding().build();

        RowSource rowSource1 = new DummyRowSource(row1, row2);
        RowSource rowSource2 = new DummyRowSource();
        RowSource rowSource3 = new DummyRowSource(row3);

        ConcatenatingRowSource objectUnderTest =
            new ConcatenatingRowSource(
                Stream.of(
                    rowSource1,
                    rowSource2,
                    rowSource3));

        // ACT
        List<Row> output = objectUnderTest.generate(arbitraryGenerationConfig).collect(Collectors.toList());

        // ASSERT
        Assert.assertThat(
            output,
            contains(
                Arrays.asList(
                    sameInstance(row1),
                    sameInstance(row2),
                    sameInstance(row3)
                )));
    }

    @Test
    void whenAllSourcesAreEmpty() {
        // ARRANGE
        RowSource rowSource1 = new DummyRowSource();
        RowSource rowSource2 = new DummyRowSource();

        ConcatenatingRowSource objectUnderTest =
            new ConcatenatingRowSource(
                Stream.of(
                    rowSource1,
                    rowSource2));

        // ACT
        List<Row> output = objectUnderTest.generate(arbitraryGenerationConfig).collect(Collectors.toList());

        // ASSERT
        Assert.assertThat(output, empty());
    }
}

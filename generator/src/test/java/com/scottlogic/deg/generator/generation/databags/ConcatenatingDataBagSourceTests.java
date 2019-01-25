package com.scottlogic.deg.generator.generation.databags;

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

class ConcatenatingDataBagSourceTests {
    private static final GenerationConfig arbitraryGenerationConfig = new GenerationConfig(
        new TestGenerationConfigSource(
            GenerationConfig.DataGenerationType.INTERESTING,
            GenerationConfig.TreeWalkerType.CARTESIAN_PRODUCT,
            GenerationConfig.CombinationStrategyType.PINNING,
            GenerationConfig.MonitorType.NOOP)
    );

    @Test
    void whenMultiplePopulatedSourcesAreProvided() {
        // ARRANGE
        DataBag dataBag1 = DataBag.startBuilding().build();
        DataBag dataBag2 = DataBag.startBuilding().build();
        DataBag dataBag3 = DataBag.startBuilding().build();

        DataBagSource dataBagSource1 = new DummyDataBagSource(dataBag1, dataBag2);
        DataBagSource dataBagSource2 = new DummyDataBagSource(dataBag3);

        ConcatenatingDataBagSource objectUnderTest =
            new ConcatenatingDataBagSource(
                Stream.of(
                    dataBagSource1,
                    dataBagSource2));

        // ACT
        List<DataBag> output = objectUnderTest.generate(arbitraryGenerationConfig).collect(Collectors.toList());

        // ASSERT
        Assert.assertThat(
            output,
            contains(
                Arrays.asList(
                    sameInstance(dataBag1),
                    sameInstance(dataBag2),
                    sameInstance(dataBag3)
                )));
    }

    @Test
    void whenOnePopulatedSourceIsProvided() {
        // ARRANGE
        DataBag dataBag1 = DataBag.startBuilding().build();

        DataBagSource dataBagSource1 = new DummyDataBagSource(dataBag1);

        ConcatenatingDataBagSource objectUnderTest =
            new ConcatenatingDataBagSource(
                Stream.of(dataBagSource1));

        // ACT
        List<DataBag> output = objectUnderTest.generate(arbitraryGenerationConfig).collect(Collectors.toList());

        // ASSERT
        Assert.assertThat(
            output,
            contains(
                Arrays.asList(
                    sameInstance(dataBag1)
                )));
    }

    @Test
    void whenMiddleSourceIsEmpty() {
        // ARRANGE
        DataBag dataBag1 = DataBag.startBuilding().build();
        DataBag dataBag2 = DataBag.startBuilding().build();
        DataBag dataBag3 = DataBag.startBuilding().build();

        DataBagSource dataBagSource1 = new DummyDataBagSource(dataBag1, dataBag2);
        DataBagSource dataBagSource2 = new DummyDataBagSource();
        DataBagSource dataBagSource3 = new DummyDataBagSource(dataBag3);

        ConcatenatingDataBagSource objectUnderTest =
            new ConcatenatingDataBagSource(
                Stream.of(
                    dataBagSource1,
                    dataBagSource2,
                    dataBagSource3));

        // ACT
        List<DataBag> output = objectUnderTest.generate(arbitraryGenerationConfig).collect(Collectors.toList());

        // ASSERT
        Assert.assertThat(
            output,
            contains(
                Arrays.asList(
                    sameInstance(dataBag1),
                    sameInstance(dataBag2),
                    sameInstance(dataBag3)
                )));
    }

    @Test
    void whenAllSourcesAreEmpty() {
        // ARRANGE
        DataBagSource dataBagSource1 = new DummyDataBagSource();
        DataBagSource dataBagSource2 = new DummyDataBagSource();

        ConcatenatingDataBagSource objectUnderTest =
            new ConcatenatingDataBagSource(
                Stream.of(
                    dataBagSource1,
                    dataBagSource2));

        // ACT
        List<DataBag> output = objectUnderTest.generate(arbitraryGenerationConfig).collect(Collectors.toList());

        // ASSERT
        Assert.assertThat(output, empty());
    }
}

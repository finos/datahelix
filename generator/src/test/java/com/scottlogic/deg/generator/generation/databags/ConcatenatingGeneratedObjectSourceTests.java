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

class ConcatenatingGeneratedObjectSourceTests {
    private static final GenerationConfig arbitraryGenerationConfig = new GenerationConfig(
        new TestGenerationConfigSource(
            GenerationConfig.DataGenerationType.INTERESTING,
            GenerationConfig.TreeWalkerType.CARTESIAN_PRODUCT,
            GenerationConfig.CombinationStrategyType.PINNING)
    );

    @Test
    void whenMultiplePopulatedSourcesAreProvided() {
        // ARRANGE
        GeneratedObject generatedObject1 = GeneratedObject.startBuilding().build();
        GeneratedObject generatedObject2 = GeneratedObject.startBuilding().build();
        GeneratedObject generatedObject3 = GeneratedObject.startBuilding().build();

        DataBagSource dataBagSource1 = new DummyDataBagSource(generatedObject1, generatedObject2);
        DataBagSource dataBagSource2 = new DummyDataBagSource(generatedObject3);

        ConcatenatingDataBagSource objectUnderTest =
            new ConcatenatingDataBagSource(
                Stream.of(
                    dataBagSource1,
                    dataBagSource2));

        // ACT
        List<GeneratedObject> output = objectUnderTest.generate(arbitraryGenerationConfig).collect(Collectors.toList());

        // ASSERT
        Assert.assertThat(
            output,
            contains(
                Arrays.asList(
                    sameInstance(generatedObject1),
                    sameInstance(generatedObject2),
                    sameInstance(generatedObject3)
                )));
    }

    @Test
    void whenOnePopulatedSourceIsProvided() {
        // ARRANGE
        GeneratedObject generatedObject1 = GeneratedObject.startBuilding().build();

        DataBagSource dataBagSource1 = new DummyDataBagSource(generatedObject1);

        ConcatenatingDataBagSource objectUnderTest =
            new ConcatenatingDataBagSource(
                Stream.of(dataBagSource1));

        // ACT
        List<GeneratedObject> output = objectUnderTest.generate(arbitraryGenerationConfig).collect(Collectors.toList());

        // ASSERT
        Assert.assertThat(
            output,
            contains(
                Arrays.asList(
                    sameInstance(generatedObject1)
                )));
    }

    @Test
    void whenMiddleSourceIsEmpty() {
        // ARRANGE
        GeneratedObject generatedObject1 = GeneratedObject.startBuilding().build();
        GeneratedObject generatedObject2 = GeneratedObject.startBuilding().build();
        GeneratedObject generatedObject3 = GeneratedObject.startBuilding().build();

        DataBagSource dataBagSource1 = new DummyDataBagSource(generatedObject1, generatedObject2);
        DataBagSource dataBagSource2 = new DummyDataBagSource();
        DataBagSource dataBagSource3 = new DummyDataBagSource(generatedObject3);

        ConcatenatingDataBagSource objectUnderTest =
            new ConcatenatingDataBagSource(
                Stream.of(
                    dataBagSource1,
                    dataBagSource2,
                    dataBagSource3));

        // ACT
        List<GeneratedObject> output = objectUnderTest.generate(arbitraryGenerationConfig).collect(Collectors.toList());

        // ASSERT
        Assert.assertThat(
            output,
            contains(
                Arrays.asList(
                    sameInstance(generatedObject1),
                    sameInstance(generatedObject2),
                    sameInstance(generatedObject3)
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
        List<GeneratedObject> output = objectUnderTest.generate(arbitraryGenerationConfig).collect(Collectors.toList());

        // ASSERT
        Assert.assertThat(output, empty());
    }
}

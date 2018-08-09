package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

class ConcatenatingDataBagSourceTests {
    @Test
    void whenMultiplePopulatedSourcesAreProvided() {
        // ARRANGE
        DataBag dataBag1 = DataBag.startBuilding().build();
        DataBag dataBag2 = DataBag.startBuilding().build();
        DataBag dataBag3 = DataBag.startBuilding().build();

        IDataBagSource dataBagSource1 = new DummyDataBagSource(dataBag1, dataBag2);
        IDataBagSource dataBagSource2 = new DummyDataBagSource(dataBag3);

        ConcatenatingDataBagSource objectUnderTest =
            new ConcatenatingDataBagSource(
                Arrays.asList(
                    dataBagSource1,
                    dataBagSource2));

        // ACT
        List<DataBag> output = new ArrayList<>();
        for (DataBag db : objectUnderTest.generate(GenerationConfig.exhaustivePresets))
            output.add(db);

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

        IDataBagSource dataBagSource1 = new DummyDataBagSource(dataBag1);

        ConcatenatingDataBagSource objectUnderTest =
            new ConcatenatingDataBagSource(
                Arrays.asList(
                    dataBagSource1));

        // ACT
        List<DataBag> output = new ArrayList<>();
        for (DataBag db : objectUnderTest.generate(GenerationConfig.exhaustivePresets))
            output.add(db);

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

        IDataBagSource dataBagSource1 = new DummyDataBagSource(dataBag1, dataBag2);
        IDataBagSource dataBagSource2 = new DummyDataBagSource();
        IDataBagSource dataBagSource3 = new DummyDataBagSource(dataBag3);

        ConcatenatingDataBagSource objectUnderTest =
            new ConcatenatingDataBagSource(
                Arrays.asList(
                    dataBagSource1,
                    dataBagSource2,
                    dataBagSource3));

        // ACT
        List<DataBag> output = new ArrayList<>();
        for (DataBag db : objectUnderTest.generate(GenerationConfig.exhaustivePresets))
            output.add(db);

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
        IDataBagSource dataBagSource1 = new DummyDataBagSource();
        IDataBagSource dataBagSource2 = new DummyDataBagSource();

        ConcatenatingDataBagSource objectUnderTest =
            new ConcatenatingDataBagSource(
                Arrays.asList(
                    dataBagSource1,
                    dataBagSource2));

        // ACT
        List<DataBag> output = new ArrayList<>();
        for (DataBag db : objectUnderTest.generate(GenerationConfig.exhaustivePresets))
            output.add(db);

        // ASSERT
        Assert.assertThat(output, empty());
    }
}

package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.Field;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

class DataBagTests {
    @Test
    void getShouldReturnSettedValue() {
        // ARRANGE
        Field idField = new Field("id");

        DataBag objectUnderTest = new DataBag();

        // ACT
        objectUnderTest.set(idField, 3);

        // ASSERT
        Assert.assertThat(
            objectUnderTest.get(idField),
            equalTo(3));
    }

    @Test
    void setShouldThrowExceptionIfAlreadyHasValueForField() {
        // ARRANGE
        Field idField = new Field("id");

        DataBag objectUnderTest = new DataBag();

        objectUnderTest.set(idField, 3);

        // ACT / ASSERT
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> objectUnderTest.set(idField, 4));
    }

    @Test
    void getShouldThrowIfFieldNotSpecified() {
        // ARRANGE
        Field idField = new Field("id");

        DataBag objectUnderTest = new DataBag();

        // ACT / ASSERT
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> objectUnderTest.get(idField));
    }

    @Test
    void mergedDataBagsShouldContainTheSameValuesAsInputs() {
        // ARRANGE
        Field idField = new Field("id");
        Field priceField = new Field("price");

        DataBag dataBag1 = new DataBag();
        dataBag1.set(idField, 3);

        DataBag dataBag2 = new DataBag();
        dataBag2.set(priceField, 4);

        // ACT
        DataBag mergedDataBag = DataBag.merge(dataBag1, dataBag2);

        // ASSERT
        Assert.assertThat(
            mergedDataBag.get(idField),
            equalTo(3));

        Assert.assertThat(
            mergedDataBag.get(priceField),
            equalTo(4));
    }

    @Test
    void mergedShouldThrowIfDataBagsOverlap() {
        // ARRANGE
        Field idField = new Field("id");
        Field priceField = new Field("price");

        DataBag dataBag1 = new DataBag();
        dataBag1.set(idField, "foo");

        DataBag dataBag2 = new DataBag();
        dataBag2.set(idField, "foo");
        dataBag2.set(priceField, 4);

        // ACT / ASSERT
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> DataBag.merge(dataBag1, dataBag2));
    }
}

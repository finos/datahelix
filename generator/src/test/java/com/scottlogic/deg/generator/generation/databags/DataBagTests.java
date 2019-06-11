package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.builders.DataBagBuilder;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

class DataBagTests {
    @Test
    void getShouldReturnSettedValue() {
        // ARRANGE
        Field idField = new Field("id");

        // ACT
        DataBag objectUnderTest = new DataBagBuilder().set(idField, 3).build();

        // ASSERT
        Assert.assertThat(
            objectUnderTest.getFormattedValue(idField),
            equalTo(3));
    }

    @Test
    void setShouldThrowExceptionIfAlreadyHasValueForField() {
        // ARRANGE
        Field idField = new Field("id");

        // ACT / ASSERT
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new DataBagBuilder()
                .set(idField, 3)
                .set(idField, 3)
                .build());
    }

    @Test
    void getShouldThrowIfFieldNotSpecified() {
        // ARRANGE
        Field idField = new Field("id");

        DataBag objectUnderTest = DataBag.empty;

        // ACT / ASSERT
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> objectUnderTest.getFormattedValue(idField));
    }

    @Test
    void mergedDataBagsShouldContainTheSameValuesAsInputs() {
        // ARRANGE
        Field idField = new Field("id");
        Field priceField = new Field("price");

        DataBag dataBag1 = new DataBagBuilder().set(idField, new DataBagValue(3)).build();
        DataBag dataBag2 = new DataBagBuilder().set(priceField, new DataBagValue(4)).build();

        // ACT
        DataBag mergedDataBag = DataBag.merge(dataBag1, dataBag2);

        // ASSERT
        Assert.assertThat(
            mergedDataBag.getFormattedValue(idField),
            equalTo(3));

        Assert.assertThat(
            mergedDataBag.getFormattedValue(priceField),
            equalTo(4));
    }

    @Test
    void mergeShouldThrowIfDataBagsOverlap() {
        // ARRANGE
        Field idField = new Field("id");
        Field priceField = new Field("price");

        DataBag dataBag1 = new DataBagBuilder()
            .set(idField, "foo")
            .build();

        DataBag dataBag2 = new DataBagBuilder()
            .set(idField, "foo")
            .set(priceField, 4)
            .build();

        // ACT / ASSERT
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> DataBag.merge(dataBag1, dataBag2));
    }
}

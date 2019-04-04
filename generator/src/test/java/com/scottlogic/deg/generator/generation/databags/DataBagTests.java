package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.DataBagValueSource;
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

        // ACT
        DataBag objectUnderTest = DataBag.startBuilding().set(idField, 3, DataBagValueSource.Empty).build();

        // ASSERT
        Assert.assertThat(
            objectUnderTest.getValue(idField),
            equalTo(3));
    }

    @Test
    void setShouldThrowExceptionIfAlreadyHasValueForField() {
        // ARRANGE
        Field idField = new Field("id");

        // ACT / ASSERT
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> DataBag.startBuilding()
                .set(idField, 3, DataBagValueSource.Empty)
                .set(idField, 3, DataBagValueSource.Empty)
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
            () -> objectUnderTest.getValueAndFormat(idField));
    }

    @Test
    void mergedDataBagsShouldContainTheSameValuesAsInputs() {
        // ARRANGE
        Field idField = new Field("id");
        Field priceField = new Field("price");

        DataBag dataBag1 = DataBag.startBuilding().set(idField, new DataBagValue(3, DataBagValueSource.Empty)).build();
        DataBag dataBag2 = DataBag.startBuilding().set(priceField, new DataBagValue(4, DataBagValueSource.Empty)).build();

        // ACT
        DataBag mergedDataBag = DataBag.merge(dataBag1, dataBag2);

        // ASSERT
        Assert.assertThat(
            mergedDataBag.getValue(idField),
            equalTo(3));

        Assert.assertThat(
            mergedDataBag.getValue(priceField),
            equalTo(4));
    }

    @Test
    void mergeShouldThrowIfDataBagsOverlap() {
        // ARRANGE
        Field idField = new Field("id");
        Field priceField = new Field("price");

        DataBag dataBag1 = DataBag.startBuilding()
            .set(idField, "foo", DataBagValueSource.Empty)
            .build();

        DataBag dataBag2 = DataBag.startBuilding()
            .set(idField, "foo", DataBagValueSource.Empty)
            .set(priceField, 4, DataBagValueSource.Empty)
            .build();

        // ACT / ASSERT
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> DataBag.merge(dataBag1, dataBag2));
    }
}

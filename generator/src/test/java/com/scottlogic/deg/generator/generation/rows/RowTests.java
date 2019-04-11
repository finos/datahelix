package com.scottlogic.deg.generator.generation.rows;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

class RowTests {
    @Test
    void getShouldReturnSettedValue() {
        // ARRANGE
        Field idField = new Field("id");

        // ACT
        Row objectUnderTest = GeneratedObjectBuilder.startBuilding().set(idField, 3, FieldSpecSource.Empty).build();

        // ASSERT
        Assert.assertThat(
            objectUnderTest
                .getFieldToValue()
                .get(idField)
                .getValue(),
            equalTo(3));
    }

    @Test
    void mergedDataBagsShouldContainTheSameValuesAsInputs() {
        // ARRANGE
        Field idField = new Field("id");
        Field priceField = new Field("price");

        Row row1 = GeneratedObjectBuilder.startBuilding().set(idField, new Value(idField, 3)).build();
        Row row2 = GeneratedObjectBuilder.startBuilding().set(priceField, new Value(priceField, 4)).build();

        // ACT
        Row mergedRow = GeneratedObjectMerger.merge(row1, row2);

        // ASSERT
        Assert.assertThat(
            mergedRow.getFieldToValue().get(idField).getValue(),
            equalTo(3));

        Assert.assertThat(
            mergedRow.getFieldToValue().get(priceField).getValue(),
            equalTo(4));
    }

    @Test
    void mergeShouldThrowIfDataBagsOverlap() {
        // ARRANGE
        Field idField = new Field("id");
        Field priceField = new Field("price");

        Row row1 = GeneratedObjectBuilder.startBuilding()
            .set(idField, "foo", FieldSpecSource.Empty)
            .build();

        Row row2 = GeneratedObjectBuilder.startBuilding()
            .set(idField, "foo", FieldSpecSource.Empty)
            .set(priceField, 4, FieldSpecSource.Empty)
            .build();

        // ACT / ASSERT
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> GeneratedObjectMerger.merge(row1, row2));
    }
}

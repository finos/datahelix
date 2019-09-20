/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.builders.DataBagBuilder;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

class DataBagTests {
    @Test
    void getShouldReturnSettedValue() {
        // ARRANGE
        Field idField = createField("id");

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
        Field idField = createField("id");

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
        Field idField = createField("id");

        DataBag objectUnderTest = DataBag.empty;

        // ACT / ASSERT
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> objectUnderTest.getFormattedValue(idField));
    }

    @Test
    void mergedDataBagsShouldContainTheSameValuesAsInputs() {
        // ARRANGE
        Field idField = createField("id");
        Field priceField = createField("price");

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
        Field idField = createField("id");
        Field priceField = createField("price");

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

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
package com.scottlogic.datahelix.generator.core.fieldspecs.relations;

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpec;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpecFactory;
import com.scottlogic.datahelix.generator.core.generation.databags.DataBagValue;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.scottlogic.datahelix.generator.common.profile.FieldBuilder.createField;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMapRelationTest {
    private static InMapRelation testInstance;

    @BeforeAll
    static void before() {
        Field f1 = createField("field1");
        Field f2 = createField("field1");
        List<Object> values = Arrays.asList("foo", "bar");

        testInstance = new InMapRelation(f1, f2, values);
    }

    @Test
    void reduceValueToFieldSpec_whenValidIndex_returnFieldSpec() {
        FieldSpec expected = FieldSpecFactory.fromAllowedSingleValue("bar");
        FieldSpec actual = testInstance.createModifierFromOtherValue(new DataBagValue(BigDecimal.valueOf(1)));

        Assert.assertEquals(expected, actual);
    }

    @Test
    void reduceValueToFieldSpec_whenInvalidIndex_throws() {
        assertThrows(IndexOutOfBoundsException.class, () -> testInstance.createModifierFromOtherValue(new DataBagValue(BigDecimal.valueOf(3))));
    }

    @Test
    void reduceValueToFieldSpec_whenInvalidIndexType_throws() {
        assertThrows(ClassCastException.class, () -> testInstance.createModifierFromOtherValue(new DataBagValue("bar")));
    }
}
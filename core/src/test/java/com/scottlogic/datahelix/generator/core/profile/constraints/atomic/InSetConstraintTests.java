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

package com.scottlogic.datahelix.generator.core.profile.constraints.atomic;

import com.scottlogic.datahelix.generator.common.ValidationException;
import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.whitelist.DistributedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static com.scottlogic.datahelix.generator.common.profile.FieldBuilder.createField;

public class InSetConstraintTests
{
    @Test
    public void testConstraintThrowsIfGivenEmptySet(){
        Field field1 = createField("TestField");

        Assertions.assertThrows(
            ValidationException.class,
            () -> new InSetConstraint(field1, DistributedList.empty(), false));
    }

    @Test
    public void testConstraintThrowsIfGivenNullInASet(){
        Field field1 = createField("TestField");

        Assertions.assertThrows(
            ValidationException.class,
            () -> new InSetConstraint(field1, DistributedList.singleton(null), false));
    }

    @Test
    public void testConstraintThrowsNothingIfGivenAValidSet(){
        Field field1 = createField("TestField");
        Assertions.assertDoesNotThrow(
            () -> new InSetConstraint(field1, DistributedList.singleton("foo"), false));
    }

}

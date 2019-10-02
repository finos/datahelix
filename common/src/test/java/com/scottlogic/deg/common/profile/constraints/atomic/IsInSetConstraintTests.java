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

package com.scottlogic.deg.common.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

public class IsInSetConstraintTests {

    @Test
    public void testConstraintThrowsIfGivenEmptySet(){
        Field field1 = createField("TestField");

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new IsInSetConstraint(field1, DistributedList.empty()));
    }

    @Test
    public void testConstraintThrowsIfGivenNullInASet(){
        Field field1 = createField("TestField");

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new IsInSetConstraint(field1, DistributedList.singleton(null)));
    }

    @Test
    public void testConstraintThrowsNothingIfGivenAValidSet(){
        Field field1 = createField("TestField");
        Assertions.assertDoesNotThrow(
            () -> new IsInSetConstraint(field1, DistributedList.singleton("foo")));
    }

}

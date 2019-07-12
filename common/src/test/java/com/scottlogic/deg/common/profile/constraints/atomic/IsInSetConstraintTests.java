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
import com.scottlogic.deg.common.profile.RuleInformation;
import com.scottlogic.deg.generator.fieldspecs.whitelist.ElementFrequency;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyWhitelist;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

public class IsInSetConstraintTests {

    @Test
    public void testConstraintThrowsIfGivenEmptySet(){
        Field field1 = new Field("TestField");

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new IsInSetConstraint(field1, new FrequencyWhitelist<>(Collections.emptySet())));
    }

    @Test
    public void testConstraintThrowsIfGivenNullInASet(){
        Field field1 = new Field("TestField");

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new IsInSetConstraint(field1, new FrequencyWhitelist<>(Collections.singleton(null))));
    }

    @Test
    public void testConstraintThrowsNothingIfGivenAValidSet(){
        Field field1 = new Field("TestField");
        Assertions.assertDoesNotThrow(
            () -> new IsInSetConstraint(field1, new FrequencyWhitelist<>(
                Collections.singleton(
                    new ElementFrequency<>("foo", 1.0F)))));
    }

}

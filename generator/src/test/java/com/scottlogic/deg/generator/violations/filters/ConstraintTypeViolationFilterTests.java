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

package com.scottlogic.deg.generator.violations.filters;

import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsGreaterThanConstantConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.fieldspecs.whitelist.ElementFrequency;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyWhitelist;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;

/**
 * Defines tests for the ConstraintTypeViolationFilter
 */
public class ConstraintTypeViolationFilterTests {
    private ConstraintTypeViolationFilter target;

    @Before
    public void setup() {
        target = new ConstraintTypeViolationFilter(IsInSetConstraint.class);
    }

    /**
     * Tests that the canViolate method with matching type constraint returns false.
     */
    @Test
    public void canViolate_withMatchingTypeConstraint_returnsFalse() {
        //Arrange
        Constraint inputConstraint = new IsInSetConstraint(
            null,
            new FrequencyWhitelist<>(Collections.singleton(new ElementFrequency<>("", 1.0F))));

        //Act
        boolean actual = target.canViolate(inputConstraint);

        //Assert
        Assert.assertThat("Actual result should be false", actual, is(false));
    }

    /**
     * Tests that the canViolate method with non matching type constraint returns true.
     */
    @Test
    public void canViolate_withNonMatchingTypeConstraint_returnsTrue() {
        //Arrange
        Constraint inputConstraint = new IsGreaterThanConstantConstraint(null, 100);

        //Act
        boolean actual = target.canViolate(inputConstraint);

        //Assert
        Assert.assertThat("Actual result should be true", actual, is(true));
    }
}
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

package com.scottlogic.datahelix.generator.core.violations.filters;

import com.scottlogic.datahelix.generator.common.util.NumberUtils;
import com.scottlogic.datahelix.generator.common.whitelist.DistributedList;
import com.scottlogic.datahelix.generator.common.whitelist.WeightedElement;
import com.scottlogic.datahelix.generator.core.profile.constraints.Constraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.GreaterThanConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.InSetConstraint;
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
        target = new ConstraintTypeViolationFilter(InSetConstraint.class);
    }

    /**
     * Tests that the canViolate method with matching type constraint returns false.
     */
    @Test
    public void canViolate_withMatchingTypeConstraint_returnsFalse() {
        //Arrange
        Constraint inputConstraint = new InSetConstraint(
            null,
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("", 1.0F))));

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
        Constraint inputConstraint = new GreaterThanConstraint(null, NumberUtils.coerceToBigDecimal(100));

        //Act
        boolean actual = target.canViolate(inputConstraint);

        //Assert
        Assert.assertThat("Actual result should be true", actual, is(true));
    }
}
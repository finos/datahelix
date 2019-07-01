package com.scottlogic.deg.generator.violations.filters;

import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsGreaterThanConstantConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInSetConstraint;
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
        Constraint inputConstraint = new IsInSetConstraint(null, Collections.singleton(""));

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
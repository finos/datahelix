package com.scottlogic.deg.generator.violations.filters;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.ConditionalConstraint;

/**
 * Violation filter which filters on teh type of the constraint.
 */
public class ConstraintTypeViolationFilter implements ViolationFilter{
    private final Class constraintType;
    public ConstraintTypeViolationFilter(Class constraintType){
        this.constraintType = constraintType;
    }

    @Override
    public boolean canViolate(Constraint constraint) {
        return !constraint.getClass().equals(constraintType);
    }
}

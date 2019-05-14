package com.scottlogic.deg.generator.violations.filters;

import com.scottlogic.deg.common.constraint.Constraint;

public interface ViolationFilter {
    /**
     * For a violation filter, if the given constraint can be violated return true.
     * @param constraint Constraint to check.
     * @return True if the constraint is allowed for violation, otherwise false.
     */
    boolean canViolate(Constraint constraint);
}
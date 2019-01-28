package com.scottlogic.deg.generator.violations.filters;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.ConditionalConstraint;

public class ConstraintTypeViolationFilter implements ViolationFilter{
    private final Class constraintType;
    public ConstraintTypeViolationFilter(Class constraintType){
        this.constraintType = constraintType;
    }

    @Override
    public boolean accept(Constraint constraint) {
        if (isConstraintType(constraint)) {
            return false;
        }
        if (constraint instanceof ConditionalConstraint){
            ConditionalConstraint conditional = (ConditionalConstraint) constraint;
            if (isConstraintType(conditional.whenConditionIsTrue)){
                return false;
            }
            if (conditional.whenConditionIsTrue instanceof AndConstraint){
                for (Constraint subConstraint:((AndConstraint)(conditional).whenConditionIsTrue).subConstraints) {
                    if (isConstraintType(subConstraint)){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean isConstraintType(Constraint constraint){
        return constraint.getClass().equals(constraintType);
    }
}

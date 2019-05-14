package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.constraint.atomic.IsInSetConstraint;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AtomicConstraintBuilder {
    private ConstraintNodeBuilder constraintNodeBuilder;
    private Field field;

    protected AtomicConstraintBuilder(ConstraintNodeBuilder constraintNodeBuilder, Field field){
        this.constraintNodeBuilder = constraintNodeBuilder;
        this.field = field;
    }

    public ConstraintNodeBuilder isInSet(Object... legalValues){
        Set values = new HashSet();
        Collections.addAll(values, legalValues);
        IsInSetConstraint isInSetConstraint = new IsInSetConstraint(field, values, Collections.emptySet());
        constraintNodeBuilder.constraints.add(isInSetConstraint);
        return constraintNodeBuilder;
    }
}

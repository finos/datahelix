package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInSetConstraint;

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

    public ConstraintNodeBuilder isNotInSet(Object... legalValues){
        Set values = new HashSet();
        Collections.addAll(values, legalValues);
        AtomicConstraint isInSetConstraint = new IsInSetConstraint(field, values, Collections.emptySet()).negate();
        constraintNodeBuilder.constraints.add(isInSetConstraint);
        return constraintNodeBuilder;
    }

    public ConstraintNodeBuilder isNull(){
        IsNullConstraint isNullConstraint = new IsNullConstraint(field, Collections.emptySet());
        constraintNodeBuilder.constraints.add(isNullConstraint);
        return constraintNodeBuilder;
    }

    public ConstraintNodeBuilder isNotNull(){
        AtomicConstraint isNotNullConstraint = new IsNullConstraint(field, Collections.emptySet()).negate();
        constraintNodeBuilder.constraints.add(isNotNullConstraint);
        return constraintNodeBuilder;
    }
}

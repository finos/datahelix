package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

public class ConditionalConstraint implements IConstraint
{
    public final IConstraint condition;
    public final IConstraint whenConditionIsTrue;
    public final IConstraint whenConditionIsFalse;

    public ConditionalConstraint(
        IConstraint condition,
        IConstraint whenConditionIsTrue) {
        this(condition, whenConditionIsTrue, null);
    }

    public ConditionalConstraint(
        IConstraint condition,
        IConstraint whenConditionIsTrue,
        IConstraint whenConditionIsFalse) {
        this.condition = condition;
        this.whenConditionIsTrue = whenConditionIsTrue;
        this.whenConditionIsFalse = whenConditionIsFalse;
    }

    @Override
    public String toDotLabel() {
        throw new UnsupportedOperationException("IF constraints should be consumed during conversion to decision trees");
    }

    @Override
    public Field getField() {
        throw new UnsupportedOperationException();
    }
}

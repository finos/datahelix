package com.scottlogic.deg.generator.constraints;

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
        return null;
    }
}

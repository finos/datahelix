package com.scottlogic.deg.generator.constraints;

public class ConditionalConstraint implements Constraint
{
    public final Constraint condition;
    public final Constraint whenConditionIsTrue;
    public final Constraint whenConditionIsFalse;

    public ConditionalConstraint(
        Constraint condition,
        Constraint whenConditionIsTrue) {
        this(condition, whenConditionIsTrue, null);
    }

    public ConditionalConstraint(
        Constraint condition,
        Constraint whenConditionIsTrue,
        Constraint whenConditionIsFalse) {
        this.condition = condition;
        this.whenConditionIsTrue = whenConditionIsTrue;
        this.whenConditionIsFalse = whenConditionIsFalse;
    }
}

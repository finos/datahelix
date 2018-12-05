package com.scottlogic.deg.generator.constraints;

public class ConditionalConstraint implements LogicalConstraint
{
    public final LogicalConstraint condition;
    public final LogicalConstraint whenConditionIsTrue;
    public final LogicalConstraint whenConditionIsFalse;

    public ConditionalConstraint(
        LogicalConstraint condition,
        LogicalConstraint whenConditionIsTrue) {
        this(condition, whenConditionIsTrue, null);
    }

    public ConditionalConstraint(
        LogicalConstraint condition,
        LogicalConstraint whenConditionIsTrue,
        LogicalConstraint whenConditionIsFalse) {
        this.condition = condition;
        this.whenConditionIsTrue = whenConditionIsTrue;
        this.whenConditionIsFalse = whenConditionIsFalse;
    }
}

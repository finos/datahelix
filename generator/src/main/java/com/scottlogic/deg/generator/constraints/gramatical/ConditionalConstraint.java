package com.scottlogic.deg.generator.constraints.gramatical;

import com.scottlogic.deg.generator.constraints.Constraint;

public class ConditionalConstraint implements GramaticalConstraint
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

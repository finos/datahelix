package com.scottlogic.deg.generator.constraints.grammatical;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.Constraint;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConditionalConstraint implements GrammaticalConstraint
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

    @Override
    public Collection<Field> getFields() {
        return Stream.of(condition, whenConditionIsTrue, whenConditionIsFalse)
            .flatMap(constraint -> constraint.getFields().stream())
            .collect(Collectors.toList());
    }
}

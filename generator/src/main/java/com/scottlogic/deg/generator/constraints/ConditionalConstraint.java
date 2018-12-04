package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConditionalConstraint implements AtomicConstraint
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
    public Collection<Field> getFields() {
        return Stream.of(condition, whenConditionIsTrue, whenConditionIsFalse)
            .flatMap(constraint -> constraint.getFields().stream())
            .collect(Collectors.toList());
    }
}

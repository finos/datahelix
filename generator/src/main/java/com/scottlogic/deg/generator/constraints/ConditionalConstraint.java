package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.visitor.IConstraintValidatorVisitor;
import com.scottlogic.deg.generator.inputs.visitor.ValidationAlert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Collection<Field> getFields() {
        return Stream.of(condition, whenConditionIsTrue, whenConditionIsFalse)
            .flatMap(constraint -> constraint.getFields().stream())
            .collect(Collectors.toList());
    }

    @Override
    public List<ValidationAlert> accept(IConstraintValidatorVisitor visitor) {
        return new ArrayList<>();
    }
}

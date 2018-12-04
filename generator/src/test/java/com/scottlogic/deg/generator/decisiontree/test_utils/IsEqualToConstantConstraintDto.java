package com.scottlogic.deg.generator.decisiontree.test_utils;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.IsEqualToConstantConstraint;

public class IsEqualToConstantConstraintDto implements ConstraintDto {
    public FieldDto field;
    public String requiredValue;

    @Override
    public AtomicConstraint map() {
        return new IsEqualToConstantConstraint(new Field(field.name), requiredValue);
    }
}

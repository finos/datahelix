package com.scottlogic.deg.generator.decisiontree.test_utils;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsNullConstraint;

public class IsNullConstraintDto implements ConstraintDto {
    public FieldDto field;

    @Override
    public AtomicConstraint map() {
        return new IsNullConstraint(new Field(field.name));
    }
}

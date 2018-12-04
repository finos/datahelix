package com.scottlogic.deg.generator.decisiontree.test_utils;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsInSetConstraint;

import java.util.HashSet;
import java.util.List;

public class IsInSetConstraintDto implements ConstraintDto {
    public FieldDto field;
    public List<Object> legalValues;

    @Override
    public AtomicConstraint map() {
        return new IsInSetConstraint(new Field(field.name), new HashSet<>(legalValues));
    }
}

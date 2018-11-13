package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsInSetConstraint;

import java.util.HashSet;
import java.util.List;

public class IsInSetConstraintDto implements ConstraintDto {
    public FieldDto field;
    public List<Object> legalValues;

    @Override
    public IConstraint map() {
        return new IsInSetConstraint(new Field(field.name), new HashSet<>(legalValues));
    }
}

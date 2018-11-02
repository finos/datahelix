package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsLessThanConstantConstraint;

public class IsLessThanConstantConstraintDto implements IConstraintDto {
    public FieldDto field;
    public Number referenceValue;

    @Override
    public IConstraint map() {
        return new IsLessThanConstantConstraint(new Field(this.field.name), this.referenceValue);
    }
}

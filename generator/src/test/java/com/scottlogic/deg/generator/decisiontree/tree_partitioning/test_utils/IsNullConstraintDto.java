package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsNullConstraint;

public class IsNullConstraintDto implements IConstraintDto {
    public FieldDto field;

    @Override
    public IConstraint map() {
        return new IsNullConstraint(new Field(this.field.name));
    }
}

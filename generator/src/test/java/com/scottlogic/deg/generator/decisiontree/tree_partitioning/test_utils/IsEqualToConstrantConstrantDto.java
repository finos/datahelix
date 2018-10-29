package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsEqualToConstantConstraint;

public class IsEqualToConstrantConstrantDto implements IConstraintDto {
    public FieldDto field;
    public String requiredValue;

    @Override
    public IConstraint map() {
        return new IsEqualToConstantConstraint(new Field(this.field.name), this.requiredValue);
    }
}

package com.scottlogic.deg.generator.decisiontree.serialisation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsEqualToConstantConstraint;

public class IsEqualToConstantConstraintDto implements ConstraintDto {
    public FieldDto field;
    public String requiredValue;

    @Override
    public IConstraint fromDto() {
        return new IsEqualToConstantConstraint(new Field(field.name), requiredValue);
    }
}

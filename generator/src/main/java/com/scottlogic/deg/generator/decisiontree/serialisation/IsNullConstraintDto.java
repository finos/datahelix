package com.scottlogic.deg.generator.decisiontree.serialisation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsNullConstraint;

public class IsNullConstraintDto implements ConstraintDto {
    public FieldDto field;
    
    @Override
    public IConstraint fromDto() {
        return new IsNullConstraint(new Field(field.name));
    }
}

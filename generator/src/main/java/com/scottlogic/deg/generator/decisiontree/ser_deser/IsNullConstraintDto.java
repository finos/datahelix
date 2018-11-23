package com.scottlogic.deg.generator.decisiontree.ser_deser;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsNullConstraint;

public class IsNullConstraintDto implements ConstraintDto {
    public FieldDto field;

    static public IsNullConstraintDto toDto(IsNullConstraint constraint) {
        IsNullConstraintDto dto = new IsNullConstraintDto();
        dto.field = new FieldDto(constraint.field.name);
        return dto;
    }
    
    @Override
    public IConstraint map() {
        return new IsNullConstraint(new Field(field.name));
    }
}

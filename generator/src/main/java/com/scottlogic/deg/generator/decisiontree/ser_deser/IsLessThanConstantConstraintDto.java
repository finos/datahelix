package com.scottlogic.deg.generator.decisiontree.ser_deser;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsLessThanConstantConstraint;

public class IsLessThanConstantConstraintDto implements ConstraintDto {
    public FieldDto field;
    public Number referenceValue;

    static public IsLessThanConstantConstraintDto toDto(IsLessThanConstantConstraint constraint) {
        IsLessThanConstantConstraintDto dto = new IsLessThanConstantConstraintDto();
        dto.field = new FieldDto(constraint.field.name);
        dto.referenceValue = constraint.referenceValue;
        return dto;
    }
    
    @Override
    public IConstraint map() {
        return new IsLessThanConstantConstraint(new Field(field.name), referenceValue);
    }
}

package com.scottlogic.deg.generator.decisiontree.ser_deser;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsStringShorterThanConstraint;

public class IsStringShorterThanConstraintDto implements ConstraintDto {
    public FieldDto field;
    public int referenceValue;

    static public IsStringShorterThanConstraintDto toDto(IsStringShorterThanConstraint constraint) {
        IsStringShorterThanConstraintDto dto = new IsStringShorterThanConstraintDto();
        dto.field = new FieldDto(constraint.field.name);
        dto.referenceValue = constraint.referenceValue;
        return dto;
    }
    
    @Override
    public IConstraint map() {
        return new IsStringShorterThanConstraint(new Field(field.name), referenceValue);
    }
}

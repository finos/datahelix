package com.scottlogic.deg.generator.decisiontree.serialisation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsEqualToConstantConstraint;

public class IsEqualToConstantConstraintDto implements ConstraintDto {
    public FieldDto field;
    public String requiredValue;

    static public IsEqualToConstantConstraintDto toDto(IsEqualToConstantConstraint constraint) {
        IsEqualToConstantConstraintDto dto = new IsEqualToConstantConstraintDto();
        dto.field = new FieldDto(constraint.field.name);
        dto.requiredValue = constraint.requiredValue.toString();
        return dto;
    }
    
    
    @Override
    public IConstraint map() {
        return new IsEqualToConstantConstraint(new Field(field.name), requiredValue);
    }
}

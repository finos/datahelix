package com.scottlogic.deg.generator.decisiontree.ser_deser;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsInSetConstraint;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class IsInSetConstraintDto implements ConstraintDto {
    public FieldDto field;
    public List<Object> legalValues;
    
    static public IsInSetConstraintDto toDto(IsInSetConstraint constraint) {
        IsInSetConstraintDto dto = new IsInSetConstraintDto();
        dto.field = new FieldDto(constraint.field.name);
        dto.legalValues = new ArrayList<>(constraint.legalValues);
        return dto;
    }

    @Override
    public IConstraint map() {
        return new IsInSetConstraint(new Field(field.name), new HashSet<>(legalValues));
    }
}

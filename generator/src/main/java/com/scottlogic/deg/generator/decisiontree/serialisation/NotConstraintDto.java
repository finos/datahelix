package com.scottlogic.deg.generator.decisiontree.serialisation;

import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.NotConstraint;

public class NotConstraintDto implements ConstraintDto {
    public ConstraintDto negatedConstraint;

    static public NotConstraintDto toDto(NotConstraint constraint) {
        NotConstraintDto dto = new NotConstraintDto();
        dto.negatedConstraint = DecisionTreeMapper.toDto(constraint.negatedConstraint);
        return dto;
    }
    
    @Override
    public IConstraint map() {
        return new NotConstraint(negatedConstraint.map());
    }
}

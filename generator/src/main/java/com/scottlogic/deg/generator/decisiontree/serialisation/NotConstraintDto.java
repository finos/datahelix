package com.scottlogic.deg.generator.decisiontree.serialisation;

import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.NotConstraint;

public class NotConstraintDto implements ConstraintDto {
    public ConstraintDto negatedConstraint;
  
    @Override
    public IConstraint fromDto() {
        return new NotConstraint(negatedConstraint.fromDto());
    }
}

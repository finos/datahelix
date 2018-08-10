package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

public class IsStringShorterThanConstraint implements IConstraint {
    public final Field field;
    public final Number referenceValue;

    public IsStringShorterThanConstraint(Field field, Number referenceValue) {

        if(referenceValue == null){
            throw new IllegalArgumentException("Argument 'referenceValue' cannot be null.");
        }

        this.referenceValue = referenceValue;
        this.field = field;
    }
}

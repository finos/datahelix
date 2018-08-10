package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

public class IsStringLongerThanConstraint implements IConstraint {
    public final Field field;
    public final Number referenceValue;

    public IsStringLongerThanConstraint(Field field, Number referenceValue) {

        if(referenceValue == null){
            throw new IllegalArgumentException("Argument 'referenceValue' cannot be null.");
        }


        this.referenceValue = referenceValue;
        this.field = field;
    }
}

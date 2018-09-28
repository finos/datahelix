package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

public class IsLessThanConstantConstraint implements IConstraint {
    public final Field field;
    public final Number referenceValue;

    public IsLessThanConstantConstraint(Field field, Number referenceValue) {
        this.referenceValue = referenceValue;
        this.field = field;
    }

    @Override
    public String toString(){
        return String.format("%s < %s", field.name, referenceValue);
    }
}

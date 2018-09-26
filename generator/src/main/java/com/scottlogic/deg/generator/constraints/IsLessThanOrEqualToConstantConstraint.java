package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

public class IsLessThanOrEqualToConstantConstraint implements IConstraint {
    public final Field field;
    public final Number referenceValue;

    public IsLessThanOrEqualToConstantConstraint(Field field, Number referenceValue) {
        this.referenceValue = referenceValue;
        this.field = field;
    }

    @Override
    public String toString(){
        return String.format("%s is less than or equal to %s", field.name, referenceValue);
    }
}

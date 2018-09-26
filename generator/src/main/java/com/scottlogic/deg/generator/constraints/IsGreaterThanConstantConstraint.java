package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

public class IsGreaterThanConstantConstraint implements IConstraint
{
    public final Field field;
    public final Number referenceValue;

    public IsGreaterThanConstantConstraint(Field field, Number referenceValue) {
        this.referenceValue = referenceValue;
        this.field = field;
    }

    @Override
    public String toString(){
        return String.format("%s is greater than %s", field.name, referenceValue);
    }
}

package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

public class IsGreaterThanOrEqualToConstantConstraint implements IConstraint
{
    public final Field field;
    public final Number referenceValue;

    public IsGreaterThanOrEqualToConstantConstraint(Field field, Number referenceValue) {
        this.referenceValue = referenceValue;
        this.field = field;
    }
}

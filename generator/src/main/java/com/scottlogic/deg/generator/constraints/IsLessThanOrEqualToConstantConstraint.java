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
    public String toDotLabel() {
        return String.format("%s <= %s", field.name, referenceValue);
    }
}

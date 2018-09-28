package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

public class IsStringLongerThanConstraint implements IConstraint {
    public final Field field;
    public final int referenceValue;

    public IsStringLongerThanConstraint(Field field, int referenceValue) {
        this.referenceValue = referenceValue;
        this.field = field;
    }

    @Override
    public String toString(){
        return String.format("%s length > '%s'", field.name, referenceValue);
    }
}

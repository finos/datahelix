package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

public class IsNullConstraint implements IConstraint
{
    public final Field field;

    public IsNullConstraint(Field field) {
        this.field = field;
    }

    @Override
    public String toString(){
        return String.format("%s is null", field.name);
    }
}

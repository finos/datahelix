package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

public class IsEqualToConstantConstraint implements IConstraint
{
    public final Field field;
    public final Object requiredValue;

    public IsEqualToConstantConstraint(Field field, Object requiredValue) {
        this.field = field;
        this.requiredValue = requiredValue;
    }
}

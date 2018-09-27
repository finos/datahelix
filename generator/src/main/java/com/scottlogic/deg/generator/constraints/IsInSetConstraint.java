package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

import java.util.Set;

public class IsInSetConstraint implements IConstraint
{
    public final Field field;
    public final Set<Object> legalValues;

    public IsInSetConstraint(Field field, Set<Object> legalValues) {
        this.field = field;
        this.legalValues = legalValues;

        if(legalValues.isEmpty()){
            throw new IllegalStateException("Cannot create an IsInSetConstraint for field '" +
                field.name + "' with an empty set.");
        }
    }
}

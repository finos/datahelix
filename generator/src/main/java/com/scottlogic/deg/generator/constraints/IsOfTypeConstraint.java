package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

public class IsOfTypeConstraint implements IConstraint
{
    public final Field field;
    public final Types requiredType;

    public IsOfTypeConstraint(Field field, Types requiredType) {
        this.field = field;
        this.requiredType = requiredType;
    }

    public enum Types
    {
        Numeric,
        String,
        Temporal
    }

    @Override
    public String toString(){
        return String.format("%s of type '%s'", field.name, requiredType.name());
    }
}

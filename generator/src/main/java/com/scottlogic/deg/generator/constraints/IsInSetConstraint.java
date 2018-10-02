package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

import java.util.Set;
import java.util.stream.Collectors;

public class IsInSetConstraint implements IConstraint
{
    public final Field field;
    public final Set<Object> legalValues;

    public IsInSetConstraint(Field field, Set<Object> legalValues) {
        this.field = field;
        this.legalValues = legalValues;
    }

    @Override
    public String toDotLabel(){
        return String.format("%s in [%s]", field.name,
            legalValues.stream().map(x -> x.toString()).collect(Collectors.joining(", ")));
    }
}

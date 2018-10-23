package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;

public class IsNullConstraint implements IConstraint
{
    public final Field field;

    public IsNullConstraint(Field field) {
        this.field = field;
    }

    @Override
    public String toDotLabel() {
        return String.format("%s is null", field.name);
    }

    public String toString(){
        return String.format(
                "`%s`: %s",
                NullRestrictions.Nullness.MustBeNull,
                field.toString());
    }
}

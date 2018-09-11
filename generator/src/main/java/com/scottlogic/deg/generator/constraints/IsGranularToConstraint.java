package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

public class IsGranularToConstraint implements IConstraint {
    public final Field field;
    public final String granularity;

    public IsGranularToConstraint(Field field, String granularity) {
        this.granularity = granularity;
        this.field = field;
    }
}

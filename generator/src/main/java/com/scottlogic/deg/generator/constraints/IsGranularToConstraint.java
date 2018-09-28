package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.restrictions.ParsedGranularity;

public class IsGranularToConstraint implements IConstraint {
    public final Field field;
    public final ParsedGranularity granularity;

    public IsGranularToConstraint(Field field, ParsedGranularity granularity) {
        this.granularity = granularity;
        this.field = field;
    }

    @Override
    public String toString(){
        return String.format("%s granular to '%s'", field.name, granularity);
    }
}

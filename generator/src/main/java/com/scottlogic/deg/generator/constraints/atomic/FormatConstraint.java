package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;

public class FormatConstraint implements AtomicConstraint {

    public final Field field;
    public final String format;

    public FormatConstraint(Field field, String format) {
        this.field = field;
        this.format = format;
    }

    @Override
    public String toDotLabel(){
        return String.format("%s has format '%s'", field.name, format);
    }

    @Override
    public Field getField() {
        return field;
    }
}

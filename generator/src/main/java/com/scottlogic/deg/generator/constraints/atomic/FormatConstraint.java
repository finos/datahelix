package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.ConstraintRule;

public class FormatConstraint implements AtomicConstraint {

    public final Field field;
    public final String format;
    private final ConstraintRule rule;

    public FormatConstraint(Field field, String format, ConstraintRule rule) {
        this.field = field;
        this.format = format;
        this.rule = rule;
    }

    @Override
    public String toDotLabel(){
        return String.format("%s has format '%s'", field.name, format);
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public ConstraintRule getRule() {
        return rule;
    }
}

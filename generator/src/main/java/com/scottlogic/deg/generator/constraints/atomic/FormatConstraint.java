package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.RuleInformation;

public class FormatConstraint implements AtomicConstraint {

    public final Field field;
    public final String format;
    private final RuleInformation rule;

    public FormatConstraint(Field field, String format, RuleInformation rule) {
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
    public RuleInformation getRule() {
        return rule;
    }
}

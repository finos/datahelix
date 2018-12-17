package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Set;

public class FormatConstraint implements AtomicConstraint {

    public final Field field;
    public final String format;
    private final Set<RuleInformation> rules;

    public FormatConstraint(Field field, String format, Set<RuleInformation> rules) {
        this.field = field;
        this.format = format;
        this.rules = rules;
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
    public Set<RuleInformation> getRules() {
        return rules;
    }

    @Override
    public AtomicConstraint withRules(Set<RuleInformation> rules) {
        return new FormatConstraint(this.field, this.format, rules);
    }
}

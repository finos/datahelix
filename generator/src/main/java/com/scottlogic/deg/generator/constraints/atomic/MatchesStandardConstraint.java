package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.ConstraintRule;
import com.scottlogic.deg.generator.generation.IStringGenerator;

public class MatchesStandardConstraint implements AtomicConstraint {
    public final Field field;
    public final IStringGenerator standard; // TODO: Change this to an enum member; string generators shouldn't exist on this level
    private final ConstraintRule rule;

    public MatchesStandardConstraint(Field field, IStringGenerator standard, ConstraintRule rule) {
        this.field = field;
        this.standard = standard;
        this.rule = rule;
    }

    @Override
    public String toDotLabel(){
        return String.format("%s is a %s", field.name, standard.getClass().getName());
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

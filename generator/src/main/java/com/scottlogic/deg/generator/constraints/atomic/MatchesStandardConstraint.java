package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.generation.StringGenerator;

public class MatchesStandardConstraint implements AtomicConstraint {
    public final Field field;
    public final StringGenerator standard; // TODO: Change this to an enum member; string generators shouldn't exist on this level

    public MatchesStandardConstraint(Field field, StringGenerator standard) {
        this.field = field;
        this.standard = standard;
    }

    @Override
    public String toDotLabel(){
        return String.format("%s is a %s", field.name, standard.getClass().getName());
    }

    @Override
    public Field getField() {
        return field;
    }
}

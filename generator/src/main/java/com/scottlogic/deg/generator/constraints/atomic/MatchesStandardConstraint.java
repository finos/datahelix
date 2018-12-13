package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.ConstraintRule;
import com.scottlogic.deg.generator.generation.StringGenerator;

import java.util.Objects;

public class MatchesStandardConstraint implements AtomicConstraint {
    public final Field field;
    public final StringGenerator standard; // TODO: Change this to an enum member; string generators shouldn't exist on this level
    private final ConstraintRule rule;

    public MatchesStandardConstraint(Field field, StringGenerator standard, ConstraintRule rule) {
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

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o instanceof ViolatedAtomicConstraint) {
            return o.equals(this);
        }
        if (o == null || getClass() != o.getClass()) return false;
        MatchesStandardConstraint constraint = (MatchesStandardConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(standard, constraint.standard);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, standard);
    }
}

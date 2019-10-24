package com.scottlogic.deg.generator.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory;

import java.util.Objects;
import java.util.regex.Pattern;

public class NotMatchesStandardConstraint implements AtomicConstraint {
    public final Field field;
    public final StandardConstraintTypes standard;

    public NotMatchesStandardConstraint(Field field, StandardConstraintTypes standard) {
        this.field = field;
        this.standard = standard;
    }

    @Override
    public String toString(){
        return String.format("%s is a %s", field.name, standard.getClass().getName());
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AtomicConstraint negate() {
        return new MatchesStandardConstraint(field, standard);
    }

    @Override
    public FieldSpec toFieldSpec() {
        return FieldSpecFactory.fromRestriction(StringRestrictionsFactory.forStringMatching(Pattern.compile(standard.getRegex()), false));
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o instanceof ViolatedAtomicConstraint) {
            return o.equals(this);
        }
        if (o == null || getClass() != o.getClass()) return false;
        NotMatchesStandardConstraint constraint = (NotMatchesStandardConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(standard, constraint.standard);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, standard);
    }
}


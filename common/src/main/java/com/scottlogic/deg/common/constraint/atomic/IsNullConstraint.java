package com.scottlogic.deg.common.constraint.atomic;

import com.scottlogic.deg.common.constraint.restriction.Nullness;
import com.scottlogic.deg.common.profile.Field;

import com.scottlogic.deg.common.profile.RuleInformation;

import java.util.Objects;
import java.util.Set;

public class IsNullConstraint implements AtomicConstraint
{
    public final Field field;
    private final Set<RuleInformation> rules;

    public IsNullConstraint(Field field, Set<RuleInformation> rules) {
        this.field = field;
        this.rules = rules;
    }

    @Override
    public String toDotLabel() {
        return String.format("%s is null", field.name);
    }

    public String toString(){
        return String.format(
                "`%s`: %s",
                Nullness.MUST_BE_NULL,
                field.toString());
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o instanceof ViolatedAtomicConstraint) {
            return o.equals(this);
        }
        if (o == null || getClass() != o.getClass()) return false;
        IsNullConstraint constraint = (IsNullConstraint) o;
        return Objects.equals(field, constraint.field);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field);
    }


    @Override
    public Set<RuleInformation> getRules() {
        return rules;
    }

    @Override
    public AtomicConstraint withRules(Set<RuleInformation> rules) {
        return new IsNullConstraint(this.field, rules);
    }
}

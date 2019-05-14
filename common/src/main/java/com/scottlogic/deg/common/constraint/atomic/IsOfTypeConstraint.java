package com.scottlogic.deg.common.constraint.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.RuleInformation;

import java.util.Objects;
import java.util.Set;

public class IsOfTypeConstraint implements AtomicConstraint {
    public final Field field;
    public final Types requiredType;
    private final Set<RuleInformation> rules;

    public IsOfTypeConstraint(Field field, Types requiredType, Set<RuleInformation> rules) {
        this.field = field;
        this.requiredType = requiredType;
        this.rules = rules;
    }

    public enum Types {
        NUMERIC,
        STRING,
        DATETIME
    }

    @Override
    public String toDotLabel() {
        return String.format("%s is %s", field.name, requiredType.name());
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
        IsOfTypeConstraint constraint = (IsOfTypeConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(requiredType, constraint.requiredType);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, requiredType);
    }

    @Override
    public String toString() { return String.format("`%s` is %s", field.name, requiredType.name()); }

    @Override
    public Set<RuleInformation> getRules() {
        return rules;
    }

    @Override
    public AtomicConstraint withRules(Set<RuleInformation> rules) {
        return new IsOfTypeConstraint(this.field, this.requiredType, rules);
    }
}
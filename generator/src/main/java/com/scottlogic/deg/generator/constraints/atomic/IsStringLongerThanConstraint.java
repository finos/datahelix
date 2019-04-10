package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.validation.ProfileVisitor;
import com.scottlogic.deg.generator.inputs.validation.VisitableProfileElement;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class IsStringLongerThanConstraint implements AtomicConstraint, VisitableProfileElement {
    private final Set<RuleInformation> rules;
    private final boolean isSoftConstraint;

    public final Field field;
    public final int referenceValue;

    public IsStringLongerThanConstraint(Field field, int referenceValue, Set<RuleInformation> rules) {
        this(field, referenceValue, rules, false);
    }

    private IsStringLongerThanConstraint(Field field, int referenceValue, Set<RuleInformation> rules, boolean isSoftConstraint) {
        if (referenceValue < 0){
            throw new IllegalArgumentException("Cannot create an IsStringLongerThanConstraint for field '" +
                field.name + "' with a a negative length.");
        }

        this.isSoftConstraint = isSoftConstraint;
        this.field = field;
        this.rules = rules;
        this.referenceValue = referenceValue;
    }

    public static IsStringLongerThanConstraint softConstraint(Field field, int referenceValue){
        return new IsStringLongerThanConstraint(field, referenceValue, Collections.emptySet(), true);
    }

    @Override
    public boolean isSoftConstraint() {
        return isSoftConstraint;
    }

    @Override
    public String toDotLabel(){
        return String.format("%s length > %s", field.name, referenceValue);
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
        IsStringLongerThanConstraint constraint = (IsStringLongerThanConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(referenceValue, constraint.referenceValue);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, referenceValue);
    }

    @Override
    public String toString() { return String.format("`%s` length > %d", field.name, referenceValue); }

    @Override
    public void accept(ProfileVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Set<RuleInformation> getRules() {
        return rules;
    }

    @Override
    public AtomicConstraint withRules(Set<RuleInformation> rules) {
        return new IsStringLongerThanConstraint(this.field, this.referenceValue, rules);
    }
}

package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.validation.ProfileVisitor;
import com.scottlogic.deg.generator.inputs.validation.VisitableProfileElement;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.generator.restrictions.ParsedGranularity;

import java.util.Objects;
import java.util.Set;

public class IsGranularToNumericConstraint implements AtomicConstraint, VisitableProfileElement {
    public final Field field;
    private final Set<RuleInformation> rules;
    public final ParsedGranularity granularity;

    public IsGranularToNumericConstraint(Field field, ParsedGranularity granularity, Set<RuleInformation> rules) {
        this.granularity = granularity;
        this.field = field;
        this.rules = rules;
    }

    @Override
    public String toDotLabel() {
        return String.format("%s granular to %s", field.name, granularity.getNumericGranularity());
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
        IsGranularToNumericConstraint constraint = (IsGranularToNumericConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(granularity.getNumericGranularity(), constraint.granularity.getNumericGranularity());
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, granularity.getNumericGranularity());
    }

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
        return new IsGranularToNumericConstraint(this.field, this.granularity, rules);
    }

    @Override
    public String toString() {
        return String.format("granularTo %s", this.granularity.getNumericGranularity());
    }
}

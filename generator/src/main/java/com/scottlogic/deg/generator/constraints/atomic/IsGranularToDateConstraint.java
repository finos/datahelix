package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.generator.inputs.validation.ProfileVisitor;

import com.scottlogic.deg.generator.restrictions.ParsedDateGranularity;
import com.scottlogic.deg.generator.restrictions.ParsedGranularity;

import java.util.Objects;
import java.util.Set;

public class IsGranularToDateConstraint implements AtomicConstraint {
    public final Field field;
    private final Set<RuleInformation> rules;
    public final ParsedDateGranularity granularity;

    public IsGranularToDateConstraint(Field field, ParsedDateGranularity granularity, Set<RuleInformation> rules) {
        if(field == null)
            throw new IllegalArgumentException("field must not be null");
        if(granularity == null)
            throw new IllegalArgumentException("granularity must not be null");

        this.granularity = granularity;
        this.field = field;
        this.rules = rules;
    }

    @Override
    public String toDotLabel() {
        return String.format("%s granular to %s", field.name, granularity.getGranularity());
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
        IsGranularToDateConstraint constraint = (IsGranularToDateConstraint) o;
        return (field.equals(constraint.field) && Objects.equals(granularity.getGranularity(), constraint.granularity.getGranularity()));
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, granularity);
    }

    @Override
    public Set<RuleInformation> getRules() {
        return rules;
    }

    @Override
    public AtomicConstraint withRules(Set<RuleInformation> rules) {
        return new IsGranularToDateConstraint(field, granularity, rules);
    }

    @Override
    public String toString() {
        return String.format("granularTo %s", granularity.getGranularity());
    }
}

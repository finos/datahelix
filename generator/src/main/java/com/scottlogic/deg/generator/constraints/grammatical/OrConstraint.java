package com.scottlogic.deg.generator.constraints.grammatical;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrConstraint implements GrammaticalConstraint {
    public final Collection<Constraint> subConstraints;

    public OrConstraint(Collection<Constraint> subConstraints) {
        this.subConstraints = subConstraints;
    }

    public OrConstraint(Constraint... subConstraints) {
        this(Arrays.asList(subConstraints));
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrConstraint otherConstraint = (OrConstraint) o;
        return Objects.equals(subConstraints, otherConstraint.subConstraints);
    }

    @Override
    public int hashCode(){
        return Objects.hash("OR", subConstraints);
    }

    @Override
    public Collection<Field> getFields() {
        return subConstraints.stream().flatMap(c -> c.getFields().stream()).collect(Collectors.toSet());
    }

    @Override
    public RuleInformation getRule() {
        return RuleInformation.fromConstraints(subConstraints, " or ");
    }
}

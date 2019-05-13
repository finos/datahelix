package com.scottlogic.deg.generator.constraints.grammatical;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.generator.inputs.validation.ProfileVisitor;


import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class AndConstraint implements GrammaticalConstraint
{
    public final Collection<Constraint> subConstraints;

    public AndConstraint(Collection<Constraint> subConstraints) {
        this.subConstraints = subConstraints;
    }

    public AndConstraint(Constraint... subConstraints) {
        this(Arrays.asList(subConstraints));
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AndConstraint otherConstraint = (AndConstraint) o;
        return Objects.equals(subConstraints, otherConstraint.subConstraints);
    }

    @Override
    public int hashCode(){
        return Objects.hash("AND", subConstraints);
    }

    @Override
    public Collection<Field> getFields() {
        return subConstraints.stream().flatMap(c -> c.getFields().stream()).collect(Collectors.toSet());
    }

    @Override
    public Set<RuleInformation> getRules() {
        return this.subConstraints
            .stream()
            .flatMap(c -> c.getRules().stream())
            .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return String.join(
            " and ",
            this.subConstraints
                .stream()
                .map(Object::toString)
                .collect(Collectors.toSet()));
    }
}

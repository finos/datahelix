package com.scottlogic.deg.common.profile.constraints.grammatical;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.RuleInformation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
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
    public Set<RuleInformation> getRules() {
        return this.subConstraints
            .stream()
            .flatMap(c -> c.getRules().stream())
            .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return String.join(
            " or ",
            this.subConstraints
                .stream()
                .map(Object::toString)
                .collect(Collectors.toSet()));
    }
}

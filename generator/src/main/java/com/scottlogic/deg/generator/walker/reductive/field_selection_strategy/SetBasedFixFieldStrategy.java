package com.scottlogic.deg.generator.walker.reductive.field_selection_strategy;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.Guice.ProfileProvider;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.IsInSetConstraint;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Stream;

final class SetBasedFixFieldStrategy extends ProfileBasedFixFieldStrategy {

    SetBasedFixFieldStrategy(ProfileProvider profileProvider) {
        super(profileProvider);
    }

    Comparator<Field> getFieldOrderingStrategy() {
        Comparator<Field> fieldHasSetConstraint = Comparator.comparing(this::fieldConstrainedBySet);
        Comparator<Field> preferSmallerSets = Comparator.comparingInt(this::numValuesInSet);
        return fieldHasSetConstraint.thenComparing(preferSmallerSets);
    }

    private boolean fieldConstrainedBySet(Field field) {
        return constraintsFromProfile()
            .anyMatch(constraint -> constraint instanceof IsInSetConstraint
                && constraint.getFields().iterator().next().equals(field));
    }

    private int numValuesInSet(Field field) {
        return constraintsFromProfile()
            .filter(constraint -> constraint instanceof IsInSetConstraint
                && constraint.getFields().iterator().next().equals(field))
            .map(constraint -> ((IsInSetConstraint) constraint).legalValues)
            .max(Comparator.comparing(Set::size))
            .orElse(Collections.emptySet())
            .size();
    }

    private Stream<Constraint> constraintsFromProfile(){
        return FlatMappingSpliterator.flatMap(profileProvider.get().rules.stream(), rule -> rule.constraints.stream());
    }

}

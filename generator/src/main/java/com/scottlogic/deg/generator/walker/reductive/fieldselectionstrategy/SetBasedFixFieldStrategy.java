package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Stream;

final class SetBasedFixFieldStrategy extends ProfileBasedFixFieldStrategy {
    private final DecisionTree tree;

    SetBasedFixFieldStrategy(DecisionTree tree) {
        this.tree = tree;
    }

    Comparator<Field> getFieldOrderingStrategy() {
        Comparator<Field> fieldHasSetConstraint = Comparator.comparing(this::fieldConstrainedBySet);
        Comparator<Field> preferSmallerSets = Comparator.comparingInt(this::numValuesInSet);
        return fieldHasSetConstraint.thenComparing(preferSmallerSets);
    }

    private boolean fieldConstrainedBySet(Field field) {
        return constraintsFromProfile(tree)
            .anyMatch(constraint -> constraint instanceof IsInSetConstraint
                && constraint.getFields().iterator().next().equals(field));
    }

    private int numValuesInSet(Field field) {
        return constraintsFromProfile(tree)
            .filter(constraint -> constraint instanceof IsInSetConstraint
                && constraint.getFields().iterator().next().equals(field))
            .map(constraint -> ((IsInSetConstraint) constraint).legalValues)
            .max(Comparator.comparing(Set::size))
            .orElse(Collections.emptySet())
            .size();
    }

    private Stream<AtomicConstraint> constraintsFromProfile(DecisionTree tree){
        return AtomicConstraintCollector.getAllAtomicConstraints(tree).stream();
    }

}

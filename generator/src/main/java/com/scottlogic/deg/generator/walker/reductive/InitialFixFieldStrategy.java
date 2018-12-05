package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;

import java.util.Comparator;
import java.util.stream.Collectors;

public class InitialFixFieldStrategy implements FixFieldStrategy {

    /*
    1. Get all the atomic constraints in the tree, group them by field
    2. Rank each constraint by it's precision (e.g. an equalTo is precise, a set of 2 values is half as precise)
    2.1. should also include number of decisions influenced by the constraint?
    3. Pick the constraint with the highest rank (greatest precision)
    4. Yield detail of the constraint and its related field
    */
    @Override
    public FieldAndConstraintMapping getFieldAndConstraintMapToFixNext(ReductiveConstraintNode rootNode) {
        return rootNode.getAllUnfixedAtomicConstraints()
            .stream()
            .collect(Collectors.groupingBy(AtomicConstraint::getField))

            .entrySet()
            .stream()
            .map(entry -> new FieldAndConstraintMapping(entry.getKey(), entry.getValue()))
            .max(Comparator.comparing(FieldAndConstraintMapping::getPriority))

            .filter(c -> !c.getConstraints().isEmpty())
            .orElse(null);
    }
}

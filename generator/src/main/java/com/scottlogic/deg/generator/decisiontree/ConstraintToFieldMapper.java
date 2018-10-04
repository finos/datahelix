package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.reducer.ConstraintFieldSniffer;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Given a decision tree, find which constraints and decisions act on which fields and return a map from them to fields
 */
class ConstraintToFieldMapper {
    private final ConstraintFieldSniffer constraintSniffer = new ConstraintFieldSniffer();

    private Stream<Field> findFields(ConstraintNode node) {
        return Stream.concat(
            node.getAtomicConstraints()
                .stream()
                .map(constraintSniffer::detectField),
            node.getDecisions()
                .stream()
                .flatMap(decision ->
                    decision
                        .getOptions()
                        .stream()
                        .flatMap(this::findFields))
        );
    }

    Map<DecisionNode, Set<Field>> mapDecisionsToFields(DecisionTree decisionTree) {
        return decisionTree
            .getRootNode()
            .getDecisions()
            .stream()
            .collect(
                Collectors.toMap(
                    decision -> decision,
                    decision -> decision
                        .getOptions()
                        .stream()
                        .flatMap(this::findFields)
                        .collect(Collectors.toSet())
                )
            );
    }

    Map<IConstraint, Set<Field>> mapConstraintsToFields(DecisionTree decisionTree) {
        return decisionTree
            .getRootNode()
            .getAtomicConstraints()
            .stream()
            .collect(Collectors.toMap(
                    constraint -> constraint,
                constraint -> Collections.singleton(constraintSniffer.detectField(constraint))
            ));
    }
}

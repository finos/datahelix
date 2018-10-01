package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.reducer.ConstraintFieldSniffer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Given a decision tree, find which constraints and decisions act on which fields and return a map from them to fields
 */
class ConstraintToFieldMapper {

    private class ConstraintToFields {
        public Object constraint;
        public Set<Field> fields;

        ConstraintToFields(Object constraint, Set<Field> fields) {
            this.constraint = constraint;
            this.fields = fields;
        }

        ConstraintToFields(Object constraint, Field field) {
            this.constraint = constraint;
            this.fields = Collections.singleton(field);
        }
    }

    private final ConstraintFieldSniffer constraintSniffer = new ConstraintFieldSniffer();

    private Stream<ConstraintToFields> mapConstraintToFields(ConstraintNode node) {
        return Stream.concat(
            node.getAtomicConstraints()
                .stream()
                .map(constraint -> new ConstraintToFields(constraint, constraintSniffer.detectField(constraint))),
            node.getDecisions()
                .stream()
                .map(decision -> new ConstraintToFields(
                    decision,
                    decision
                        .getOptions()
                        .stream()
                        .flatMap(this::mapConstraintToFields)
                        .flatMap(objectField -> objectField.fields.stream())
                        .collect(Collectors.toSet()))
        ));
    }

    Map<Object, Set<Field>> mapConstraintsToFields(DecisionTree decisionTree){
        return mapConstraintToFields(decisionTree.getRootNode())
            .collect(
                Collectors.toMap(
                map -> map.constraint,
                map -> map.fields
            ));
    }
}

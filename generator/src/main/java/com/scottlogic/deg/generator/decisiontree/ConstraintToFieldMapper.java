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
                    // TODO: This will only produce mappings from the root decisions/constraints. (Technically all we need, but investigate the below if needed)
//                        .flatMap(map -> Stream.of(
//                            map.fields, // this part is technically not used, but no reason not to keep it
//                            new ConstraintToFields(decision, map.fields)
//                        ))
        ));
    }

//    private Stream<Field> mapConstraintToFields(ConstraintNode node) {
//        return Stream.concat(
//            node.getAtomicConstraints()
//                .stream()
//                .map(constraintSniffer::detectField),
//            node.getDecisions()
//                .stream()
//                .flatMap(decision -> decision.getOptions()
//                    .stream()
//                    .flatMap(this::mapConstraintToFields)));
//    }

    Map<Object, Set<Field>> mapConstraintsToFields(DecisionTree decisionTree){
        return Stream.concat(
                mapConstraintToFields(decisionTree.getRootNode()),
                Stream.of(new ConstraintToFields(decisionTree.getRootNode(), decisionTree.getFields().stream().collect(Collectors.toSet()))))
            .collect(
                Collectors.toMap(
                map -> map.constraint,
                map -> map.fields
            ));
    }
}

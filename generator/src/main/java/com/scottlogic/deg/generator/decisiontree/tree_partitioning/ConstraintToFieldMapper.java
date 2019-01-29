package com.scottlogic.deg.generator.decisiontree.tree_partitioning;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Given a decision tree, find which constraints and decisions act on which fields and return a map from them to fields
 */
class ConstraintToFieldMapper {

    private class ConstraintToFields {
        public RootLevelConstraint constraint;
        public Set<Field> fields;

        ConstraintToFields(RootLevelConstraint constraint, Set<Field> fields) {
            this.constraint = constraint;
            this.fields = fields;
        }

        ConstraintToFields(RootLevelConstraint constraint, Field field) {
            this.constraint = constraint;
            this.fields = Collections.singleton(field);
        }
    }

    private Stream<ConstraintToFields> mapConstraintToFields(ConstraintNode node) {
        return Stream.concat(
            node.getAtomicConstraints()
                .stream()
                .map(constraint -> new ConstraintToFields(new RootLevelConstraint(constraint), constraint.getField())),
            node.getDecisions()
                .stream()
                .map(decision -> new ConstraintToFields(
                    new RootLevelConstraint(decision),
                    FlatMappingSpliterator.flatMap(
                        FlatMappingSpliterator.flatMap(decision
                            .getOptions()
                            .stream(),
                            this::mapConstraintToFields),
                        objectField -> objectField.fields.stream())
                        .collect(Collectors.toSet()))
                ));
    }

    Map<RootLevelConstraint, Set<Field>> mapConstraintsToFields(DecisionTree decisionTree){
        return mapConstraintToFields(decisionTree.getRootNode())
            .collect(
                Collectors.toMap(
                    map -> map.constraint,
                    map -> map.fields
                ));
    }
}

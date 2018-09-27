package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeProfile;
import com.scottlogic.deg.generator.reducer.ConstraintFieldSniffer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Given a rule or list of rules it finds which rules act on which fields and returns a map of rules to fields
 */
public class FieldMapper {

    private class ObjectFields {
        public Object object;
        public Set<Field> fields;

        ObjectFields(Object object, Set<Field> fields) {
            this.object = object;
            this.fields = fields;
        }

        ObjectFields(Object object, Field field) {
            this.object = object;
            this.fields = Collections.singleton(field);
        }
    }

    private final ConstraintFieldSniffer constraintSniffer = new ConstraintFieldSniffer();

    private Stream<ObjectFields> mapConstraintToFields(ConstraintNode node) {
        return Stream.concat(
            node.getAtomicConstraints()
                .stream()
                .map(constraint -> new ObjectFields(constraint, constraintSniffer.detectField(constraint))),
            node.getDecisions()
                .stream()
                .map(decision -> new ObjectFields(
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
//                            new ObjectFields(decision, map.fields)
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

    public Map<Object, Set<Field>> mapRulesToFields(DecisionTreeProfile profile){
        return mapConstraintToFields(profile.getRootNode())
            .collect(Collectors.toMap(
                map -> map.object,
                map -> map.fields
            ));
    }
}

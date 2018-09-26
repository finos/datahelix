package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeProfile;
import com.scottlogic.deg.generator.utils.ConcatenatingIterable;
import com.scottlogic.deg.generator.utils.ProjectingIterable;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Given a profile and mapping from rules to fields, split the tree into multiple trees based on which rules affect which fields
 */
public class TreePartitioner {
    public Stream<ConstraintNode> splitTreeIntoPartitions(ConstraintNode rootNode, Map<Object, List<Field>> mapping) {
        // Easy but stupid way:
        //      we split the root node into a list of constraint nodes
        //      a constraint node for every constraint and for every decision
        //      then we can run the mapper over it to map each constraint node to a field, like we did when we were operating on rules
        // Less stupid way:
        //      so first we need to get the field mappings from constraint/decision to field
        //      I guess that would be a Map<Object, List<Field>>, where the key can be either IConstraint or DecisionNode
        //      Then in the loop below we iterate over a concatenated stream of the fields coming from constraints and decisions
        //      And partition them as we do.
        // tomorrow I will do this
        final Map<Field, Integer> partitionsByField = new HashMap<>();
        final Map<Integer, List<Field>> partitionsById = new HashMap<>();

        int partitionCount = 0;

        ConcatenatingIterable<Object> fieldedObjects = new ConcatenatingIterable<>(
            new ProjectingIterable<>(rootNode.getAtomicConstraints(), constraint -> constraint),
            new ProjectingIterable<>(rootNode.getDecisions(), decision -> decision)
        );

        for (Object fieldedObject : fieldedObjects) {
            final List<Field> fields = mapping.get(fieldedObject);

            final List<Integer> partitionsTouched = fields
                .stream()
                .map(partitionsByField::get)
                .distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            final List<Field> fieldsToPartition = partitionsTouched.size() <= 1
                ? fields
                : Stream.concat(
                fields.stream(),
                partitionsTouched
                    .stream()
                    .flatMap(id -> partitionsById.get(id).stream()))
                .collect(Collectors.toList());

            final int currentPartition = partitionsTouched.size() == 1
                ? partitionsTouched.get(0)
                : partitionCount++;

            partitionsById.put(currentPartition, fieldsToPartition);
            fieldsToPartition.forEach(field -> partitionsByField.put(field, currentPartition));
        }

        Map<Integer, List<IConstraint>> partitionedConstraints = rootNode
            .getAtomicConstraints()
            .stream()
            .collect(Collectors.groupingBy(constraint -> partitionsByField.get(mapping.get(constraint).get(0))));

        Map<Integer, List<DecisionNode>> partitionedDecisions = rootNode
            .getDecisions()
            .stream()
            .collect(Collectors.groupingBy(decision -> partitionsByField.get(mapping.get(decision).get(0))));

        return partitionsById
            .keySet()
            .stream()
            .map(id -> new ConstraintNode( // TODO: consider moving this class to decision tree so we don't have to make that constructor public
                partitionedConstraints.getOrDefault(id, Collections.emptyList()),
                partitionedDecisions.getOrDefault(id, Collections.emptyList())
            ));

//        return partitionsById
//            .keySet()
//            .stream()
//            .map(partitionId -> {
//
//            });
    }
//            .reduce(
//                new RuleDecisionTree("", new ConstraintNode()),
//                (accumulator, nextRule) -> {
//                    return Stream.of(nextRule);
//                },
//                rules -> new DecisionTreeProfile(profile.getFields(), rules.collect(Collectors.toList())),
//                rules -> rules);




//    public Stream<DecisionTreeProfile> splitTreeIntoPartitionsOld(DecisionTreeProfile profile, Map<RuleDecisionTree, List<Field>> ruleFieldMapping) {
//
//        final Map<Field, Integer> partitionsByField = new HashMap<>();
//        final Map<Integer, List<Field>> partitionsById = new HashMap<>();
//
//        int partitionCount = 0;
//
//        for (RuleDecisionTree rule : profile.getDecisionTrees()) {
//            final List<Field>  fields = ruleFieldMapping.get(rule);
//
//            final List<Integer> partitionsTouched = fields
//                .stream()
//                .map(partitionsByField::get)
//                .distinct()
//                .collect(Collectors.toList());
//
//            final List<Field> fieldsToPartition = partitionsTouched.size() <= 1
//                ? fields
//                : Stream.concat(
//                        fields.stream(),
//                        partitionsTouched
//                            .stream()
//                            .flatMap(id -> partitionsById.get(id).stream()))
//                    .collect(Collectors.toList());
//
//            final int currentPartition = partitionsTouched.size() == 1
//                ? partitionsTouched.get(0)
//                : partitionCount++;
//
//            partitionsById.put(currentPartition, fieldsToPartition);
//            fieldsToPartition.forEach(field -> partitionsByField.put(field, currentPartition));
//        }
//
//        return null;
////            .reduce(
////                new RuleDecisionTree("", new ConstraintNode()),
////                (accumulator, nextRule) -> {
////                    return Stream.of(nextRule);
////                },
////                rules -> new DecisionTreeProfile(profile.getFields(), rules.collect(Collectors.toList())),
////                rules -> rules);
//    }
//}
}

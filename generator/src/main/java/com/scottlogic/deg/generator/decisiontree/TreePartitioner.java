package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.utils.ConcatenatingIterable;
import com.scottlogic.deg.generator.utils.ProjectingIterable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Given a decision tress, split it into multiple trees based on which constraints and decisions affect which fields
 */
public class TreePartitioner implements ITreePartitioner{
    private final ConstraintToFieldMapper fieldMapper;

    public TreePartitioner() {
        fieldMapper = new ConstraintToFieldMapper();
    }

    public Stream<DecisionTree> splitTreeIntoPartitions(DecisionTree decisionTree) {
        // a mapping from root-level constraints/decisions to the fields they affect
        final Map<Object, Set<Field>> mapping = fieldMapper.mapConstraintsToFields(decisionTree);

        final Map<Field, Integer> partitionsByField = new HashMap<>();
        final Map<Integer, Set<Field>> partitionsById = new HashMap<>();

        int partitionCount = 0;

        // each set of fields iterated here are constrained by a single root-level constraint/decision
        for (Set<Field> fields : mapping.values()) {
            // find which existing partitions this constraint/decision affects (if any)
            final List<Integer> partitionsTouched = fields
                .stream()
                .map(partitionsByField::get)
                .distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            // if there aren't any, we start a new partition,
            // otherwise we gather up all the fields in the touched partitions and move them to a new one
            final Set<Field> fieldsToPartition = partitionsTouched.size() == 0
                ? fields
                : Stream.concat(
                        fields.stream(),
                        partitionsTouched
                            .stream()
                            .flatMap(id -> partitionsById.get(id).stream()))
                    .collect(Collectors.toSet());

            // we can reuse the partition ID our new constraint fits exactly into an existing partition
            final int currentPartition = partitionsTouched.size() == 1
                ? partitionsTouched.get(0)
                : partitionCount++;

            // TODO: write test for this
            // if partitions are being merged, remove the old ones
            if (partitionsTouched.size() > 1)
                partitionsTouched.forEach(partitionsById::remove);

            // create/update partitions
            partitionsById.put(currentPartition, fieldsToPartition);
            fieldsToPartition.forEach(field -> partitionsByField.put(field, currentPartition));
        }

        // take all the root level constraints and group them by what partition they're in...
        final Map<Integer, List<IConstraint>> partitionedConstraints = decisionTree.getRootNode()
            .getAtomicConstraints()
            .stream()
            .collect(Collectors.groupingBy(
                constraint ->
                    partitionsByField.get(
                        mapping.get(constraint).iterator().next())));

        // ...same with root level decisions
        final Map<Integer, List<DecisionNode>> partitionedDecisions = decisionTree
            .getRootNode()
            .getDecisions()
            .stream()
            .collect(Collectors.groupingBy(
                decision ->
                    partitionsByField.get(
                        mapping.get(decision).iterator().next())));

        return partitionsById
            .keySet()
            .stream()
            .map(id -> new DecisionTree(
                new ConstraintNode(
                    partitionedConstraints.getOrDefault(id, Collections.emptyList()),
                    partitionedDecisions.getOrDefault(id, Collections.emptyList())
                ),
                new ProfileFields(new ArrayList<>(partitionsById.get(id))
            )));
    }
}

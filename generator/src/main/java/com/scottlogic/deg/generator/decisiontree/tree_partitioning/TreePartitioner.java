package com.scottlogic.deg.generator.decisiontree.tree_partitioning;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Given a decision tress, split it into multiple trees based on which constraints and decisions affect which fields
 */
public class TreePartitioner implements ITreePartitioner {
    private final ConstraintToFieldMapper fieldMapper;

    public TreePartitioner() {
        fieldMapper = new ConstraintToFieldMapper();
    }

    public Stream<DecisionTree> splitTreeIntoPartitions(DecisionTree decisionTree) {
        final PartitionIndex partitions = new PartitionIndex();

        final Map<RootLevelConstraint, Set<Field>> mapping = fieldMapper.mapConstraintsToFields(decisionTree);

        // each set of fields iterated here are constrained by a single root-level constraint/decision
        for (RootLevelConstraint constraint : mapping.keySet()) {
            Set<Field> fields = mapping.get(constraint);

            // find which existing partitions this constraint/decision affects (if any)
            final Set<UUID> existingIntersectingPartitions = fields
                .stream()
                .map(partitions::getPartitionId)
                .distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

            final UUID partitionId = partitions.addPartition(fields, new HashSet<>(Collections.singletonList(constraint)));

            if (existingIntersectingPartitions.size() > 0) {
                final Set<UUID> partitionsToMerge = new HashSet<>();
                partitionsToMerge.add(partitionId);
                partitionsToMerge.addAll(existingIntersectingPartitions);

                partitions.mergePartitions(partitionsToMerge);
            }

            // TODO: write test for this
            // if partitions are being merged, remove the old ones
        }

        final Stream<Field> unpartitionedFields = decisionTree
            .getFields()
            .stream()
            .filter(field -> Objects.isNull(partitions.getPartitionId(field)));

        return Stream.concat(
            partitions
                .getPartitions()
                .stream()
                .map(partition -> new DecisionTree(
                    new ConstraintNode(partition.getAtomicConstraints(), partition.getDecisionNodes()),
                    new ProfileFields(new ArrayList<>(partition.fields))
                )),
            unpartitionedFields
                .map(field -> new DecisionTree(
                    new ConstraintNode(),
                    new ProfileFields(Collections.singletonList(field))
                ))
            );
    }

    class Partition {
        final UUID id;
        final Set<Field> fields;
        final Set<RootLevelConstraint> constraints;

        Partition(UUID id, Set<Field> fields, Set<RootLevelConstraint> constraints) {
            this.id = id;
            this.fields = fields;
            this.constraints = constraints;
        }

        Set<IConstraint> getAtomicConstraints() {
            return constraints
                .stream()
                .map(RootLevelConstraint::getAtomicConstraint)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        }

        Set<DecisionNode> getDecisionNodes() {
            return constraints
                .stream()
                .map(RootLevelConstraint::getDecisionNode)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        }
    }

    class PartitionIndex {
        private final Map<UUID, Partition> idToPartition = new HashMap<>();
        private final Map<Field, Partition> fieldsToPartition = new HashMap<>();

        UUID addPartition(Set<Field> fields, Set<RootLevelConstraint> constraints) {
            final Partition newPartition = new Partition(
                java.util.UUID.randomUUID(),
                fields,
                constraints);

            idToPartition.put(newPartition.id, newPartition);

            for (Field field : fields)
                fieldsToPartition.put(field, newPartition);

            return newPartition.id;
        }

        UUID mergePartitions(Set<UUID> ids) {
            final Set<Partition> partitions = ids
                .stream()
                .map(idToPartition::get)
                .collect(Collectors.toSet());

            final Set<Field> fields = getFromAllPartitions(partitions, partition -> partition.fields);
            final Set<RootLevelConstraint> constraints = getFromAllPartitions(partitions, partition -> partition.constraints);

            final Partition newPartition = new Partition(
                java.util.UUID.randomUUID(),
                fields,
                constraints);
            idToPartition.put(newPartition.id, newPartition);
            fields.forEach(field -> fieldsToPartition.put(field, newPartition));

            ids.forEach(idToPartition::remove);

            return newPartition.id;
        }

        private <T> Set<T> getFromAllPartitions(Set<Partition> partitions, Function<Partition, Set<T>> getter) {
            return partitions
                .stream()
                .flatMap(partition -> getter.apply(partition).stream())
                .collect(Collectors.toSet());
        }

        UUID getPartitionId(Field field) {
            return fieldsToPartition.containsKey(field)
                ? fieldsToPartition.get(field).id
                : null;
        }

        Collection<Partition> getPartitions() {
            return idToPartition.values();
        }
    }
}

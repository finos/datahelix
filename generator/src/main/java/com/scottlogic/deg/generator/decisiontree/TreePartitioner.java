package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
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
        final BasePartitionIndex partitionIndex = new BasePartitionIndex();

        partitionParts(
            fieldMapper.mapDecisionsToFields(decisionTree),
            partitionIndex.asDecisionNodePartitionIndex());

        partitionParts(
            fieldMapper.mapConstraintsToFields(decisionTree),
            partitionIndex.asConstraintPartitionIndex());

        return partitionIndex
            .getPartitions()
            .stream()
            .map(partition -> new DecisionTree(
                new ConstraintNode(
                    partition.constraints,
                    partition.decisions
                ),
                new ProfileFields(new ArrayList<>(partition.fields))
            ));
    }

    <T> void partitionParts(Map<T, Set<Field>> mapping, PartitionIndex<T> partitions) {
        // each set of fields iterated here are constrained by a single root-level constraint/decision
        for (T constraint : mapping.keySet()) {
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
    }

    interface PartitionIndex<T> {
        UUID addPartition(Set<Field> fields, Set<T> constraints);

        UUID mergePartitions(Set<UUID> partitions);

        UUID getPartitionId(Field field);
    }

    class BasePartitionIndex {
        private final Map<UUID, Partition> idToPartition = new HashMap<>();
        private final Map<Field, Partition> fieldsToPartition = new HashMap<>();

        UUID addPartition(Set<Field> fields, Set<IConstraint> constraints, Set<DecisionNode> decisions) {
            final Partition newPartition = new Partition(
                java.util.UUID.randomUUID(),
                fields,
                constraints,
                decisions
            );

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
            final Set<IConstraint> constraints = getFromAllPartitions(partitions, partition -> partition.constraints);
            final Set<DecisionNode> decisions = getFromAllPartitions(partitions, partition -> partition.decisions);

            final Partition newPartition = new Partition(
                java.util.UUID.randomUUID(),
                fields,
                constraints,
                decisions);
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

        PartitionIndex<IConstraint> asConstraintPartitionIndex() {
            return new ConstraintPartitionIndex(this);
        }

        PartitionIndex<DecisionNode> asDecisionNodePartitionIndex() {
            return new DecisionPartitionIndex(this);
        }

        class DecisionPartitionIndex implements PartitionIndex<DecisionNode> {
            final BasePartitionIndex partitionIndex;

            DecisionPartitionIndex(BasePartitionIndex partitionIndex) {
                this.partitionIndex = partitionIndex;
            }

            @Override
            public UUID addPartition(Set<Field> fields, Set<DecisionNode> constraints) {
                return partitionIndex.addPartition(fields, Collections.emptySet(), constraints);
            }

            @Override
            public UUID mergePartitions(Set<UUID> partitions) {
                return partitionIndex.mergePartitions(partitions);
            }

            @Override
            public UUID getPartitionId(Field field) {
                return partitionIndex.getPartitionId(field);
            }
        }

        class ConstraintPartitionIndex implements PartitionIndex<IConstraint> {
            final BasePartitionIndex partitionIndex;

            ConstraintPartitionIndex(BasePartitionIndex partitionIndex) {
                this.partitionIndex = partitionIndex;
            }

            @Override
            public UUID addPartition(Set<Field> fields, Set<IConstraint> constraints) {
                return partitionIndex.addPartition(fields, constraints, Collections.emptySet());
            }

            @Override
            public UUID mergePartitions(Set<UUID> partitions) {
                return partitionIndex.mergePartitions(partitions);
            }

            @Override
            public UUID getPartitionId(Field field) {
                return partitionIndex.getPartitionId(field);
            }
        }
    }

    class Partition {
        final UUID id;
        final Set<Field> fields;
        final Set<DecisionNode> decisions;
        final Set<IConstraint> constraints;

        Partition(UUID id, Set<Field> fields, Set<IConstraint> constraints, Set<DecisionNode> decisions) {
            this.id = id;
            this.fields = fields;
            this.decisions = decisions;
            this.constraints = constraints;
        }
    }
}

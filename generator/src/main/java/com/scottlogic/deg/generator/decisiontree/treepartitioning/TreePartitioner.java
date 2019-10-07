/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.generator.decisiontree.treepartitioning;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.common.util.FlatMappingSpliterator;
import com.scottlogic.deg.generator.decisiontree.ConstraintNodeBuilder;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Given a decision tress, split it into multiple trees based on which constraints and decisions affect which fields
 */
public class TreePartitioner {
    private final ConstraintToFieldMapper fieldMapper;
    private static Integer partitionIndex = 0;

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
            final Set<Integer> existingIntersectingPartitions = fields
                .stream()
                .map(partitions::getPartitionId)
                .distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

            // then, add new partition for this new constraint
            final Integer partitionId = partitions.addPartition(fields, new HashSet<>(Collections.singletonList(constraint)));

            // if there are any intersecting partitions, merge them with the new one
            if (existingIntersectingPartitions.size() > 0) {
                final Set<Integer> partitionsToMerge = new HashSet<>();
                partitionsToMerge.add(partitionId);
                partitionsToMerge.addAll(existingIntersectingPartitions);

                partitions.mergePartitions(partitionsToMerge);
            }
        }

        // any leftover fields must be grouped into their own partition
        final Stream<Field> unpartitionedFields = decisionTree
            .getFields()
            .stream()
            .filter(field -> Objects.isNull(partitions.getPartitionId(field)));

        return Stream.concat(
            partitions
                .getPartitions()
                .stream()
                .sorted(Comparator.comparingInt(p -> p.id))
                .map(partition -> new DecisionTree(
                    new ConstraintNodeBuilder()
                        .addAtomicConstraints(partition.getAtomicConstraints())
                        .addRelations(partition.getRelations())
                        .setDecisions(partition.getDecisionNodes())
                        .build(),
                    new ProfileFields(new ArrayList<>(partition.fields))
                )),
            unpartitionedFields
                .map(field -> new DecisionTree(
                    new ConstraintNodeBuilder().build(),
                    new ProfileFields(Collections.singletonList(field))
                ))
            );
    }

    static class Partition {
        final Integer id;
        final Set<Field> fields;
        final Set<RootLevelConstraint> constraints;

        Partition(Integer id, Set<Field> fields, Set<RootLevelConstraint> constraints) {
            this.id = id;
            this.fields = fields;
            this.constraints = constraints;
        }

        Set<AtomicConstraint> getAtomicConstraints() {
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

        Set<FieldSpecRelations> getRelations() {
            return constraints
                .stream()
                .map(RootLevelConstraint::getRelations)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        }
    }

    static class PartitionIndex {
        private final Map<Integer, Partition> idToPartition = new HashMap<>();
        private final Map<Field, Partition> fieldsToPartition = new HashMap<>();

        Integer addPartition(Set<Field> fields, Set<RootLevelConstraint> constraints) {
            final Partition newPartition = new Partition(
                partitionIndex++,
                fields,
                constraints);

            idToPartition.put(newPartition.id, newPartition);

            for (Field field : fields)
                fieldsToPartition.put(field, newPartition);

            return newPartition.id;
        }

        void mergePartitions(Set<Integer> ids) {
            final Set<Partition> partitions = ids
                .stream()
                .map(idToPartition::get)
                .collect(Collectors.toSet());

            final Set<Field> fields = getFromAllPartitions(partitions, partition -> partition.fields);
            final Set<RootLevelConstraint> constraints = getFromAllPartitions(partitions, partition -> partition.constraints);

            final Partition newPartition = new Partition(
                partitionIndex++,
                fields,
                constraints);
            idToPartition.put(newPartition.id, newPartition);
            fields.forEach(field -> fieldsToPartition.put(field, newPartition));

            ids.forEach(idToPartition::remove);
        }

        private <T> Set<T> getFromAllPartitions(Set<Partition> partitions, Function<Partition, Set<T>> getter) {
            return FlatMappingSpliterator.flatMap(partitions
                .stream(),
                partition -> getter.apply(partition).stream())
                .collect(Collectors.toSet());
        }

        Integer getPartitionId(Field field) {
            return fieldsToPartition.containsKey(field)
                ? fieldsToPartition.get(field).id
                : null;
        }

        Collection<Partition> getPartitions() {
            return idToPartition.values();
        }
    }
}

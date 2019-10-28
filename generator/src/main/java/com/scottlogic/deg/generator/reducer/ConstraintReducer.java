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

package com.scottlogic.deg.generator.reducer;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.*;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ConstraintReducer {
    private final FieldSpecMerger fieldSpecMerger;

    @Inject
    public ConstraintReducer(
        FieldSpecMerger fieldSpecMerger
    ) {
        this.fieldSpecMerger = fieldSpecMerger;
    }

    public Optional<RowSpec> reduceConstraintsToRowSpec(ProfileFields fields, ConstraintNode node) {
        Set<AtomicConstraint> constraints = node.getAtomicConstraints();
        Set<FieldSpecRelations> relations = node.getRelations();

        final Map<Field, Set<AtomicConstraint>> fieldToConstraints = constraints.stream()
            .collect(
                Collectors.groupingBy(
                    AtomicConstraint::getField,
                    Collectors.mapping(Function.identity(),
                        Collectors.toSet())));

        final Map<Field, Optional<FieldSpec>> fieldToFieldSpec = fields.stream()
            .collect(
                Collectors.toMap(
                    Function.identity(),
                    field -> reduceConstraintsToFieldSpec(field, fieldToConstraints.get(field))));

        final Optional<Map<Field, FieldSpec>> optionalMap = Optional.of(fieldToFieldSpec)
            .filter(map -> map.values().stream().allMatch(Optional::isPresent))
            .map(map -> map
                .entrySet()
                .stream()
                .collect(
                    Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().get())));

        return optionalMap.map(
            map -> new RowSpec(
                fields,
                map,
                new ArrayList<>(relations)));
    }

    public Optional<FieldSpec> reduceConstraintsToFieldSpec(Field field, Iterable<AtomicConstraint> constraints) {
        return constraints == null
            ? Optional.of(FieldSpecFactory.fromType(field.getType()))
            : getRootFieldSpec(field, constraints);
    }

    private Optional<FieldSpec> getRootFieldSpec(Field field, Iterable<AtomicConstraint> rootConstraints) {
        final Stream<FieldSpec> rootConstraintsStream =
            StreamSupport
                .stream(rootConstraints.spliterator(), false)
                .map(AtomicConstraint::toFieldSpec);

        return rootConstraintsStream
            .map(Optional::of)
            .reduce(
                Optional.of(FieldSpecFactory.fromType(field.getType())),
                (optSpec1, optSpec2) -> optSpec1.flatMap(
                    spec1 -> optSpec2.flatMap(
                        spec2 -> fieldSpecMerger.merge(spec1, spec2))));
    }
}

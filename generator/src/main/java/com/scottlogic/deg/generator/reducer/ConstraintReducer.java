package com.scottlogic.deg.generator.reducer;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ConstraintReducer {
    private final FieldSpecFactory fieldSpecFactory;
    private final FieldSpecMerger fieldSpecMerger;

    @Inject
    public ConstraintReducer(
            FieldSpecFactory fieldSpecFactory,
            FieldSpecMerger fieldSpecMerger
    ) {
        this.fieldSpecFactory = fieldSpecFactory;
        this.fieldSpecMerger = fieldSpecMerger;
    }

    public Optional<RowSpec> reduceConstraintsToRowSpec(ProfileFields fields, Iterable<AtomicConstraint> constraints) {
        final Map<Field, List<AtomicConstraint>> fieldToConstraints = StreamSupport
            .stream(constraints.spliterator(), false)
            .collect(
                Collectors.groupingBy(
                    AtomicConstraint::getField,
                    Collectors.mapping(Function.identity(),
                        Collectors.toList())));

        final Map<Field, Optional<FieldSpec>> fieldToFieldSpec = fields.stream()
            .collect(
                Collectors.toMap(
                    Function.identity(),
                    field ->  reduceConstraintsToFieldSpec(fieldToConstraints.get(field))));

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
                map));
    }

    public Optional<FieldSpec> reduceConstraintsToFieldSpec(Iterable<AtomicConstraint> constraints) {
        return constraints == null
                ? Optional.of(FieldSpec.Empty)
                : getRootFieldSpec(constraints);
    }

    public Optional<FieldSpec> reduceConstraintsToFieldSpecWithMustContains(Iterable<AtomicConstraint> rootConstraints,
                                                            Set<FieldSpec> decisionFieldSpecs) {

        Optional<FieldSpec> rootFieldSpec = reduceConstraintsToFieldSpec(rootConstraints);

        if (decisionFieldSpecs.isEmpty()) { return rootFieldSpec; }

        return Optional.of(fieldSpecFactory.toMustContainRestrictionFieldSpec(
            rootFieldSpec.orElse(FieldSpec.Empty),
            StreamSupport.stream(decisionFieldSpecs.spliterator(), false).collect(Collectors.toSet())
        ));
    }

    private Optional<FieldSpec> getRootFieldSpec(Iterable<AtomicConstraint> rootConstraints) {
        final Stream<FieldSpec> rootConstraintsStream =
            StreamSupport
                .stream(rootConstraints.spliterator(), false)
                .map(fieldSpecFactory::construct);

        return rootConstraintsStream
            .reduce(
                Optional.of(FieldSpec.Empty),
                (optAcc, next) ->
                    optAcc.flatMap(acc -> fieldSpecMerger.merge(acc, next)),
                (optAcc1, optAcc2) -> optAcc1.flatMap(
                    acc1 -> optAcc2.flatMap(
                        acc2 -> fieldSpecMerger.merge(acc1, acc2))));
    }
}

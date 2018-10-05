package com.scottlogic.deg.generator.reducer;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpec;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ConstraintReducer {
    private final ConstraintFieldSniffer constraintFieldSniffer = new ConstraintFieldSniffer();
    private final FieldSpecFactory fieldSpecFactory;
    private final FieldSpecMerger fieldSpecMerger;

    public ConstraintReducer(
            FieldSpecFactory fieldSpecFactory,
            FieldSpecMerger fieldSpecMerger
    ) {
        this.fieldSpecFactory = fieldSpecFactory;
        this.fieldSpecMerger = fieldSpecMerger;
    }

    public Optional<RowSpec> reduceConstraintsToRowSpec(ProfileFields fields, Iterable<IConstraint> constraints) {
        final Map<Field, List<IConstraint>> fieldToConstraints = StreamSupport
            .stream(constraints.spliterator(), false)
            .map(constraintFieldSniffer::generateTuple)
            .collect(
                Collectors.groupingBy(
                    ConstraintAndFieldTuple::getField, // map from field...
                    Collectors.mapping( // ...to a list of constraints
                        ConstraintAndFieldTuple::getConstraint,
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

    private Optional<FieldSpec> reduceConstraintsToFieldSpec(Iterable<IConstraint> constraints) {
        if (constraints == null) {
            return Optional.of(new FieldSpec());
        }

        return StreamSupport
            .stream(constraints.spliterator(), false)
            .map(fieldSpecFactory::construct)
            .reduce(
                Optional.of(new FieldSpec()),
                (optAcc, next) ->
                    optAcc.flatMap(acc -> fieldSpecMerger.merge(acc, next)),
                (optAcc1, optAcc2) -> optAcc1.flatMap(
                    acc1 -> optAcc2.flatMap(
                        acc2 -> fieldSpecMerger.merge(acc1, acc2))));
    }
}

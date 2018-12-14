package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;

import java.util.Optional;

public class DateTimeRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final DateTimeRestrictionsMerger dateTimeRestrictionsMerger = new DateTimeRestrictionsMerger();

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged) {
        MergeResult<DateTimeRestrictions> mergeResult = dateTimeRestrictionsMerger.merge(
            left.getDateTimeRestrictions(), right.getDateTimeRestrictions());

        if (!mergeResult.successful) {
            return Optional.empty();
        }

        if (mergeResult.restrictions == null) {
            return Optional.of(merged.withDateTimeRestrictions(
                null,
                FieldSpecSource.Empty));
        }

        TypeRestrictions typeRestrictions = merged.getTypeRestrictions();
        if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.TEMPORAL)) {
            return Optional.empty();
        }

        return Optional.of(merged
            .withDateTimeRestrictions(
                mergeResult.restrictions,
                FieldSpecSource.fromFieldSpecs(left, right))
            .withTypeRestrictions(
                DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.TEMPORAL),
                FieldSpecSource.fromFieldSpecs(left, right)));
    }
}


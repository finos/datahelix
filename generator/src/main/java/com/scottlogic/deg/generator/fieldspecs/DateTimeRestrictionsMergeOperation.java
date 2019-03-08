package com.scottlogic.deg.generator.fieldspecs;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.Optional;

public class DateTimeRestrictionsMergeOperation implements RestrictionMergeOperation {
    private final DateTimeRestrictionsMerger merger;

    @Inject
    public DateTimeRestrictionsMergeOperation(DateTimeRestrictionsMerger merger) {
        this.merger = merger;
    }

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        DateTimeRestrictions dateTimeRestrictions = merger.merge(
            left.getDateTimeRestrictions(), right.getDateTimeRestrictions());

        if (dateTimeRestrictions == null) {
            return Optional.of(merging.withDateTimeRestrictions(
                null,
                FieldSpecSource.Empty));
        }

        return Optional.of(merging
            .withDateTimeRestrictions(
                dateTimeRestrictions,
                FieldSpecSource.fromFieldSpecs(left, right)));
    }
}

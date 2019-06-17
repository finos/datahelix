package com.scottlogic.deg.generator.fieldspecs;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.Optional;

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.DATETIME;
import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.STRING;

public class DateTimeRestrictionsMergeOperation implements RestrictionMergeOperation {
    private final DateTimeRestrictionsMerger merger;

    @Inject
    public DateTimeRestrictionsMergeOperation(DateTimeRestrictionsMerger merger) {
        this.merger = merger;
    }

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        if (!merging.isTypeAllowed(DATETIME)){
            return Optional.of(merging);
        }

        MergeResult<DateTimeRestrictions> mergeResult = merger.merge(
            left.getDateTimeRestrictions(), right.getDateTimeRestrictions());

        if (!mergeResult.successful){
            return Optional.of(merging.withoutType(DATETIME));
        }

        if (mergeResult.restrictions == null) {
            return Optional.of(merging); //no change
        }

        return Optional.of(merging
            .withDateTimeRestrictions(
                mergeResult.restrictions));
    }
}

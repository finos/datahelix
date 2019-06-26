package com.scottlogic.deg.generator.fieldspecs;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.Optional;

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.DATETIME;

public class DateTimeRestrictionsMergeOperation implements RestrictionMergeOperation {
    private final DateTimeRestrictionsMerger merger;

    @Inject
    public DateTimeRestrictionsMergeOperation(DateTimeRestrictionsMerger merger) {
        this.merger = merger;
    }

    @Override
    public FieldSpec applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        if (!merging.isTypeAllowed(DATETIME)){
            return merging;
        }

        MergeResult<DateTimeRestrictions> mergeResult = merger.merge(
            left.getDateTimeRestrictions(), right.getDateTimeRestrictions());

        if (!mergeResult.successful){
            return merging.withoutType(DATETIME);
        }

        return merging.withDateTimeRestrictions(mergeResult.restrictions);
    }
}

package com.scottlogic.deg.generator.fieldspecs;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.Optional;

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.NUMERIC;

public class NumericRestrictionsMergeOperation implements RestrictionMergeOperation {
    private final NumericRestrictionsMerger merger;

    @Inject
    public NumericRestrictionsMergeOperation(NumericRestrictionsMerger merger) {
        this.merger = merger;
    }

    @Override
    public FieldSpec applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        if (!merging.isTypeAllowed(NUMERIC)){
            return merging;
        }

        MergeResult<NumericRestrictions> mergeResult = merger.merge(
            left.getNumericRestrictions(), right.getNumericRestrictions());

        if (!mergeResult.successful) {
            return merging.withoutType(NUMERIC);
        }

        return merging.withNumericRestrictions(mergeResult.restrictions);
    }
}


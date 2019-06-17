package com.scottlogic.deg.generator.fieldspecs;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.Optional;

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.NUMERIC;
import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.STRING;

public class NumericRestrictionsMergeOperation implements RestrictionMergeOperation {
    private final NumericRestrictionsMerger merger;

    @Inject
    public NumericRestrictionsMergeOperation(NumericRestrictionsMerger merger) {
        this.merger = merger;
    }

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        if (!merging.isTypeAllowed(NUMERIC)){
            return Optional.of(merging);
        }

        MergeResult<NumericRestrictions> mergeResult = merger.merge(
            left.getNumericRestrictions(), right.getNumericRestrictions());

        if (!mergeResult.successful) {
            return Optional.of(merging.withoutType(NUMERIC));
        }

        NumericRestrictions numberRestrictions = mergeResult.restrictions;
        if (numberRestrictions == null) {
            return Optional.of(merging);
        }

        return Optional.of(merging
            .withNumericRestrictions(
                numberRestrictions));
    }
}


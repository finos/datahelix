package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.Optional;

public class NumericRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final NumericRestrictionsMerger numericRestrictionsMerger = new NumericRestrictionsMerger();

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        MergeResult<NumericRestrictions> mergeResult = numericRestrictionsMerger.merge(
            left.getNumericRestrictions(), right.getNumericRestrictions());

        if (!mergeResult.successful) {
            return Optional.empty();
        }

        NumericRestrictions numberRestrictions = mergeResult.restrictions;
        if (numberRestrictions == null) {
            return Optional.of(merging.withNumericRestrictions(
                null,
                FieldSpecSource.Empty));
        }

        return Optional.of(merging
            .withNumericRestrictions(
                numberRestrictions,
                FieldSpecSource.fromFieldSpecs(left, right)));
    }
}


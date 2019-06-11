package com.scottlogic.deg.generator.fieldspecs;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.Optional;

public class NumericRestrictionsMergeOperation implements RestrictionMergeOperation {
    private final NumericRestrictionsMerger merger;

    @Inject
    public NumericRestrictionsMergeOperation(NumericRestrictionsMerger merger) {
        this.merger = merger;
    }

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        MergeResult<NumericRestrictions> mergeResult = merger.merge(
            left.getNumericRestrictions(), right.getNumericRestrictions());

        if (!mergeResult.successful) {
            TypeRestrictions typeRestrictions = merging.getTypeRestrictions();
            if (typeRestrictions == null){
                typeRestrictions = DataTypeRestrictions.ALL_TYPES_PERMITTED;
            }

            return Optional.of(merging
                .withTypeRestrictions(
                    typeRestrictions.except(IsOfTypeConstraint.Types.NUMERIC)));
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


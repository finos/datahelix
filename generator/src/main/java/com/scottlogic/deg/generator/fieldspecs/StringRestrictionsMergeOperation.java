package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.Optional;

public class StringRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final StringRestrictionsMerger stringRestrictionsMerger = new StringRestrictionsMerger();

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        MergeResult<StringRestrictions> mergeResult = stringRestrictionsMerger.merge(
            left.getStringRestrictions(), right.getStringRestrictions());

        if (!mergeResult.successful) {
            return Optional.empty();
        }

        StringRestrictions stringRestrictions = mergeResult.restrictions;

        if (stringRestrictions == null) {
            return Optional.of(merging.withStringRestrictions(
                null,
                FieldSpecSource.Empty));
        }

        TypeRestrictions typeRestrictions = merging.getTypeRestrictions();
        if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.STRING)) {
            return Optional.empty();
        }

        return Optional.of(merging
            .withStringRestrictions(
                stringRestrictions,
                FieldSpecSource.fromFieldSpecs(left, right))
            .withTypeRestrictions(
                DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.STRING),
                FieldSpecSource.fromFieldSpecs(left, right)));
    }
}


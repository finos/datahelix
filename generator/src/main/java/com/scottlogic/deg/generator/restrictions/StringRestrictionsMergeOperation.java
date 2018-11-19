package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

import java.util.Optional;

public class StringRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final StringRestrictionsMerger stringRestrictionsMerger = new StringRestrictionsMerger();

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged) {
        MergeResult<StringRestrictions> mergeResult = stringRestrictionsMerger.merge(
            left.getStringRestrictions(), right.getStringRestrictions());

        if (!mergeResult.successful) {
            return Optional.empty();
        }

        StringRestrictions stringRestrictions = mergeResult.restrictions;

        if (stringRestrictions == null) {
            return Optional.of(merged.setStringRestrictions(null));
        }

        TypeRestrictions typeRestrictions = merged.getTypeRestrictions();
        if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.String)) {
            return Optional.empty();
        }

        return Optional.of(merged.setStringRestrictions(stringRestrictions).setTypeRestrictions(
            DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.String)));
    }
}


package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.restrictions.*;

import java.util.Optional;

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.STRING;

public class StringRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final StringRestrictionsMerger stringRestrictionsMerger = new StringRestrictionsMerger();

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        if (!merging.isTypeAllowed(STRING)){
            return Optional.of(merging);
        }

        MergeResult<StringRestrictions> mergeResult = stringRestrictionsMerger.merge(
            left.getStringRestrictions(), right.getStringRestrictions());

        if (!mergeResult.successful) {
            return Optional.of(merging.withoutType(STRING));
        }

        StringRestrictions stringRestrictions = mergeResult.restrictions;

        if (stringRestrictions == null) {
            return Optional.of(merging.withStringRestrictions(null));
        }

        return Optional.of(merging
            .withStringRestrictions(
                stringRestrictions));
    }
}


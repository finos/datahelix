package com.scottlogic.deg.generator.fieldspecs;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.Optional;

public class StringRestrictionsMergeOperation implements RestrictionMergeOperation {
    private final StringRestrictionsMerger merger;

    @Inject
    public StringRestrictionsMergeOperation(StringRestrictionsMerger merger) {
        this.merger = merger;
    }

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        MergeResult<StringRestrictions> mergeResult = merger.merge(
            left.getStringRestrictions(), right.getStringRestrictions());

        if (!mergeResult.successful) {
            return Optional.empty();
        }

        StringRestrictions stringRestrictions = mergeResult.restrictions;

        if (stringRestrictions == null) {
            return Optional.of(merging.withStringRestrictions(
                merging.getStringRestrictions(),
                FieldSpecSource.Empty));
        }

        return Optional.of(merging
            .withStringRestrictions(
                stringRestrictions,
                FieldSpecSource.fromFieldSpecs(left, right)));
    }
}


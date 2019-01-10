package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.restrictions.FormatRestrictionsMerger;

import java.util.Optional;

public class FormatRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final FormatRestrictionsMerger formatRestrictionMerger = new FormatRestrictionsMerger();

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        return Optional.of(merging.withFormatRestrictions(
            formatRestrictionMerger.merge(left.getFormatRestrictions(), right.getFormatRestrictions()),
            FieldSpecSource.fromFieldSpecs(left, right)));
    }
}


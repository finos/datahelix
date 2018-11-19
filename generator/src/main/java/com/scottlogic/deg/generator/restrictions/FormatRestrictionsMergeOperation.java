package com.scottlogic.deg.generator.restrictions;

public class FormatRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final FormatRestrictionsMerger formatRestrictionMerger = new FormatRestrictionsMerger();

    @Override
    public boolean applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged) {
        merged.setFormatRestrictions(
            formatRestrictionMerger.merge(left.getFormatRestrictions(), right.getFormatRestrictions()));
        return true;
    }
}


package com.scottlogic.deg.generator.restrictions;

public class FormatRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final FormatRestrictionsMerger formatRestrictionMerger = new FormatRestrictionsMerger();

    @Override
    public boolean successful(FieldSpec left, FieldSpec right, FieldSpec merged) {
        try {
            merged.setFormatRestrictions(
                formatRestrictionMerger.merge(left.getFormatRestrictions(), right.getFormatRestrictions()));

            return true;
        } catch (UnmergeableRestrictionException e) {
            return false;
        }
    }
}


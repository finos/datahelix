package com.scottlogic.deg.generator.restrictions;

public class NullRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final NullRestrictionsMerger nullRestrictionsMerger = new NullRestrictionsMerger();

    @Override
    public boolean successful(FieldSpec left, FieldSpec right, FieldSpec merged) {
        try {
            merged.setNullRestrictions(
                nullRestrictionsMerger.merge(left.getNullRestrictions(), right.getNullRestrictions()));

            return true;
        } catch (UnmergeableRestrictionException e) {
            return false;
        }
    }
}


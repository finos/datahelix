package com.scottlogic.deg.generator.restrictions;

public class GranularityRestrictionsMergeOperation implements RestrictionMergeOperation{
    private static final GranularityRestrictionsMerger granularityRestrictionsMerger = new GranularityRestrictionsMerger();

    @Override
    public boolean successful(FieldSpec left, FieldSpec right, FieldSpec merged) {
        try {
            merged.setGranularityRestrictions(
                granularityRestrictionsMerger.merge(left.getGranularityRestrictions(), right.getGranularityRestrictions()));

            return true;
        } catch (UnmergeableRestrictionException e) {
            return false;
        }
    }
}

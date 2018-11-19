package com.scottlogic.deg.generator.restrictions;

public class GranularityRestrictionsMergeOperation implements RestrictionMergeOperation{
    private static final GranularityRestrictionsMerger granularityRestrictionsMerger = new GranularityRestrictionsMerger();

    @Override
    public boolean applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged) {
        merged.setGranularityRestrictions(
            granularityRestrictionsMerger.merge(left.getGranularityRestrictions(), right.getGranularityRestrictions()));

        return true;
    }
}

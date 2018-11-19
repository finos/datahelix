package com.scottlogic.deg.generator.restrictions;

public class NullRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final NullRestrictionsMerger nullRestrictionsMerger = new NullRestrictionsMerger();

    @Override
    public boolean applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged) {
        MergeResult<NullRestrictions> mergeResult = nullRestrictionsMerger.merge(
            left.getNullRestrictions(),
            right.getNullRestrictions());

        if (!mergeResult.successful){
            return false;
        }

        merged.setNullRestrictions(mergeResult.restrictions);
        return true;
    }
}


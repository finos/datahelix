package com.scottlogic.deg.generator.restrictions;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class SetRestrictionsMerger {
    public MergeResult<SetRestrictions> merge(SetRestrictions left, SetRestrictions right) {
        return SetRestrictions.merge(left, right);
    }
}

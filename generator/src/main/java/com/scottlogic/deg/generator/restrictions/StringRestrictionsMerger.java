package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.generation.StringGenerator;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class StringRestrictionsMerger {
    public MergeResult<StringRestrictions> merge(StringRestrictions left, StringRestrictions right) {
        if (left == null && right == null)
            return new MergeResult<>(null);
        if (left == null)
            return new MergeResult<>(right);
        if (right == null)
            return new MergeResult<>(left);

        StringGenerator mergedStringBuilder = left.stringGenerator.intersect(right.stringGenerator);

        StringRestrictions newRestrictions = new StringRestrictions();
        newRestrictions.stringGenerator = mergedStringBuilder;

        return new MergeResult<>(newRestrictions);
    }
}

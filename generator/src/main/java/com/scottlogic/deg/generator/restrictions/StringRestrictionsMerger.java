package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.utils.IStringGenerator;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class StringRestrictionsMerger {
    public StringRestrictions merge(StringRestrictions left, StringRestrictions right) {
        if (left == null && right == null)
            return null;
        if (left == null)
            return right;
        if (right == null)
            return left;

        IStringGenerator mergedStringBuilder = left.stringGenerator.intersect(right.stringGenerator);

        StringRestrictions newRestrictions = new StringRestrictions();
        newRestrictions.stringGenerator = mergedStringBuilder;

        return newRestrictions;
    }
}

package com.scottlogic.deg.generator.restrictions;

public class TypeRestrictionsMerger {
    public ITypeRestrictions merge(ITypeRestrictions left, ITypeRestrictions right) {
        if (left == null && right == null)
            return null;
        if (left == null)
            return right;
        if (right == null)
            return left;

        final ITypeRestrictions merged = left.intersect(right);

        if (merged == null) {
            throw new UnmergeableRestrictionException(
                    "Unable to merge type restrictions - two or more conflicting types found");
        }

        return merged;
    }
}

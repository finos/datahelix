package com.scottlogic.deg.generator.restrictions;

public class TypeRestrictionsMerger {
    public MergeResult<ITypeRestrictions> merge(ITypeRestrictions left, ITypeRestrictions right) {
        if (left == null && right == null)
            return new MergeResult<>(null);
        if (left == null)
            return new MergeResult<>(right);
        if (right == null)
            return new MergeResult<>(left);

        final ITypeRestrictions merged = left.intersect(right);

        if (merged == null) {
            return new MergeResult();
        }

        return new MergeResult<>(merged);
    }
}

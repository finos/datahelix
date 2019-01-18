package com.scottlogic.deg.generator.restrictions;

public class TypeRestrictionsMerger {
    public MergeResult<TypeRestrictions> merge(TypeRestrictions left, TypeRestrictions right) {
        if (left == null && right == null)
            return new MergeResult<>(null);
        if (left == null)
            return new MergeResult<>(right);
        if (right == null)
            return new MergeResult<>(left);

        final TypeRestrictions merged = left.intersect(right);

        if (merged == null) {
            return new MergeResult<>(DataTypeRestrictions.NO_TYPES_PERMITTED);
        }

        return new MergeResult<>(merged);
    }
}

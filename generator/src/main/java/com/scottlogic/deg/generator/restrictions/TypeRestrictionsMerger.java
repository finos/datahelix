package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint.Types;

public class TypeRestrictionsMerger {
    public TypeRestrictions merge(TypeRestrictions left, TypeRestrictions right) {
        if (left == null && right == null)
            return null;
        if (left == null)
            return right;
        if (right == null)
            return left;

        final TypeRestrictions merged = new TypeRestrictions();
        merged.type = getMergedTypes(left.type, right.type);

        return merged;
    }

    private Types getMergedTypes(Types left, Types right) {
        if (left == null && right == null) {
            return null;
        }
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        if (left == right) {
            return left;
        }

        throw new UnsupportedOperationException();
    }
}

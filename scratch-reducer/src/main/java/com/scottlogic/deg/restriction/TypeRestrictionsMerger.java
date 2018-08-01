package com.scottlogic.deg.restriction;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint.Types;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class TypeRestrictionsMerger {
    public TypeRestrictions merge(TypeRestrictions left, TypeRestrictions right) {
        final TypeRestrictions merged = new TypeRestrictions();
        merged.type = getMergedNullness(left.type, right.type);

        return merged;
    }

    private Types getMergedNullness(Types left, Types right) {
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
